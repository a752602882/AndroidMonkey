package dome.ninebox.com.androidmonkey.db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import dome.ninebox.com.androidmonkey.model.Heroes;
import dome.ninebox.com.androidmonkey.model.Items;
import dome.ninebox.com.androidmonkey.model.MatchDetails;
import dome.ninebox.com.androidmonkey.model.User;

/**
 * Created by Administrator on 2016/7/15.
 */
public class DotaMaxDAOImpl implements DotaMaxDAO {


    public DotaMaxDBHelper mDBHelper = null;

    public DotaMaxDAOImpl(Context context) {
        mDBHelper = DotaMaxDBHelper.getInstance(context);
    }

    @Override
    public synchronized void insertHeroes(List<Heroes> heroList) {

        SQLiteDatabase db = mDBHelper.getWritableDatabase();
        for (int i = 0; i < heroList.size(); i++) {
            Cursor c = db.rawQuery("select * from Heroes where id=?", new String[]{String.valueOf(heroList.get(i).getId())});
            if (c.getCount() == 0) {
                db.execSQL("insert into Heroes(id,name,localized_name) values(?,?,?)",
                        new Object[]{heroList.get(i).getId(), heroList.get(i).getName(), heroList.get(i).getLocalized_name()});
            }
        }
    }

    @Override
    public void insertItems(List<Items> itemsList) {
        SQLiteDatabase db = mDBHelper.getWritableDatabase();
        for (int i = 0; i < itemsList.size(); i++) {

            Cursor c = db.rawQuery("select * from Items where _id=?", new String[]{String.valueOf(itemsList.get(i).get_id())});
            if (c.getCount() == 0) {
                db.execSQL("insert into Items(_id,cost,name,secret_shop,side_shop,recipe) values(?,?,?,?,?,?)",
                        new Object[]{itemsList.get(i).get_id(), itemsList.get(i).getCost(), itemsList.get(i).getName(),
                                itemsList.get(i).getSecret_shop(), itemsList.get(i).getSide_shop(), itemsList.get(i).getRecipe()});
            }

        }
    }

    @Override
    public Heroes getHeroes(int id) {
        SQLiteDatabase db = mDBHelper.getReadableDatabase();
        List<Heroes> list = new ArrayList<Heroes>();
    /*    Cursor cursor = db.query("Heroes", null,
                null, null, null, null, null);*/
        Cursor cursor = db.rawQuery("select * from Heroes where  id=?", new String[]{id + ""});
        while (cursor.moveToNext()) {
            Heroes hero = new Heroes();
            hero.setId(cursor.getInt(cursor.getColumnIndex("id")));
            hero.setName(cursor.getString(cursor.getColumnIndex("name")));
            hero.setLocalized_name(cursor.getString(cursor.getColumnIndex("localized_name")));
            return hero;

        }

        return null;
    }

    @Override
    public void insertMatch(MatchDetails match) {

        SQLiteDatabase db = mDBHelper.getWritableDatabase();
        String sql = "insert into Match (match_id , " +
                "account_id , " +
                "steam_id , " +
                "start_time , " +
                "player_slot , " +
                "item_0 , " +
                "item_1 , " +
                "item_2 , " +
                "item_3 , " +
                "item_4 , " +
                "item_5 , " +
                "kills , " +
                "deaths , " +
                "assists , " +
                "hero_damage , " +
                "tower_damage , " +
                "hero_healing , " +
                "level , " +
                "gold_per_min , " +
                "xp_per_min , " +
                "radiant_win , " +
                "imageUrl  ," +
                "avatar  ," +
                "person_name ) " +
                "values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";


        db.execSQL(sql, new Object[]{match.getMatch_id(), match.getAccount_id(), match.getSteam_id(), match.getStart_time(), match.getPlayer_slot(),
                match.getItem_0(), match.getItem_1(), match.getItem_2(), match.getItem_3(), match.getItem_4(), match.getItem_5(),
                match.getKills(), match.getDeaths(), match.getAssists(), match.getHero_damage(), match.getTower_damage(), match.getHero_healing(),
                match.getLevel(), match.getGold_per_min(), match.getXp_per_min(), match.getRadiant_win(), match.getImageUrl(), match.getAvatar(), match.getPerson_name()});

    }

