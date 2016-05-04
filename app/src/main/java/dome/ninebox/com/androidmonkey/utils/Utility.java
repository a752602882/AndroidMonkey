package dome.ninebox.com.androidmonkey.utils;

import android.text.TextUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import dome.ninebox.com.androidmonkey.model.Heroes;

/**
 * Created by Administrator on 2016/5/4.
 */
public class Utility {

    /**
     * 解析和处理服务器返回的英雄数据
     */
    public synchronized static boolean handleHeroesResponse(DotaMaxDB dotaMaxDB,JSONObject response){



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

                dotaMaxDB.saveHeroes(hero);

            }


        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }


        return false;
    }
}
