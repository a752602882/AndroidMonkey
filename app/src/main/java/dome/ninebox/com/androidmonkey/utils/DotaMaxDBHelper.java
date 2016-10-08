package dome.ninebox.com.androidmonkey.utils;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

/**
 * Created by Administrator on 2016/5/4.
 */
public class DotaMaxDBHelper extends SQLiteOpenHelper {

    public static final String DB_NAME = "DOTA";
    public static final int VERSION =2;

    private static DotaMaxDBHelper mHelper=null;

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
    public static final String CREATE_MATCH = "create table Match ("
            + "hid integer primary key autoincrement, "
            + "match_id integer, "
            + "account_id integer, "
            + "start_time integer, "
            + "player_slot integer, "
            + "item_0 integer, "
            + "item_1 integer, "
            + "item_2 integer, "
            + "item_3 integer, "
            + "item_4 integer, "
            + "item_5 integer, "
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
            + "imageUrl text)";
    private static final String DROP_HEROES="drop table if exists Heroes";
    private static final String DROP_MATCH="drop table if exists Match";

    private DotaMaxDBHelper(Context context) {
        super(context, DB_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_HEROES);
        db.execSQL(CREATE_MATCH);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(DROP_HEROES);
        db.execSQL(DROP_MATCH);
        db.execSQL(CREATE_HEROES);
        db.execSQL(CREATE_MATCH);
    }


}
