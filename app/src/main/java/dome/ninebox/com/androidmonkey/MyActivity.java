package dome.ninebox.com.androidmonkey;

import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import dome.ninebox.com.androidmonkey.adapter.MyViewPagerAdapter;
import dome.ninebox.com.androidmonkey.model.MatchDetails;
import dome.ninebox.com.androidmonkey.service.MatchIntentService;
import dome.ninebox.com.androidmonkey.utils.SnackbarUtil;

import static android.support.design.widget.TabLayout.MODE_SCROLLABLE;

public class MyActivity extends AppCompatActivity implements View.OnClickListener, ViewPager.OnPageChangeListener {


    @BindView(R.id.id_toolbar)
    Toolbar mToolbar;
    @BindView(R.id.id_tablayout)
    TabLayout mTabLayout;
    @BindView(R.id.id_appbarlayout)
    AppBarLayout mAppBarLayout;
    @BindView(R.id.id_viewpager)
    ViewPager mViewPager;
    @BindView(R.id.id_floatingactionbutton)
    FloatingActionButton mFloatingActionButton;
    @BindView(R.id.id_coordinatorlayout)
    CoordinatorLayout mCoordinatorLayout;
    @BindView(R.id.id_navigationview)
    NavigationView mNavigationView;
    @BindView(R.id.id_drawerlayout)
    DrawerLayout mDrawerLayout;
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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my);
        ButterKnife.bind(this);


        mRequestQueue = Volley.newRequestQueue(getApplicationContext());


        //从服务器读取最新的25场比赛ID
        // HttpReadMatches();

        //从服务器读取1场比赛的详细信息
        //  HttpReadMatchDetails();

        // 初始化mTitles、mFragments等ViewPager需要的数据
        //这里的数据都是模拟出来了，自己手动生成的，在项目中需要从网络获取数据
        initData();

        // 对各种控件进行设置、适配、填充数据
        configViews();

       Intent intent = new Intent(this,MatchIntentService.class);
       intent.putExtra("URL","https://api.steampowered.com/IDOTA2Match_570/GetMatchHistory/V001/?key=BAA464D3B432D062BEA99BA753214681&matches_requested=2&account_id=125690482");
       startService(intent);


    }

    private void initData() {

        // Tab的标题采用string-array的方法保存，在res/values/arrays.xml中写
        mTitles = getResources().getStringArray(R.array.tab_titles);

        //初始化填充到ViewPager中的Fragment集合
        mFragments = new ArrayList<>();
        for (int i = 0; i < mTitles.length; i++) {
            Bundle mBundle = new Bundle();
            mBundle.putInt("flag", i);
            MyFragment mFragment = new MyFragment();

            mFragment.setArguments(mBundle);
            mFragments.add(i, mFragment);
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

        // 设置FloatingActionButton的点击事件
        mFloatingActionButton.setOnClickListener(this);


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


    @OnClick({R.id.id_tablayout, R.id.id_floatingactionbutton})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.id_tablayout:
                break;
            case R.id.id_floatingactionbutton:
                break;
        }
    }




}
