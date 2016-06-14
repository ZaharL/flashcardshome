package com.learningstarz.myflashcards.ui.activities;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.learningstarz.myflashcards.R;
import com.learningstarz.myflashcards.tools.Tools;
import com.learningstarz.myflashcards.types.Deck;
import com.learningstarz.myflashcards.ui.components.DepthPageTransformer;
import com.learningstarz.myflashcards.ui.components.adapters.StudyCardsAdapter;
import com.learningstarz.myflashcards.ui.fragments.StudyCardBackFragment;

/**
 * Created by ZahARin on 02.06.2016.
 */
public class StudyActivity extends AppCompatActivity {

    Deck deck;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_study);

        deck = getIntent().getParcelableExtra(Tools.deckExtraTag);

        initToolbar();
        initViewPager();
        initButtons();
    }

    private void initButtons() {
        Button btnFlip = (Button) findViewById(R.id.StudyActivity_btnFlip);

        ((TextView) findViewById(R.id.StudyActivity_tvTotalCardNum)).setText(String.valueOf(deck.getCardsCount()));
    }

    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.StudyActivity_toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    private void initViewPager() {
        ViewPager vp = (ViewPager) findViewById(R.id.StudyActivity_viewPager);
        StudyCardsAdapter adapter = new StudyCardsAdapter(getSupportFragmentManager(), deck);
        vp.setPageTransformer(true, new DepthPageTransformer());
        vp.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.empty_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }


}
