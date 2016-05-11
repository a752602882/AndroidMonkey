package dome.ninebox.com.androidmonkey.utils;

import android.text.TextUtils;


import com.android.volley.VolleyLog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


import dome.ninebox.com.androidmonkey.model.Heroes;
import dome.ninebox.com.androidmonkey.model.MatchDetails;

/**
 * Created by Administrator on 2016/5/4.
 */
public class Utility {

    /**
     * 解析和处理服务器返回的英雄数据
     */
    public synchronized static boolean handleHeroesResponse(DotaMaxDB dotaMaxDB,JSONObject response){


        List<Heroes> list = new ArrayList<Heroes>();
        try {
            JSONObject josn =  response;
            JSONArray jsonArray = josn.getJSONObject("result").getJSONArray("heroes");

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject user = (JSONObject) jsonArray.get(i) ;
                String name = (String)user.get("name");
                int id  = (int)user.get("id");
                String localized_name= (String)user.get("localized_name");

                Heroes  hero = new Heroes();
                hero.setName(name);
                hero.setId(id);
                hero.setLocalized_name(localized_name);
                list.add(hero);
            }

          dotaMaxDB.saveHeroes(list);

        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }





        return true;
    }

    /**
     * 解析和处理服务器返回的25场比赛简单数据，并返回一个matches集合
     */
    public synchronized static List<String> handleMatchesResponse(JSONObject response){


        List<String> matches =new ArrayList<String>();
        JSONObject json =  response;
        String match_id="";


        JSONArray jsonArray = null;
        try {
            jsonArray = json.getJSONObject("result").getJSONArray("matches");
            VolleyLog.d("jsonArray matches--->%d", jsonArray.length());
            for (int i = 0; i<jsonArray.length();i++) {
                JSONObject user = (JSONObject) jsonArray.get(i) ;
                match_id = user.getString("match_id");
                VolleyLog.d(" match_id--->%s", match_id);

                matches.add(match_id);

                //   dotaMaxDB.saveHeroes(hero);

            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return matches;
    }

    public synchronized static List<MatchDetails> handleMatchDetailsResponse(JSONObject response) {


        List<MatchDetails> list = null;
        int start_time;
        String match_id=null;
        boolean radiant_win;
        try {
            JSONObject josn =  response;
            JSONObject matchesExternal =josn.getJSONObject("result");
             // start_time = (int)matchesExternal.get("start_time");
             // match_id = (String)matchesExternal.get("match_id");
             //radiant_win=(boolean)matchesExternal.get("radiant_win");

            JSONArray jsonArray = josn.getJSONObject("result").getJSONArray("players");

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject player = (JSONObject) jsonArray.get(i) ;
             //   String account_id = (String)player.get("account_id");
              //  String start_time = (String)player.get("start_time");
            //    String start_time = (String)player.get("match_id");
                int player_slot = (int)player.get("player_slot");
                int hero_id = (int)player.get("hero_id");
                int item_0 = (int)player.get("item_0");
                int item_1 = (int)player.get("item_1");
                int item_2 = (int)player.get("item_2");
                int item_3 = (int)player.get("item_3");
                int item_4 = (int)player.get("item_4");
                int item_5 = (int)player.get("item_5");
                int kills = (int)player.get("kills");
                int deaths = (int)player.get("deaths");
                int assists = (int)player.get("assists");
                int hero_damage = (int)player.get("hero_damage");
                int tower_damage = (int)player.get("tower_damage");
                int hero_healing = (int)player.get("hero_healing");
                int level = (int)player.get("level");

                int gold_per_min = (int)player.get("gold_per_min");
                int xp_per_min = (int)player.get("xp_per_min");

                //int  radiant_win= (int)player.get("radiant_win");

                MatchDetails md = new MatchDetails();
              //  md.setAccount_id(account_id);
                md.setHero_id(hero_id);
                md.setPlayer_slot(player_slot);
                md.setItem_0(item_0);
                md.setItem_1(item_1);
                md.setItem_2(item_2);
                md.setItem_3(item_3);
                md.setItem_4(item_4);
                md.setItem_5(item_5);
                md.setKills(kills);
                md.setDeaths(deaths);
                md.setAssists(assists);
                md.setHero_damage(hero_damage);
                md.setHero_healing(hero_healing);
                md.setTower_damage(tower_damage);
                md.setLevel(level);
                md.setGold_per_min(gold_per_min);
                md.setXp_per_min(xp_per_min);

                //md.setStart_time(start_time);
            //    md.setMatch_id(match_id);
            //    md.setRadiant_win(radiant_win);
                list.add(md);
            }


        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }



        return  list;
    }
}
