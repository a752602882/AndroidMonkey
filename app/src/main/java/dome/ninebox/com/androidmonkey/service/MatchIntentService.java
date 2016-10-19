package dome.ninebox.com.androidmonkey.service;

import android.annotation.TargetApi;
import android.app.IntentService;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.IBinder;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.android.volley.VolleyLog;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/9/18.
 */
public class MatchIntentService extends IntentService {
    public static final String BROADCAST_ACTION = "dome.ninebox.com.androidmonkey.adapter.MyRecyclerViewAdapter";

    public static final String EXTENDED_DATA= "Date";



    private final String TAG = "TAG";
    List<String> matches =new ArrayList<String>();

    public MatchIntentService() {
        super("sss");
        Log.d(TAG, "服务启动: ");
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    @Override
    protected void onHandleIntent(Intent intent) {

        String url = null;
        HttpURLConnection con = null;
        url = intent.getStringExtra("URL");
        InputStream input = null;
        StringBuffer jsonBuilder =new StringBuffer();


        try {
            URL mUrl = new URL(url);
            con = (HttpURLConnection) mUrl.openConnection();
            con.setRequestMethod("GET");
            con.setConnectTimeout(3000);
            con.setRequestProperty("Charset", "UTF-8");

            if (con.getResponseCode() == HttpURLConnection.HTTP_OK) {
                int len = -1;
                //in = new BufferedInputStream(con.getInputStream(), 8 * 1024);
                input=con.getInputStream();
                byte[] buffer = new byte[1024*4];
                while((len=input.read(buffer))!=-1){
                   Log.d(TAG, "buffer is :"+buffer);
                   jsonBuilder.append(new String(buffer,0,len, StandardCharsets.UTF_8));
                    Log.d(TAG, "jsonBuilder is :"+jsonBuilder.toString());

                }

                if (jsonBuilder.length()<=0){
                    Log.d("TAG","-----读取json数据错误----");
                    return;
                }

              JSONObject mMatchIds = new JSONObject(jsonBuilder.toString());
              JSONArray array =  mMatchIds.getJSONObject("result").getJSONArray("matches");
                for (int i = 0; i<array.length();i++) {
                    String match_id = null;
                    JSONObject user = (JSONObject) array.get(i) ;
                    match_id = user.getString("match_id");
               //     VolleyLog.d(" match_id--->%s", match_id);

                    matches.add(match_id);

                    //dotaMaxDB.saveHeroes(hero);

                }

            }

        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            try {
                input.close();
                con.disconnect();
                Intent intent1 = new Intent(BROADCAST_ACTION);
                intent1.putStringArrayListExtra(EXTENDED_DATA, (ArrayList<String>) matches);

                LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(intent1);


            } catch (IOException e) {
                e.printStackTrace();
            }

        }


    }







}
