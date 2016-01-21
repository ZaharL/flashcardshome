package com.learningstarz.myflashcards.ui.activities;

import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewParent;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.learningstarz.myflashcards.R;
import com.learningstarz.myflashcards.Tools.Tools;
import com.learningstarz.myflashcards.Types.User;
import com.learningstarz.myflashcards.ui.fragments.FragmentMyDeckTab;

/**
 * Created by ZahARin on 14.01.2016.
 */
public class MyDeckActivity extends AppCompatActivity {
    private boolean doubleBackToExitPressedOnce = false;

    User user;

    private DrawerLayout mDrawer;
    Toolbar toolbar;
    ActionBarDrawerToggle drawerToggle;
    NavigationView navView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_deck);

        user = getIntent().getParcelableExtra(Tools.firstActivity_userExtraTag);

        initNavDrawer();
        initViewPager();
        initToolbar();
    }

    private void initNavDrawer() {
        mDrawer = (DrawerLayout) findViewById(R.id.MyDeckActivity_drawerLayout);
        drawerToggle = new ActionBarDrawerToggle(
                this, mDrawer, toolbar, R.string.drawer_open, R.string.drawer_close);
        mDrawer.setDrawerListener(drawerToggle);
        navView = (NavigationView) findViewById(R.id.MyDeckActivity_nawDrawer);
        View nawViewHeader = navView.getHeaderView(0);
        ((TextView) nawViewHeader.findViewById(R.id.NavDrawer_tvName)).setText(user.getFullName());

    }

    private void initToolbar() {
        toolbar = (Toolbar) findViewById(R.id.MyDeckActivity_toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            drawerToggle.syncState();
        }
    }

    private void initViewPager() {
        ViewPager viewPager = (ViewPager) findViewById(R.id.MyDeckActivity_viewPager);
        viewPager.setAdapter(new CardsPagerAdapter(getSupportFragmentManager(), MyDeckActivity.this));

        TabLayout tabLayout = (TabLayout) findViewById(R.id.MyDeckActivity_tabLayout);
        tabLayout.setupWithViewPager(viewPager);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        drawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        drawerToggle.onConfigurationChanged(newConfig);
    }

    public class CardsPagerAdapter extends FragmentPagerAdapter {
        public static final int PAGE_COUNT = 3;

        private String[] tabTitles = new String[] {"Date", "Name", "Author"};
        private Context context;

        public CardsPagerAdapter(FragmentManager fm, Context context) {
            super(fm);
            this.context = context;
        }

        @Override
        public Fragment getItem(int position) {
            return FragmentMyDeckTab.newInstance(position+1);
        }

        @Override
        public int getCount() {
            return PAGE_COUNT;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return tabTitles[position];
        }
    }

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }

        doubleBackToExitPressedOnce = true;
        Toast.makeText(MyDeckActivity.this, getString(R.string.please_click_back_again), Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                doubleBackToExitPressedOnce = false;
            }
        }, 2000);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_MENU:
                if (mDrawer.isDrawerOpen(navView)) {
                    mDrawer.closeDrawer(GravityCompat.START);
                } else {
                    mDrawer.openDrawer(GravityCompat.START);
                }
                return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inf= getMenuInflater();
        inf.inflate(R.menu.my_deck_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (drawerToggle.onOptionsItemSelected(item)) return true;
        switch(item.getItemId()) {
            case R.id.home:
                mDrawer.openDrawer(GravityCompat.START);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
