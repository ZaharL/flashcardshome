package com.learningstarz.myflashcards.ui.activities;

import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;

import com.learningstarz.myflashcards.R;
import com.learningstarz.myflashcards.data_storage.DataManager;
import com.learningstarz.myflashcards.tools.Tools;
import com.learningstarz.myflashcards.types.Deck;
import com.learningstarz.myflashcards.ui.components.DepthPageTransformer;
import com.learningstarz.myflashcards.ui.components.adapters.StudyCardsAdapter;

/**
 * Created by ZahARin on 02.06.2016.
 */
public class StudyActivity extends AppCompatActivity {

    Deck deck;
    TextView tvCardHours;
    TextView tvCardMinutes;
    TextView tvCardSeconds;
    TextView tvDeckHours;
    TextView tvDeckMinutes;
    TextView tvDeckSeconds;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_study);

        deck = getIntent().getParcelableExtra(Tools.deckExtraTag);

        initToolbar();
        initViewPager();
        initComponets();
    }

    private void initComponets() {
        Button btnFlip = (Button) findViewById(R.id.StudyActivity_btnFlip);

        tvCardSeconds = (TextView) findViewById(R.id.StudyActivity_etCardSeconds);
        tvCardMinutes = (TextView) findViewById(R.id.StudyActivity_etCardMinutes);
        tvCardHours = (TextView) findViewById(R.id.StudyActivity_etCardHours);
        tvDeckSeconds = (TextView) findViewById(R.id.StudyActivity_etDeckSeconds);
        tvDeckMinutes = (TextView) findViewById(R.id.StudyActivity_etDeckMinutes);
        tvDeckHours = (TextView) findViewById(R.id.StudyActivity_etDeckHours);

        ((TextView) findViewById(R.id.StudyActivity_tvTotalCardNum)).setText(String.valueOf(deck.getCardsCount()));
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        //TODO create time record in deck to db
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