    @Override
    public MatchDetails getMatch(long id) {

        SQLiteDatabase db = mDBHelper.getReadableDatabase();
        List<MatchDetails> list = new ArrayList<MatchDetails>();
    /*    Cursor cursor = db.query("Heroes", null,
                null, null, null, null, null);*/
        Cursor cursor = db.rawQuery("select * from Match where  match_id=?", new String[]{id + ""});
        while (cursor.moveToNext()) {
            MatchDetails match = new MatchDetails();
            match.setMatch_id(cursor.getInt(cursor.getColumnIndex("match_id")));
            match.setAccount_id(cursor.getInt(cursor.getColumnIndex("account_id")));
            match.setStart_time(cursor.getInt(cursor.getColumnIndex("start_time")));
            match.setPlayer_slot(cursor.getInt(cursor.getColumnIndex("player_slot")));
            match.setItem_0(cursor.getString(cursor.getColumnIndex("item_0")));
            match.setItem_1(cursor.getString(cursor.getColumnIndex("item_1")));
            match.setItem_2(cursor.getString(cursor.getColumnIndex("item_2")));
            match.setItem_3(cursor.getString(cursor.getColumnIndex("item_3")));
            match.setItem_4(cursor.getString(cursor.getColumnIndex("item_4")));
            match.setItem_5(cursor.getString(cursor.getColumnIndex("item_5")));
            match.setKills(cursor.getInt(cursor.getColumnIndex("kills")));
            match.setDeaths(cursor.getInt(cursor.getColumnIndex("deaths")));
            match.setAssists(cursor.getInt(cursor.getColumnIndex("assists")));
            match.setHero_damage(cursor.getInt(cursor.getColumnIndex("hero_damage")));
            match.setTower_damage(cursor.getInt(cursor.getColumnIndex("tower_damage")));
            match.setHero_healing(cursor.getInt(cursor.getColumnIndex("hero_healing")));
            match.setLevel(cursor.getInt(cursor.getColumnIndex("level")));
            match.setGold_per_min(cursor.getInt(cursor.getColumnIndex("gold_per_min")));
            match.setXp_per_min(cursor.getInt(cursor.getColumnIndex("xp_per_min")));
            match.setRadiant_win(cursor.getInt(cursor.getColumnIndex("radiant_win")));
            match.setImageUrl(cursor.getString(cursor.getColumnIndex("imageUrl")));

            return match;

        }

        return null;
    }

    @Override
    public List<MatchDetails> getMatchByAccountId(long account_id) {

        List<MatchDetails> matchDetailsList = new ArrayList<>();

        SQLiteDatabase db = mDBHelper.getReadableDatabase();
        List<MatchDetails> list = new ArrayList<MatchDetails>();
        Cursor cursor = db.rawQuery("select * from Match where  account_id=? ORDER BY  start_time DESC  limit 2", new String[]{account_id + ""});
        while (cursor.moveToNext()) {
            MatchDetails match = new MatchDetails();
            match.setMatch_id(cursor.getInt(cursor.getColumnIndex("match_id")));
            match.setAccount_id(cursor.getInt(cursor.getColumnIndex("account_id")));
            match.setStart_time(cursor.getInt(cursor.getColumnIndex("start_time")));
            match.setPlayer_slot(cursor.getInt(cursor.getColumnIndex("player_slot")));
            match.setItem_0(cursor.getString(cursor.getColumnIndex("item_0")));
            match.setItem_1(cursor.getString(cursor.getColumnIndex("item_1")));
            match.setItem_2(cursor.getString(cursor.getColumnIndex("item_2")));
            match.setItem_3(cursor.getString(cursor.getColumnIndex("item_3")));
            match.setItem_4(cursor.getString(cursor.getColumnIndex("item_4")));
            match.setItem_5(cursor.getString(cursor.getColumnIndex("item_5")));
            match.setKills(cursor.getInt(cursor.getColumnIndex("kills")));
            match.setDeaths(cursor.getInt(cursor.getColumnIndex("deaths")));
            match.setAssists(cursor.getInt(cursor.getColumnIndex("assists")));
            match.setHero_damage(cursor.getInt(cursor.getColumnIndex("hero_damage")));
            match.setTower_damage(cursor.getInt(cursor.getColumnIndex("tower_damage")));
            match.setHero_healing(cursor.getInt(cursor.getColumnIndex("hero_healing")));
            match.setLevel(cursor.getInt(cursor.getColumnIndex("level")));
            match.setGold_per_min(cursor.getInt(cursor.getColumnIndex("gold_per_min")));
            match.setXp_per_min(cursor.getInt(cursor.getColumnIndex("xp_per_min")));
            match.setRadiant_win(cursor.getInt(cursor.getColumnIndex("radiant_win")));
            match.setImageUrl(cursor.getString(cursor.getColumnIndex("imageUrl")));
            matchDetailsList.add(match);


        }

        if (matchDetailsList.size() > 0) {
            return matchDetailsList;
        }

        return null;
    }

    @Override
    public String getItemsNameById(String item_id) {
        SQLiteDatabase db = mDBHelper.getReadableDatabase();


        Cursor cursor = db.rawQuery("select * from Items where  _id=?", new String[]{item_id});
        if (cursor.getCount() != 0) {
            while (cursor.moveToNext()) {
                String name = cursor.getString(cursor.getColumnIndex("name"));
                return name;
            }
        }
        return null;
    }

    @Override
    public synchronized void insertUser(User user) {
        SQLiteDatabase db = mDBHelper.getWritableDatabase();
        if (user.getName() != null) {
            Cursor c = db.rawQuery("select * from User where steam_id=?", new String[]{String.valueOf(user.getSteam_id())});
            Log.d("TAG", c.getCount() + "");
            if (c.getCount() <= 0) {
                db.execSQL("insert into User(steam_id,name,avatarmedium) values(?,?,?)",
                        new Object[]{user.getSteam_id(), user.getName(), user.getAvatarmedium()});
            }
        }
    }

    public synchronized List<Long> getUser() {
        List<Long> steamIds =new ArrayList<>();
        SQLiteDatabase db = mDBHelper.getWritableDatabase();
        Cursor c = db.rawQuery("select * from User", null);
        Log.d("TAG", c.getCount() + "");
        if (c.getCount() != 0) {
            while (c.moveToNext()) {
                Long  steam_id= c.getLong(c.getColumnIndex("steam_id"));
                steamIds.add(steam_id);
            }
            return steamIds;
        }
        return null;

    }

}
