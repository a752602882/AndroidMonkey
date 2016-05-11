package dome.ninebox.com.androidmonkey.utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import dome.ninebox.com.androidmonkey.model.Heroes;
import dome.ninebox.com.androidmonkey.model.MatchDetails;

/**
 * Created by Administrator on 2016/5/4.
 */
public class DotaMaxDB {

    /**
     * 数据库名
     */

    public static final String DB_NAME = "DOTA";

    /**
     * 数据库版本
     */
    public static final int VERSION = 3;

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


    public void saveHeroes(List<Heroes> hero) {

        db.beginTransaction();
        try{
            //在这里执行多个数据库操作
            //执行过程中可能会抛出异常
            if (hero.size()>0) {
                for (int i = 0; i < hero.size(); i++) {


                    ContentValues values = new ContentValues();
                    values.put("id", hero.get(i).getId());
                    values.put("name", hero.get(i).getName());
                    values.put("localized_name", hero.get(i).getLocalized_name());

                    db.insert("Heroes", null, values);


//                String sql = "insert into Heroes values ("+ hero.get(i).getId()+ "," +  hero.get(i)+ ","+  hero.get(i).getLocalized_name()+")";
//
//                System.out.println(sql);
//
//                db.execSQL(sql);

                }

                db.setTransactionSuccessful();
            }
            //在setTransactionSuccessful和endTransaction之间不进行任何数据库操作
        }catch(Exception e){
            //当数据库操作出现错误时，需要捕获异常，结束事务
            db.endTransaction();
            throw e;
        }finally{
            db.endTransaction(); //处理完成
            db.close();

        }
        //当所有操作执行完成后结束一个事务

       // db.endTransaction();



//        if (hero != null) {
//            ContentValues values = new ContentValues();
//            values.put("id", hero.getId());
//            values.put("name", hero.getName());
//            values.put("localized_name", hero.getLocalized_name());
//
//            db.insert("Heroes", null, values);
//        }
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
