package dome.ninebox.com.androidmonkey;


import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.util.LruCache;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;
import com.miguelcatalan.materialsearchview.MaterialSearchView;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import dome.ninebox.com.androidmonkey.adapter.MyViewPagerAdapter;
import dome.ninebox.com.androidmonkey.db.DotaMaxDAO;
import dome.ninebox.com.androidmonkey.fragment.MyFragment;
import dome.ninebox.com.androidmonkey.fragment.SteamIdNullFragment;
import dome.ninebox.com.androidmonkey.model.MatchDetails;
import dome.ninebox.com.androidmonkey.model.User;
import dome.ninebox.com.androidmonkey.service.MatchIntentService;
import dome.ninebox.com.androidmonkey.utils.SnackbarUtil;
import dome.ninebox.com.androidmonkey.utils.Utility;
import me.drakeet.materialdialog.MaterialDialog;

import static android.support.design.widget.TabLayout.MODE_SCROLLABLE;

public class MyActivity extends AppCompatActivity implements ViewPager.OnPageChangeListener {


    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.id_tablayout)
    TabLayout mTabLayout;
    @BindView(R.id.id_appbarlayout)
    AppBarLayout mAppBarLayout;
    @BindView(R.id.id_viewpager)
    ViewPager mViewPager;

    @BindView(R.id.id_coordinatorlayout)
    CoordinatorLayout mCoordinatorLayout;
    @BindView(R.id.id_navigationview)
    NavigationView mNavigationView;
    @BindView(R.id.id_drawerlayout)
    DrawerLayout mDrawerLayout;
    @BindView(R.id.search_view)
    MaterialSearchView searchView;

    //初始化各种控件，照着xml中的顺序写


    // TabLayout中的tab标题
    private String[] mTitles;
    // 填充到ViewPager中的Fragment
    private List<Fragment> mFragments;
    // ViewPager的数据适配器
    private MyViewPagerAdapter mViewPagerAdapter;


    private static List<String> matches;
    private static List<MatchDetails> matchDetailses;

    private RequestQueue mRequestQueue;

    private MaterialDialog mMaterialDialog;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my);
        ButterKnife.bind(this);


        mRequestQueue = Volley.newRequestQueue(getApplicationContext());

        // 初始化mTitles、mFragments等ViewPager需要的数据
        //这里的数据都是模拟出来了，自己手动生成的，在项目中需要从网络获取数据
        initData();

        // 对各种控件进行设置、适配、填充数据
        configViews();


        Intent intent = new Intent(this, MatchIntentService.class);
        intent.putExtra("URL", "https://api.steampowered.com/IDOTA2Match_570/GetMatchHistory/V001/?key=BAA464D3B432D062BEA99BA753214681&matches_requested=4&account_id=125690482");
        intent.putExtra(MatchIntentService.STEAM_ID, 125690482L);
        startService(intent);


        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    private void setMaterialDialog() {
        mMaterialDialog = new MaterialDialog(this);
        View view = LayoutInflater.from(this)
                .inflate(R.layout.loading_drawble,
                        null);
        mMaterialDialog.setCanceledOnTouchOutside(true);
        mMaterialDialog.setView(view).show();
    }

    private void setSucceedMaterialDialog(final User user, RequestQueue mQueue) {
        mMaterialDialog = new MaterialDialog(this);
        View view = LayoutInflater.from(this)
                .inflate(R.layout.serach_info,
                        null);
        NetworkImageView avatarImage = (NetworkImageView) view.findViewById(R.id.avatar_image);
        avatarImage.setDefaultImageResId(R.drawable.hero_no);

        avatarImage.setImageUrl(user.getAvatarmedium(), new ImageLoader(mQueue, new BitmapCache()));

        TextView userName = (TextView) view.findViewById(R.id.user_name);
        userName.setText(user.getName());
        //这里为了显示就不加STEAM64
        TextView steamId = (TextView) view.findViewById(R.id.steam_id);
        steamId.setText(user.getSteam_id()+"");
        Button button = (Button) view.findViewById(R.id.attention);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DotaMaxDAO db = MyApplication.getDb();
                user.setSteam_id(user.getSteam_id()+Utility.STEAM64);
                boolean isInsert =db.insertUser(user);
                if (isInsert==true){
                    SnackbarUtil.show(mViewPager, "添加用户："+user.getName()+"成功", 0);
                }else {
                    SnackbarUtil.show(mViewPager, "添加用户："+user.getName()+"可能已经存在，", 0);
                }
                mMaterialDialog.dismiss();

            }
        });
        mMaterialDialog.setCanceledOnTouchOutside(true);
        mMaterialDialog.setView(view).show();
    }

    private void initData() {

        // Tab的标题采用string-array的方法保存，在res/values/arrays.xml中写
        mTitles = getResources().getStringArray(R.array.tab_titles);

        //初始化填充到ViewPager中的Fragment集合
        mFragments = new ArrayList<>();
        for (int i = 0; i < mTitles.length; i++) {
            Bundle mBundle = new Bundle();
            mBundle.putInt("flag", i);

            if (i == 0) {
                MyFragment mFragment = new MyFragment(this);
                mFragment.setArguments(mBundle);
                mFragments.add(i, mFragment);
            } else {
                SteamIdNullFragment mFragment = new SteamIdNullFragment();
                mFragment.setArguments(mBundle);
                mFragments.add(i, mFragment);
            }
        }

    }

    private void configViews() {

        // 设置显示Toolbar
        setSupportActionBar(mToolbar);

        // 设置Drawerlayout开关指示器，即Toolbar最左边的那个icon
        ActionBarDrawerToggle mActionBarDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, mToolbar, R.string.open, R.string.close);
        mActionBarDrawerToggle.syncState();
        mDrawerLayout.setDrawerListener(mActionBarDrawerToggle);

        //给NavigationView填充顶部区域，也可在xml中使用app:headerLayout="@layout/header_nav"来设置
        mNavigationView.inflateHeaderView(R.layout.header_nav);
        //给NavigationView填充Menu菜单，也可在xml中使用app:menu="@menu/menu_nav"来设置
        mNavigationView.inflateMenu(R.menu.menu_nav);

        // 自己写的方法，设置NavigationView中menu的item被选中后要执行的操作
        onNavgationViewMenuItemSelected(mNavigationView);

        // 初始化ViewPager的适配器，并设置给它
        mViewPagerAdapter = new MyViewPagerAdapter(getSupportFragmentManager(), mTitles, mFragments);
        mViewPager.setAdapter(mViewPagerAdapter);


        // 设置ViewPager最大缓存的页面个数
        mViewPager.setOffscreenPageLimit(5);
        // 给ViewPager添加页面动态监听器（为了让Toolbar中的Title可以变化相应的Tab的标题）
        mViewPager.addOnPageChangeListener(this);


        mTabLayout.setTabMode(MODE_SCROLLABLE);
        // 将TabLayout和ViewPager进行关联，让两者联动起来
        mTabLayout.setupWithViewPager(mViewPager);
        // 设置Tablayout的Tab显示ViewPager的适配器中的getPageTitle函数获取到的标题
        mTabLayout.setTabsFromPagerAdapter(mViewPagerAdapter);


        searchView.setSuggestions(getResources().getStringArray(R.array.query_suggestions));
        searchView.setOnQueryTextListener(new MaterialSearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                try {
                    Long.parseLong(query);
                } catch (Exception e) {
                    SnackbarUtil.show(mViewPager, "不能转化为Long", 0);
                    return false;
                }

                setMaterialDialog();
               /* Intent intent = new Intent(MyActivity.this, MatchIntentService.class);
                intent.putExtra("URL", "https://api.steampowered.com/IDOTA2Match_570/GetMatchHistory/V001/?key=BAA464D3B432D062BEA99BA753214681&matches_requested=4&account_id="+query);
                intent.putExtra(MatchIntentService.STEAM_ID, query);
                startService(intent);

                Intent intent1 = new Intent(MatchIntentService.BROADCAST_ACTION);
                intent1.putExtra(MatchIntentService.STEAM_ID,  query);
                LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(intent1);*/
                User user = new User();
                user.setSteam_id(Long.parseLong(query));
                HttpReadUserInfo(user);

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                //Do some magic
                return false;
            }
        });

        searchView.setOnSearchViewListener(new MaterialSearchView.SearchViewListener() {
            @Override
            public void onSearchViewShown() {
                //Do some magic
                searchView.setVisibility(View.VISIBLE);
            }

            @Override
            public void onSearchViewClosed() {
                //Do some magic
            }
        });


    }


    /**
     * 设置NavigationView中menu的item被选中后要执行的操作
     *
     * @param mNav
     */
    private void onNavgationViewMenuItemSelected(NavigationView mNav) {
        mNav.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {

                String msgString = "";

                switch (menuItem.getItemId()) {
                    case R.id.nav_menu_home:
                        msgString = (String) menuItem.getTitle();
                        break;
                    case R.id.nav_menu_categories:
                        msgString = (String) menuItem.getTitle();
                        break;
                    case R.id.nav_menu_feedback:
                        msgString = (String) menuItem.getTitle();
                        break;
                    case R.id.nav_menu_setting:
                        msgString = (String) menuItem.getTitle();
                        break;
                }

                // Menu item点击后选中，并关闭Drawerlayout
                menuItem.setChecked(true);
                mDrawerLayout.closeDrawers();

                // android-support-design兼容包中新添加的一个类似Toast的控件。
                SnackbarUtil.show(mViewPager, msgString, 0);

                return true;
            }
        });
    }


    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        mToolbar.setTitle(mTitles[position]);
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_main, menu);
        MenuItem item = menu.findItem(R.id.action_search);
        searchView.setMenuItem(item);

        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == MaterialSearchView.REQUEST_VOICE && resultCode == RESULT_OK) {
            ArrayList<String> matches = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            if (matches != null && matches.size() > 0) {
                String searchWrd = matches.get(0);
                if (!TextUtils.isEmpty(searchWrd)) {
                    searchView.setQuery(searchWrd, false);

                }
            }

            return;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onBackPressed() {
        if (searchView.isSearchOpen()) {
            searchView.closeSearch();
        } else {
            super.onBackPressed();
        }
    }


    public void HttpReadUserInfo(final User user) {

        long steam_id = user.getSteam_id() +Utility.STEAM64;
        final RequestQueue mQueue = Volley.newRequestQueue(this);
        String url = "http://api.steampowered.com/ISteamUser/GetPlayerSummaries/" +
                "v0002/?key=BAA464D3B432D062BEA99BA753214681&steamids=" + steam_id;
        JsonObjectRequest jsonRequest = new JsonObjectRequest(url, null,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {

                        System.out.println("请求用户信息成功！response--" + response.toString());
                        Utility.handleUserInfoResponse(user, response);
                       /* DotaMaxDAO db = MyApplication.getDb();
                        db.insertUser(user);*/
                        mMaterialDialog.dismiss();
                        if(user.getName()==null){
                            SnackbarUtil.show(mViewPager, "用户信息读取失败~~", 1);
                        }
                        setSucceedMaterialDialog(user, mQueue);


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
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    public Action getIndexApiAction() {
        Thing object = new Thing.Builder()
                .setName("My Page") // TODO: Define a title for the content shown.
                // TODO: Make sure this auto-generated URL is correct.
                .setUrl(Uri.parse("http://[ENTER-YOUR-URL-HERE]"))
                .build();
        return new Action.Builder(Action.TYPE_VIEW)
                .setObject(object)
                .setActionStatus(Action.STATUS_TYPE_COMPLETED)
                .build();
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        AppIndex.AppIndexApi.start(client, getIndexApiAction());
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        AppIndex.AppIndexApi.end(client, getIndexApiAction());
        client.disconnect();
    }


    public class BitmapCache implements ImageLoader.ImageCache {

        private LruCache<String, Bitmap> mCache;

        public BitmapCache() {
            int maxSize = 10 * 1024 * 1024;
            mCache = new LruCache<String, Bitmap>(maxSize) {
                @Override
                protected int sizeOf(String key, Bitmap bitmap) {
                    return bitmap.getRowBytes() * bitmap.getHeight();
                }
            };
        }

        @Override
        public Bitmap getBitmap(String url) {
            return mCache.get(url);
        }

        @Override
        public void putBitmap(String url, Bitmap bitmap) {
            mCache.put(url, bitmap);
        }

    }
}
