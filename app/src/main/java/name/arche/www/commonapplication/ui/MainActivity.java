package name.arche.www.commonapplication.ui;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.ShareActionProvider;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Toast;


import java.util.ArrayList;
import java.util.List;

import name.arche.www.commonapplication.R;

public class MainActivity extends BaseActivity {

    private DrawerLayout drawerLayout;
    private Toolbar toolbar;
    private TabLayout tabLayout;

    private NavigationView navigationView;
    private ViewPager viewPager;

    private ActionBarDrawerToggle actionBarDrawerToggle;

    private ShareActionProvider shareActionProvider;

    private String titles[] = {"主页","热门","最新"};

    private List<Fragment> contentViews = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void setContentView() {
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        HomeFragment homeFragment = new HomeFragment();
        HotsFragment hotsFragment = new HotsFragment();
        LatestFragment latestFragment = new LatestFragment();

        contentViews.add(homeFragment);
        contentViews.add(hotsFragment);
        contentViews.add(latestFragment);
    }

    @Override
    protected void initViews() {

        /*=======================find views=============================*/
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        tabLayout = (TabLayout) findViewById(R.id.tablayout);

        navigationView = (NavigationView) findViewById(R.id.navigation_view);
        viewPager = (ViewPager) findViewById(R.id.view_pager);
        /*==============================================================*/

        /*=========================set views=============================*/
        toolbar.setTitle("My World");
//        toolbar.setTitleTextColor(getResources().getColor(R.color.secondary_text));
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        navigationView.setNavigationItemSelectedListener(navigationItemSelectedListener);

        actionBarDrawerToggle = new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.drawer_open,R.string.drawer_close);
        actionBarDrawerToggle.syncState();
        drawerLayout.setDrawerListener(actionBarDrawerToggle);

        viewPager.setAdapter(new MyPagerAdapter(getSupportFragmentManager()));

        tabLayout.addTab(tabLayout.newTab().setText(titles[0]));
        tabLayout.addTab(tabLayout.newTab().setText(titles[1]));
        tabLayout.addTab(tabLayout.newTab().setText(titles[2]));

        tabLayout.setupWithViewPager(viewPager);
        /*===============================================================*/

        if (Build.VERSION.SDK_INT >= 21){
            Window window = getWindow();
            window.setStatusBarColor(getResources().getColor(R.color.primary_dark));
            window.setNavigationBarColor(getResources().getColor(R.color.primary));
        }

    }

    private NavigationView.OnNavigationItemSelectedListener navigationItemSelectedListener = new NavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(MenuItem menuItem) {
            switch (menuItem.getItemId()){
                case R.id.menu_home:
                    Toast.makeText(mContext,"navigation_home",Toast.LENGTH_SHORT).show();
                    break;
                case R.id.menu_about:
                    Toast.makeText(mContext,"navigation_about",Toast.LENGTH_SHORT).show();
                    break;
                case R.id.menu_settings:
                    Toast.makeText(mContext,"navigation_settings",Toast.LENGTH_SHORT).show();
                    break;
            }
            menuItem.setChecked(true);
            drawerLayout.closeDrawer(navigationView);
            return false;
        }
    };
    protected void handleData() {

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);

        shareActionProvider = (ShareActionProvider) MenuItemCompat.getActionProvider(menu.findItem(R.id.action_share));
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/*");
        shareActionProvider.setShareIntent(intent);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_about:
                Toast.makeText(mContext,"About",Toast.LENGTH_SHORT).show();
                break;
            case R.id.action_settings:
                Toast.makeText(mContext,"Settings",Toast.LENGTH_SHORT).show();
                break;
//            case android.R.id.home:
//                Toast.makeText(mContext,"LeftButton",Toast.LENGTH_SHORT).show();
//                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {

    }

    public class MyPagerAdapter extends FragmentPagerAdapter{

        public MyPagerAdapter(FragmentManager fm) {
            super(fm);
        }


        @Override
        public CharSequence getPageTitle(int position) {
            return titles[position];
        }

        @Override
        public Fragment getItem(int position) {
            return contentViews.get(position);
        }

        @Override
        public int getCount() {
            return titles.length;
        }
    }
}
