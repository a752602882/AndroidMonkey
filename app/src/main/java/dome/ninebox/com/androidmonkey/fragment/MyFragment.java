

/**
 * Created by Administrator on 2016/11/4.
 */

package dome.ninebox.com.androidmonkey.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
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
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import dome.ninebox.com.androidmonkey.MyApplication;
import dome.ninebox.com.androidmonkey.R;
import dome.ninebox.com.androidmonkey.adapter.MyRecyclerViewAdapter;
import dome.ninebox.com.androidmonkey.db.DotaMaxDAO;
import dome.ninebox.com.androidmonkey.provider.MatchDetailsProvider;
import dome.ninebox.com.androidmonkey.service.MatchIntentService;
import dome.ninebox.com.androidmonkey.utils.SnackbarUtil;
import dome.ninebox.com.androidmonkey.utils.Utility;


/**
 * Created by Administrator on 2016/4/25.
 */
public class MyFragment extends Fragment implements MyRecyclerViewAdapter.OnItemClickListener, SwipeRefreshLayout.OnRefreshListener {


    @BindView(R.id.id_recyclerview)
    RecyclerView mRecyclerView;
    @BindView(R.id.id_swiperefreshlayout)
    SwipeRefreshLayout mSwipeRefreshLayout;


    private View mView;
    private RecyclerView.LayoutManager mLayoutManager;
    static private MyRecyclerViewAdapter mRecyclerViewAdapter;

    private static final int STAGGERED_GRID = 4;
    private int flag = 0;

    private MyBroadcast broadcast ;
    private Context mContext;

    public MyFragment(Context mContext) {
        broadcast = new MyBroadcast(mContext);
        this.mContext =mContext;
    }

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


        mRecyclerView = (RecyclerView) mView.findViewById(R.id.id_recyclerview);
        flag = (int) getArguments().get("flag");

        setRegisterBroadcastManager();
        setRecyclerViewAdapter();
        setSwipeRefreshLayout();
        initLoader();


    }

    private void setSwipeRefreshLayout() {
        // 刷新时，指示器旋转后变化的颜色
        mSwipeRefreshLayout = (SwipeRefreshLayout) mView.findViewById(R.id.id_swiperefreshlayout);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.main_blue_light, R.color.main_blue_dark);
        mSwipeRefreshLayout.setOnRefreshListener(this);
    }

    /**
     * 设置recycler adatper数据
     */
    private void setRecyclerViewAdapter() {
        if (mRecyclerViewAdapter == null) {
            String sortBy = "start_time DESC ";
            Cursor c = getActivity().getContentResolver().query(MatchDetailsProvider.URI_DOTA_ALL, null, null, null, sortBy);
            mRecyclerViewAdapter = new MyRecyclerViewAdapter(getActivity(), c, 1);
        }
        mLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        mRecyclerViewAdapter.setmOnItemClickListener(this);
        mRecyclerView.setAdapter(mRecyclerViewAdapter);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
    }

    /**
     * 注册广播
     */
    private void setRegisterBroadcastManager() {

        //注册广播接收器
        IntentFilter filter = new IntentFilter();
        filter.addAction(MatchIntentService.BROADCAST_ACTION);
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(broadcast, filter);
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

                if (flag != STAGGERED_GRID) {
                    //  mRecyclerViewAdapter.mDatas.add(0, "new" + temp);




                   /* Intent intent = new Intent(mContext, MatchIntentService.class);
                    intent.putExtra("URL", "https://api.steampowered.com/IDOTA2Match_570/GetMatchHistory/V001/?key=BAA464D3B432D062BEA99BA753214681&matches_requested=6&account_id=" + user.getSteam_id());
                    intent.putExtra("STEAM_ID", user.getSteam_id());
                    mContext.startService(intent);*/


                 /*   Cursor c = getActivity().getContentResolver().query(MatchDetailsProvider.URI_DOTA_ALL, null, null, null,null);
                    while (c.moveToNext()){
                        Intent intent = new Intent(mContext, MatchIntentService.class);
                        intent.putExtra("URL", "https://api.steampowered.com/IDOTA2Match_570/GetMatchHistory/V001/?key=BAA464D3B432D062BEA99BA753214681&matches_requested=6&account_id=" + user.getSteam_id());
                        intent.putExtra("STEAM_ID", user.getSteam_id());
                        mContext.startService(intent);
                    }*/
                 DotaMaxDAO dao = MyApplication.getDb();
                 List<Long>  steam_id_all =  dao.getUser();

                 if (steam_id_all==null){
                     return;
                 }
                 for(long steam_id: steam_id_all){

                     long account_id =steam_id- Utility.STEAM64;
                     Intent intent = new Intent(mContext, MatchIntentService.class);
                     intent.putExtra("URL", "https://api.steampowered.com/IDOTA2Match_570/GetMatchHistory/V001/?key=BAA464D3B432D062BEA99BA753214681&matches_requested=6&account_id=" + account_id);
                     intent.putExtra("STEAM_ID", account_id);
                     mContext.startService(intent);

                 }

                //    mRecyclerViewAdapter.notifyDataSetChanged();
                    getActivity().getContentResolver().notifyChange(MatchDetailsProvider.URI_DOTA_ALL,null);

                }
            }
        }, 1000);
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