package dome.ninebox.com.androidmonkey;


import android.annotation.TargetApi;


import android.content.BroadcastReceiver;
import android.content.Context;

import android.content.Intent;
import android.content.IntentFilter;


import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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

import butterknife.BindView;
import butterknife.ButterKnife;
import dome.ninebox.com.androidmonkey.adapter.MyRecyclerViewAdapter;
import dome.ninebox.com.androidmonkey.adapter.MyStaggeredViewAdapter;
import dome.ninebox.com.androidmonkey.db.DotaMaxDAO;
import dome.ninebox.com.androidmonkey.model.Heroes;
import dome.ninebox.com.androidmonkey.model.MatchDetails;
import dome.ninebox.com.androidmonkey.provider.MatchDetailsProvider;
import dome.ninebox.com.androidmonkey.service.MatchIntentService;
import dome.ninebox.com.androidmonkey.utils.SnackbarUtil;
import dome.ninebox.com.androidmonkey.utils.Utility;
import dome.ninebox.com.androidmonkey.widget.MessageResponse;

/**
 * Created by Administrator on 2016/4/25.
 */
public class MyFragment extends Fragment implements MyRecyclerViewAdapter.OnItemClickListener, MyStaggeredViewAdapter.OnItemClickListener, SwipeRefreshLayout.OnRefreshListener, MessageResponse {


    @BindView(R.id.id_recyclerview)
    RecyclerView mRecyclerView;
    @BindView(R.id.id_swiperefreshlayout)
    SwipeRefreshLayout mSwipeRefreshLayout;


    private View mView;
    private RecyclerView.LayoutManager mLayoutManager;
    static private MyRecyclerViewAdapter mRecyclerViewAdapter;
    private MyStaggeredViewAdapter mStaggeredAdapter;

    private static final int VERTICAL_LIST = 0;
    private static final int HORIZONTAL_LIST = 1;
    private static final int VERTICAL_GRID = 2;
    private static final int HORIZONTAL_GRID = 3;
    private static final int STAGGERED_GRID = 4;

    private static final int SPAN_COUNT = 2;
    private int flag = 0;

    private MyBroadcast broadcast = new MyBroadcast();

    /**
     * 记录所有正在下载或等待下载的任务。
     */
    private Set<MatchInfoTask> taskCollection = new HashSet<>();


    private List<MatchDetails> mUserDetails = new ArrayList<>();


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        mView = inflater.inflate(R.layout.frag_main, container, false);
        ButterKnife.bind(this, mView);


        return mView;


    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mSwipeRefreshLayout = (SwipeRefreshLayout) mView.findViewById(R.id.id_swiperefreshlayout);

        mRecyclerView = (RecyclerView) mView.findViewById(R.id.id_recyclerview);

        flag = (int) getArguments().get("flag");
        configRecyclerView();
        initLoader();

