package dome.ninebox.com.androidmonkey.fragment;

import android.annotation.TargetApi;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.util.Log;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import dome.ninebox.com.androidmonkey.MyApplication;
import dome.ninebox.com.androidmonkey.db.DotaMaxDAO;
import dome.ninebox.com.androidmonkey.model.Heroes;
import dome.ninebox.com.androidmonkey.model.MatchDetails;
import dome.ninebox.com.androidmonkey.service.MatchIntentService;
import dome.ninebox.com.androidmonkey.utils.Utility;
import dome.ninebox.com.androidmonkey.widget.MessageResponse;

/**
 * Created by Administrator on 2016/11/4.
 */
public class MyBroadcast extends BroadcastReceiver implements MessageResponse {


    public Long steam_id = 0L;

    /**
     * 记录所有正在下载或等待下载的任务。
     */
    private Set<MatchInfoTask> taskCollection = new HashSet<>();
    private List<MatchDetails> mUserDetails = new ArrayList<>();
    private Context mContext;

    public MyBroadcast(Context mContext) {
        this.mContext = mContext;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        List<String> ids = new ArrayList<>();
        steam_id = intent.getLongExtra(MatchIntentService.STEAM_ID, 0);
        ids = intent.getStringArrayListExtra(MatchIntentService.EXTENDED_DATA);
        Log.d("TAG", "++++你好++++");
        DotaMaxDAO dao = MyApplication.getDb();

        if (ids == null) {
            return;
        }

        for (int i = 0; i < ids.size(); i++) {

            try {
                long intNum = Long.parseLong(ids.get(i).trim());
                Log.d("TAG", "intNum: " + intNum);
                if (dao.getMatch(intNum) == null) {
                    MatchInfoTask task = new MatchInfoTask();
                    task.setResponse(this);
                    taskCollection.add(task);
                    task.execute(ids.get(i));
                }
            } catch (Exception e) {
                e.getMessage();
            }


        }

    }

    @Override
    public void onReceivedSuccess(List<MatchDetails> msg) {

        final Map<MatchDetails, Heroes> listHeroes = new HashMap<>();

        for (MatchDetails details : msg) {

            if (details.getAccount_id() == 125690482 || details.getAccount_id() == steam_id) {
                mUserDetails.add(details);
                HttpReadUserInfo(details);
            }
        }
    }

    public void HttpReadUserInfo(final MatchDetails md) {
        RequestQueue mQueue = Volley.newRequestQueue(mContext);
        String url = "http://api.steampowered.com/ISteamUser/GetPlayerSummaries/" +
                "v0002/?key=BAA464D3B432D062BEA99BA753214681&steamids=" + md.getSteam_id();
        JsonObjectRequest jsonRequest = new JsonObjectRequest(url, null,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {

                        System.out.println("请求用户信息成功！response--" + response.toString());
                        //设置用户的头像和名称
                        Utility.handleUserInfoResponse(md, response);
                       //设置物品信息
                        DotaMaxDAO db = MyApplication.getDb();
                        String item_0 = db.getItemsNameById(md.getItem_0());
                        md.setItem_0(item_0);
                        String item_1 = db.getItemsNameById(md.getItem_1());
                        md.setItem_1(item_1);
                        String item_2 = db.getItemsNameById(md.getItem_2());
                        md.setItem_2(item_2);
                        String item_3 = db.getItemsNameById(md.getItem_3());
                        md.setItem_3(item_3);
                        String item_4 = db.getItemsNameById(md.getItem_4());
                        md.setItem_4(item_4);
                        String item_5 = db.getItemsNameById(md.getItem_5());
                        md.setItem_5(item_5);
                        db.insertMatch(md);

                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println("请求用户信息失败！ error--" + error);
            }
        });
        mQueue.add(jsonRequest);
    }


    /**
     * 通过比赛Id,获得这局10个玩家的信息
     */
    class MatchInfoTask extends AsyncTask<String, Void, List<MatchDetails>> {

        public void setResponse(MessageResponse response) {
            this.response = response;
        }

        public MessageResponse response;


        @TargetApi(Build.VERSION_CODES.KITKAT)
        @Override
        protected List<MatchDetails> doInBackground(String... params) {

            List<MatchDetails> mds = new ArrayList<>();
            String url = "https://api.steampowered.com/IDOTA2Match_570/GetMatchDetails/V001/" +
                    "?key=BAA464D3B432D062BEA99BA753214681&match_id=";
            url += params[0];
            HttpURLConnection con = null;
            InputStream input = null;
            URL mUrl = null;
            StringBuffer jsonBuilder = new StringBuffer();
            try {

                mUrl = new URL(url);

                con = (HttpURLConnection) mUrl.openConnection();

                con.setRequestMethod("GET");
                con.setConnectTimeout(3000);
                con.setRequestProperty("Charset", "UTF-8");

                if (con.getResponseCode() == HttpURLConnection.HTTP_OK) {
                    int len = -1;
                    //in = new BufferedInputStream(con.getInputStream(), 8 * 1024);
                    input = con.getInputStream();
                    byte[] buffer = new byte[1024 * 4];
                    while ((len = input.read(buffer)) != -1) {
                        //      Log.d("TAG", "buffer is :" + buffer);
                        jsonBuilder.append(new String(buffer, 0, len, StandardCharsets.UTF_8));
                        //     Log.d("TAG", "jsonBuilder is :" + jsonBuilder.toString());


                    }
                    buffer = null;

                    if (jsonBuilder.length() <= 0) {
                        Log.d("TAG", "-----读取json数据错误----");
                        return null;
                    }

                    JSONObject matchInfo = new JSONObject(jsonBuilder.toString());
                    mds = Utility.handleMatchDetailsResponse(matchInfo);


                }


            } catch (Exception e) {
                e.printStackTrace();
            }

             /*   matchDetails.add(mds);
                Log.d("TAG", "---------------  :" + mds.get(0).getHero_id());*/
            return mds;

        }

        @Override
        protected void onPostExecute(List<MatchDetails> objects) {

            //  super.onPostExecute(objects);
            response.onReceivedSuccess(objects);
        }
    }

    /**
     * 取消所有正在下载或等待下载的任务。
     */
    public void cancelAllTasks() {
        if (taskCollection != null) {
            for (MatchInfoTask task : taskCollection) {
                task.cancel(false);
            }
        }
    }


}
