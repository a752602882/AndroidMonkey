package dome.ninebox.com.androidmonkey;

import android.app.Application;
import android.content.Context;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;


import org.json.JSONObject;

import dome.ninebox.com.androidmonkey.utils.DotaMaxDAO;
import dome.ninebox.com.androidmonkey.utils.DotaMaxDAOImpl;
import dome.ninebox.com.androidmonkey.utils.Utility;

/**
 * Created by Administrator on 2016/5/3.
 */
public class MyApplication extends Application {
    private static Context content;
    private static DotaMaxDAO db =null;


   private static RequestQueue mRequestQueue ;



    public static DotaMaxDAO getDb() {
        return db;
    }


    public static RequestQueue getmRequestQueue() {
        return mRequestQueue;
    }

    @Override
    public void onCreate() {
        super.onCreate();
      /*  JLog.init(this)
                .setDebug(BuildConfig.DEBUG);*/
        content = getApplicationContext();
        db = new DotaMaxDAOImpl(content);

        //把英雄放进数据库
      //  while(db.getHeroes(101)==null)
        HttpReadHeroes();// 暂时不需要

      mRequestQueue = Volley.newRequestQueue(getApplicationContext());


    }






    public static void HttpReadHeroes() {
        RequestQueue mQueue = Volley.newRequestQueue(content);
        String url ="https://api.steampowered.com/IEconDOTA2_570/GetHeroes/v0001/?" +
                "key=BAA464D3B432D062BEA99BA753214681&language=zh_cn";
        JsonObjectRequest jsonRequest = new JsonObjectRequest(url, null,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        System.out.println("请求英雄成功！response--" + response.toString());
                        Utility.handleHeroesResponse(db,response);


                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println("请求英雄失败！ error--"+error);
            }
        });
        mQueue.add(jsonRequest);
    }

    public static Context getContext() {
        return content;
    }

}
