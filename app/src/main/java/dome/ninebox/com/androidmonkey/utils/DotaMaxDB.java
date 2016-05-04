package dome.ninebox.com.androidmonkey.utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import dome.ninebox.com.androidmonkey.model.Heroes;

/**
 * Created by Administrator on 2016/5/4.
 */
public class DotaMaxDB {

    /**
     * 数据库名
     */

    public static final String DB_NAME = "cool_weather";

    /**
     * 数据库版本
     */
    public static final int VERSION = 1;

    private static DotaMaxDB dotaMaxDB;

    private SQLiteDatabase db;

    /**
     * 将构造方法私有化
     */

    private DotaMaxDB(Context context) {
        DotaMaxDBHelper dbHelper = new DotaMaxDBHelper(context,
                DB_NAME, null, VERSION);
        //方法以读写方式打开数据库，一旦数据库的磁盘空间满了，数据库就只能读而不能写，
        db = dbHelper.getWritableDatabase();
    }

    /**
     * 获取CoolWeatherDB的实例。
     */
    public synchronized static DotaMaxDB getInstance(Context context) {
        if (dotaMaxDB == null) {
            dotaMaxDB = new DotaMaxDB(context);
        }
        return dotaMaxDB;
    }

    /**
     * 将hero实例存储到数据库。
     */


    public void saveHeroes(Heroes hero) {

        if (hero != null) {
            ContentValues values = new ContentValues();
            values.put("id", hero.getId());
            values.put("name", hero.getName());
            values.put("localized_name", hero.getLocalized_name());

            db.insert("Heroes", null, values);
        }
    }

    public List<Heroes> loadHeroes() {

        List<Heroes> list = new ArrayList<Heroes>();
        Cursor cursor = db.query("Heroes", null,
                null, null, null, null, null);
        if (cursor.moveToNext()) {
            do {
                Heroes hero = new Heroes();
                hero.setId(cursor.getInt(cursor.getColumnIndex("id")));
                hero.setName(cursor.getString(cursor.getColumnIndex("name")));
                hero.setLocalized_name(cursor.getString(cursor.getColumnIndex("localized_name")));
                list.add(hero);

            } while (cursor.moveToNext());

        }

        return list;
    }
}
