package dome.ninebox.com.androidmonkey.utils;

import android.app.Activity;
import android.text.TextUtils;


import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

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



    //返回方法
    public interface VolleyCallback{
        void onSuccess(JSONObject result);
    }



    //读取个人25场网络数据
    public synchronized static void HttpReadMatches(Activity activity, final VolleyCallback callback) {

        RequestQueue mQueue = Volley.newRequestQueue(activity);
        String url ="https://api.steampowered.com/IDOTA2Match_570/GetMatchHistory/V001/?" +
                "key=BAA464D3B432D062BEA99BA753214681&matches_requested=25&account_id=125690482";
        JsonObjectRequest jsonRequest = new JsonObjectRequest(url, null,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        System.out.println("请求英雄成功！response--" + response.toString());
                     //  Utility.handleMatchesResponse(response);
                        callback.onSuccess(response);
                        }


                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println("请求mathes失败！ error--"+error);
            }
        });
        mQueue.add(jsonRequest);

    }









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
    //这个是一场比赛的详情
    public synchronized static  void HttpReadMatchDetails(String match,Activity activity, final VolleyCallback callback) {
        RequestQueue mQueue = Volley.newRequestQueue(activity);
        String url ="https://api.steampowered.com/IDOTA2Match_570/GetMatchDetails" +
                "/V001/?key=BAA464D3B432D062BEA99BA753214681&match_id=" +match;
        JsonObjectRequest jsonRequest = new JsonObjectRequest(url, null,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        System.out.println("请求单场数据成功！response--" + response.toString());

                       // matchDetailses=  Utility.handleMatchDetailsResponse(response);
                        callback.onSuccess(response);


                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println("请求mathes失败！ error--"+error);
            }
        });
        mQueue.add(jsonRequest);
    }


    public  static List<MatchDetails> handleMatchDetailsResponse(JSONObject response) {


        List<MatchDetails> list = new ArrayList<MatchDetails>();;
        int start_time;
        long match_id;
        boolean radiant_win;
        try {
            JSONObject josn =  response;
            JSONObject matchesExternal =josn.getJSONObject("result");
              start_time = (int)matchesExternal.get("start_time");
              match_id = (long)matchesExternal.get("match_id");
             radiant_win=(boolean)matchesExternal.get("radiant_win");

            JSONArray jsonArray = josn.getJSONObject("result").getJSONArray("players");

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject player = (JSONObject) jsonArray.get(i) ;
                System.out.print("----------------------------------------------");
                System.out.print(player.get("account_id").getClass().getName());;

               long account_id =new Long((Integer)player.get("account_id")) ;

              //  String start_time = (String)player.get("start_time");
              //  String start_time = (String)player.get("match_id");
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
               // md.setAccount_id(account_id);
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

                md.setStart_time(start_time);
               md.setMatch_id(match_id);
               md.setRadiant_win(radiant_win);
                list.add(md);
            }


        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }



        return  list;
    }
}
