package com.learningstarz.myflashcards.ui.activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.learningstarz.myflashcards.R;
import com.learningstarz.myflashcards.data_storage.DataManager;
import com.learningstarz.myflashcards.interfaces.TaskCompletable;
import com.learningstarz.myflashcards.tools.Tools;
import com.learningstarz.myflashcards.types.Deck;
import com.learningstarz.myflashcards.types.User;
import com.learningstarz.myflashcards.ui.components.NonSwipeableViewPager;
import com.learningstarz.myflashcards.ui.fragments.FragmentMyDeckTab;

import java.util.ArrayList;

/**
 * Created by ZahARin on 14.01.2016.
 */
public class MyDecksActivity extends AppCompatActivity implements TaskCompletable {
    private boolean doubleBackToExitPressedOnce = false;

    private static User user;
    private static String token;

    private DrawerLayout mDrawer;
    Toolbar toolbar;
    ActionBarDrawerToggle drawerToggle;
    NavigationView navView;

    ProgressBar pbGlobal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_deck);

        pbGlobal = (ProgressBar) findViewById(R.id.MyDeckActivity_pbCardsGlobal);

        user = DataManager.getUser();
        token = user.getToken();

        initiateDecks();    // !
        initNavDrawer();    // !  DO NOT REPLACE METHODS
        initToolbar();      // !
    }

    /**
     * Needs to use this after getting USER DATA from DB
     */
    private void initiateDecks() {
        Tools.syncAll(MyDecksActivity.this, token, pbGlobal);
    }

    private void initNavDrawer() {
        mDrawer = (DrawerLayout) findViewById(R.id.MyDeckActivity_drawerLayout);
        drawerToggle = new ActionBarDrawerToggle(
                this, mDrawer, toolbar, R.string.drawer_open, R.string.drawer_close);
        mDrawer.setDrawerListener(drawerToggle);
        navView = (NavigationView) findViewById(R.id.MyDeckActivity_nawDrawer);
        View nawViewHeader = navView.getHeaderView(0);
        ((TextView) nawViewHeader.findViewById(R.id.NavDrawer_tvName)).setText(user.getFullName());

        navView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                selectDrawerItem(item);
                return true;
            }
        });
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Tools.syncAll(MyDecksActivity.this, token, pbGlobal);
    }

    public void selectDrawerItem(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.reports:
                Intent intent = new Intent(MyDecksActivity.this, ReportsActivity.class);
                startActivity(intent);
                break;
            case R.id.logout:
                AlertDialog.Builder builder = new AlertDialog.Builder(MyDecksActivity.this);
                builder.setNegativeButton(R.string.cancel, null);
                builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent logoutIntent = new Intent(MyDecksActivity.this, FirstActivity.class);
                        startActivity(logoutIntent);
                        finish();
                    }
                });
                builder.setTitle(R.string.nav_menu_logout);
                builder.setMessage(R.string.are_you_sure);
                builder.show();
                break;
            case R.id.sync:
                Tools.syncAll(MyDecksActivity.this, token, pbGlobal);
                break;
        }
        item.setCheckable(false);
        mDrawer.closeDrawers();
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
        NonSwipeableViewPager viewPager = (NonSwipeableViewPager) findViewById(R.id.MyDeckActivity_viewPager);
        viewPager.setAdapter(new CardsPagerAdapter(getSupportFragmentManager()));

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

    @Override
    public void onBackgroundTaskCompleted(Object result) {

    }

    @Override
    public void onBackgroundTaskCompleted(ArrayList<Deck> result) {
        initViewPager();
    }

    public class CardsPagerAdapter extends FragmentPagerAdapter {
        public static final int PAGE_COUNT = 3;

        String author = getString(R.string.author);
        private String[] tabTitles = new String[]{getString(R.string.date), getString(R.string.name), author.substring(0, author.length() - 1)};

        public CardsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return FragmentMyDeckTab.newInstance(position + 1, token);
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
        Toast.makeText(MyDecksActivity.this, getString(R.string.please_click_back_again), Toast.LENGTH_SHORT).show();

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
        MenuInflater inf = getMenuInflater();
        inf.inflate(R.menu.my_deck_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (drawerToggle.onOptionsItemSelected(item)) return true;
        switch (item.getItemId()) {
            case R.id.home:
                mDrawer.openDrawer(GravityCompat.START);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
