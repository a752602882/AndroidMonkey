package dome.ninebox.com.androidmonkey.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Administrator on 2016/5/4.
 */
public class DotaMaxDBHelper extends SQLiteOpenHelper {

    public static final String DB_NAME = "DOTA";
    public static final int VERSION =2;

    private static DotaMaxDBHelper mHelper=null;
    public static final String TABLE_NAME = "Match";
    /**
     *  获得类对象
     */
    public  static  DotaMaxDBHelper getInstance(Context mContext) {
        if (mHelper == null) {
            mHelper = new DotaMaxDBHelper(mContext);
        }
        return mHelper;
    }
        /**
         * heroes创建英雄语句
         */
    public static final String CREATE_HEROES= "create table Heroes ("
            + "hid integer primary key autoincrement, "
            + "id integer, "
            + "name text, "
            + "localized_name text)";

    public static final String CREATE_ITEMS= "create table Items ("
            + "_id integer primary key , "
            + "cost integer, "
            + "name text, "
            + "side_shop integer, "
            + "recipe integer, "
            + "secret_shop integer)";


    public static final String CREATE_MATCH = "create table Match ("
            + "_id integer primary key autoincrement, "
            + "match_id integer, "
            + "account_id integer, "
            + "steam_id integer, "
            + "start_time integer, "
            + "player_slot integer, "
            + "item_0 text, "
            + "item_1 text, "
            + "item_2 text, "
            + "item_3 text, "
            + "item_4 text, "
            + "item_5 text, "
            + "kills integer, "
            + "deaths integer, "
            + "assists integer, "
            + "hero_damage integer, "
            + "tower_damage integer, "
            + "hero_healing integer, "
            + "level integer, "
            + "gold_per_min integer, "
            + "xp_per_min integer, "
            + "radiant_win integer, "
            + "person_name text, "
            + "avatar text, "
            + "imageUrl text)";
    private static final String DROP_HEROES="drop table if exists Heroes";
    private static final String DROP_ITEMS="drop table if exists Items";
    private static final String DROP_MATCH="drop table if exists Match";

    private DotaMaxDBHelper(Context context) {
        super(context, DB_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_HEROES);
        db.execSQL(CREATE_ITEMS);
        db.execSQL(CREATE_MATCH);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(DROP_HEROES);
        db.execSQL(DROP_MATCH);
        db.execSQL(DROP_ITEMS);

        db.execSQL(CREATE_HEROES);
        db.execSQL(CREATE_ITEMS);
        db.execSQL(CREATE_MATCH);
    }


}
