package dome.ninebox.com.androidmonkey.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import dome.ninebox.com.androidmonkey.MyApplication;
import dome.ninebox.com.androidmonkey.R;
import dome.ninebox.com.androidmonkey.utils.DotaMaxDB;
import dome.ninebox.com.androidmonkey.utils.Utility;

/**
 * Created by Administrator on 2016/4/25.
 */
public class MyRecyclerViewAdapter extends RecyclerView.Adapter<MyRecyclerViewHolder> {


    public Context mContext;
    public List<String> mMatch_ids;

    public LayoutInflater mLayoutInflater;

    public MyRecyclerViewAdapter(Context mContext) {

        this.mContext = mContext;
        mLayoutInflater = LayoutInflater.from(mContext);
        mMatch_ids = new ArrayList<String>();

        queryMatchsID();


    }

    private List<String> queryMatchsID() {
     /*
        RequestQueue mQueue = Volley.newRequestQueue(mContext);
        String url="https://api.steampowered.com/IDOTA2Match_570/GetMatchHistory/V001";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener() {
            @Override
            public void onResponse(Object response) {

                VolleyLog.v("TAGString", response.toString());
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.e("TAGString", error.getMessage(), error);
            }

        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<String, String>();
                map.put("key", "BAA464D3B432D062BEA99BA753214681");
                map.put("account_id", "125690482");
                map.put("matches_requested","25");
                return map;
            }
        };
        stringRequest.setTag("MatchId");
        mQueue.add(stringRequest);
*/
//-------------------------------------
        //----JsonObjectRequest 的用法---用法与StringRequest基本相同----
        RequestQueue mQueue = Volley.newRequestQueue(mContext);
        String url = "https://api.steampowered.com/IDOTA2Match_570/GetMatchHistory/V001/" +
                "?key=BAA464D3B432D062BEA99BA753214681&matches_requested=2&account_id=125690482";
        JsonObjectRequest jsonRequest = new JsonObjectRequest(url, null,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {

                      System.out.println("请求成功！response--"+response.toString());
                      Utility.handleHeroesResponse( MyApplication.getDb(), response);
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println("请求失败！ error--"+error);
            }
        });

        mQueue.add(jsonRequest);

        return  null;

    }


    /**
     * 创建ViewHolder
     */
    @Override
    public MyRecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mLayoutInflater.inflate(R.layout.item_feed, parent, false);
        MyRecyclerViewHolder mViewHodler = new MyRecyclerViewHolder(view);
        return mViewHodler;


    }
    /**
     * 绑定ViewHoler，给item中的控件设置数据
     */
    @Override
    public void onBindViewHolder(final MyRecyclerViewHolder holder, final int position) {

        if (mOnItemClickListener!=null){
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    mOnItemClickListener.onItemClick(holder.itemView, position);
                }
            });
            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    mOnItemClickListener.onItemLongClick(holder.itemView, position);
                    return true;
                }
            });
        }
   //     holder.mTextView.setText(mDatas.get(position));
        bindDefaultFeedItem(position,holder);
    }

    @Override
    public int getItemCount() {
        return 25;
    }


    public interface  OnItemClickListener{
        void onItemClick(View view, int position);

        void onItemLongClick(View view, int position);
    }
    public  OnItemClickListener mOnItemClickListener;

    public  void setmOnItemClickListener(OnItemClickListener listener){
        this.mOnItemClickListener = listener;
    }


    private  void bindDefaultFeedItem(int position,MyRecyclerViewHolder holder){
        if (position % 2 == 0) {
//            holder.ivFeedCenter.setImageResource(R.mipmap.img_feed_center_1);
            holder.ivFeedBottom.setImageResource(R.mipmap.img_feed_bottom_1);
        } else {
   //         holder.ivFeedCenter.setImageResource(R.mipmap.img_feed_center_2);
            holder.ivFeedBottom.setImageResource(R.mipmap.img_feed_bottom_2);
        }
    }
}