        // 刷新时，指示器旋转后变化的颜色
        mSwipeRefreshLayout.setColorSchemeResources(R.color.main_blue_light, R.color.main_blue_dark);
        mSwipeRefreshLayout.setOnRefreshListener(this);


    }


    private void configRecyclerView() {
        switch (flag) {
            case VERTICAL_LIST:
                mLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
                //注册广播接收器
                IntentFilter filter = new IntentFilter();
                filter.addAction(MatchIntentService.BROADCAST_ACTION);
                LocalBroadcastManager.getInstance(getActivity()).registerReceiver(broadcast, filter);
                break;
            case HORIZONTAL_LIST:
                mLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
                break;
            case VERTICAL_GRID:
                mLayoutManager = new GridLayoutManager(getActivity(), SPAN_COUNT, GridLayoutManager.VERTICAL, false);
                break;
            case HORIZONTAL_GRID:
                mLayoutManager = new GridLayoutManager(getActivity(), SPAN_COUNT, GridLayoutManager.HORIZONTAL, false);
                break;
            case STAGGERED_GRID:
                mLayoutManager = new StaggeredGridLayoutManager(SPAN_COUNT, StaggeredGridLayoutManager.VERTICAL);
                break;
        }

        if (flag != STAGGERED_GRID) {
            if (mRecyclerViewAdapter == null) {

                Cursor c = getActivity().getContentResolver().query(MatchDetailsProvider.URI_DOTA_ALL, null, null, null, null);
                mRecyclerViewAdapter = new MyRecyclerViewAdapter(getActivity(), c, 1);
            }

            mRecyclerViewAdapter.setmOnItemClickListener(this);
            mRecyclerView.setAdapter(mRecyclerViewAdapter);
        } else {
            mStaggeredAdapter = new MyStaggeredViewAdapter(getActivity());
            mStaggeredAdapter.setOnItemClickListener(this);
            mRecyclerView.setAdapter(mStaggeredAdapter);
        }
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
    }

    @Override
    public void onItemClick(View view, int position) {
        SnackbarUtil.show(mRecyclerView, getString(R.string.item_clicked), 0);
    }

    @Override
    public void onItemLongClick(View view, int position) {
        SnackbarUtil.show(mRecyclerView, getString(R.string.item_longclicked), 1);
    }

    @Override
    public void onRefresh() {

        // 刷新时模拟数据的变化
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mSwipeRefreshLayout.setRefreshing(false);
                int temp = (int) (Math.random() * 10);
                if (flag != STAGGERED_GRID) {
                    //  mRecyclerViewAdapter.mDatas.add(0, "new" + temp);
                    mRecyclerViewAdapter.notifyDataSetChanged();
                } else {
                    //    mStaggeredAdapter.mDatas.add(0, "new" + temp);
                    mStaggeredAdapter.mHeights.add(0, (int) (Math.random() * 300) + 200);
                    mStaggeredAdapter.notifyDataSetChanged();
                }
            }
        }, 1000);
    }

    @Override
    public void onReceivedSuccess(List<MatchDetails> msg) {

        final Map<MatchDetails, Heroes> listHeroes = new HashMap<>();

        for (MatchDetails details : msg) {
            if (details.getAccount_id() == 125690482) {
                mUserDetails.add(details);
                HttpReadUserInfo(details);

            }

        }

        //  mRecyclerViewAdapter.notifyDataSetChanged();
        //  cancelAllTasks();
    }

    public void HttpReadUserInfo(final MatchDetails md) {
        RequestQueue mQueue = Volley.newRequestQueue(getActivity());
        String url = "http://api.steampowered.com/ISteamUser/GetPlayerSummaries/" +
                "v0002/?key=BAA464D3B432D062BEA99BA753214681&steamids=" + md.getSteam_id();
        JsonObjectRequest jsonRequest = new JsonObjectRequest(url, null,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {

                        System.out.println("请求用户信息成功！response--" + response.toString());
                        Utility.handleUserInfoResponse(md, response);

                        DotaMaxDAO db =MyApplication.getDb();
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

    private void initLoader() {

        getLoaderManager().initLoader(1, null, new LoaderManager.LoaderCallbacks<Cursor>() {
            @Override
            public Loader<Cursor> onCreateLoader(int id, Bundle args) {

                CursorLoader loader = new CursorLoader(getActivity(), MatchDetailsProvider.URI_DOTA_ALL, null, null, null, null);

                return loader;
            }

            @Override
            public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
                mRecyclerViewAdapter.swapCursor(data);
            }

            @Override
            public void onLoaderReset(Loader<Cursor> loader) {
                mRecyclerViewAdapter.swapCursor(null);
            }
        });

    }


    class MyBroadcast extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            List<String> ids = new ArrayList<>();
            ids = intent.getStringArrayListExtra(MatchIntentService.EXTENDED_DATA);
            Log.d("TAG", "++++你好++++");
            DotaMaxDAO dao = MyApplication.getDb();

            for (int i = 0; i < ids.size(); i++) {

                try {
                    long intNum = Long.parseLong(ids.get(i).trim());
                    Log.d("TAG", "intNum: " + intNum);
                    if (dao.getMatch(intNum) == null) {
                        MatchInfoTask task = new MatchInfoTask();
                        task.setResponse(MyFragment.this);
                        taskCollection.add(task);
                        task.execute(ids.get(i));
                    }
                } catch (Exception e) {
                    e.getMessage();
                }


            }

        }

    }


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


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mRecyclerViewAdapter.fluchCache();
    }

    @Override
    public void onStop() {
        super.onStop();
        // 退出程序时结束所有的下载任务
        mRecyclerViewAdapter.cancelAllTasks();
    }


}
