package com.learningstarz.myflashcards.ui.activities;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.AsyncTask;
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
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.learningstarz.myflashcards.R;
import com.learningstarz.myflashcards.Tools.Tools;
import com.learningstarz.myflashcards.Types.Card;
import com.learningstarz.myflashcards.Types.Deck;
import com.learningstarz.myflashcards.Types.User;
import com.learningstarz.myflashcards.ui.fragments.FragmentMyDeckTab;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Formatter;

/**
 * Created by ZahARin on 14.01.2016.
 */
public class MyDecksActivity extends AppCompatActivity {
    private boolean doubleBackToExitPressedOnce = false;

    private User user;
    private ArrayList<Deck> myDecks;

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

        user = getIntent().getParcelableExtra(Tools.firstActivity_userExtraTag);

        initiateCards();    // !
        initNavDrawer();    // !  DO NOT REPLACE METHODS
        initToolbar();      // !
    }

    /**
     * Needs to use this after getting extras with user data
     */
    private void initiateCards() {
        Formatter f = new Formatter();
        f.format(getString(R.string.url_get_my_decks), user.getToken());
        new SyncUserCards().execute(f.toString());
        f.close();
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

    public void selectDrawerItem(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.reports:
                Intent intent = new Intent(MyDecksActivity.this, ReportsActivity.class);
                intent.putExtra(Tools.firstActivity_userExtraTag, user);
                startActivity(intent);
                break;
            case R.id.logout:
                AlertDialog.Builder builder = new AlertDialog.Builder(MyDecksActivity.this);
                builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
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
        ViewPager viewPager = (ViewPager) findViewById(R.id.MyDeckActivity_viewPager);
        viewPager.setAdapter(new CardsPagerAdapter(getSupportFragmentManager(), MyDecksActivity.this));

        TabLayout tabLayout = (TabLayout) findViewById(R.id.MyDeckActivity_tabLayout);
        tabLayout.setupWithViewPager(viewPager);
    }

    public void showGlobalPB() {
        pbGlobal.setVisibility(View.VISIBLE);
    }

    public void hideGlobalPB() {
        pbGlobal.setVisibility(View.GONE);
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

        private String[] tabTitles = new String[]{getString(R.string.date), getString(R.string.name), getString(R.string.author)};
        private Context context;

        public CardsPagerAdapter(FragmentManager fm, Context context) {
            super(fm);
            this.context = context;
        }

        @Override
        public Fragment getItem(int position) {
            return FragmentMyDeckTab.newInstance(position + 1, Tools.sortDecks(myDecks, position + 1));
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

    private class SyncUserCards extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            myDecks = new ArrayList<>();
            showGlobalPB();
        }

        @Override
        protected String doInBackground(String... params) {
            String result = "";

            try {
                URL url = new URL(params[0]);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("GET");
                httpURLConnection.connect();

                InputStream is = httpURLConnection.getInputStream();
                BufferedReader br = new BufferedReader(new InputStreamReader(is));

                String line = "";
                StringBuilder sb = new StringBuilder();

                while ((line = br.readLine()) != null) {
                    sb.append(line);
                }

                result = sb.toString();

                br.close();
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return result;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            try {
                JSONObject firstData = new JSONObject(s);
                JSONArray arrayOfData = firstData.getJSONObject(Tools.jsonResult).getJSONArray(Tools.jsonData);
                for (int i = 0; i < arrayOfData.length(); i++) {
                    JSONObject data = arrayOfData.getJSONObject(i);

                    JSONArray cards = data.getJSONArray(Tools.jsonCards);
                    ArrayList<Card> cardsArray = new ArrayList<>(cards.length());
                    for (int j = 0; j < cards.length(); j++) {
                        JSONObject card = cards.getJSONObject(j);
                        cardsArray.add(new Card(
                                card.getInt("id"),
                                card.getString("uid"),
                                card.getInt("lastDateUpdated"),
                                card.getString("deckUId"),
                                card.getString("question"),
                                card.getString("answer"),
                                card.getString("image"),
                                card.getString("image2"),
                                card.getString("imagepath"),
                                card.getInt("cardTime"),
                                card.getInt("knowStatus")
                        ));
                    }
                    myDecks.add(new Deck(
                            data.getInt("id"),
                            data.getString("uid"),
                            data.getString("title"),
                            data.getString("author"),
                            cardsArray,
                            data.getInt("cardsCount"),
                            data.getInt("dateCreated"),
                            data.getInt("lastDateUpdated"),
                            data.getInt("progress"),
                            data.getInt("deckType"),
                            data.getInt("owner"),
                            data.getInt("deckTime"),
                            data.getString("keywords"),
                            data.getString("description")
                    ));
                }
                initViewPager();
                hideGlobalPB();
            } catch (JSONException e) {
                e.printStackTrace();
            }
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
