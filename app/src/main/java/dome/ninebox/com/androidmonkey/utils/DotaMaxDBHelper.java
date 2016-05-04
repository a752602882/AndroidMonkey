package dome.ninebox.com.androidmonkey.utils;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Administrator on 2016/5/4.
 */
public class DotaMaxDBHelper extends SQLiteOpenHelper {

    /**
     * heroes创建英雄语句
     */
    public static final String CREATE_HEROES= "create table Heroes ("
            + "hid integer primary key autoincrement, "
            + "id integer, "
            + "province_name text, "
            + "province_code text)";

    public DotaMaxDBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_HEROES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
