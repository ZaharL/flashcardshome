package com.learningstarz.myflashcards.ui.activities;

import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.learningstarz.myflashcards.R;
import com.learningstarz.myflashcards.interfaces.DataTransferable;
import com.learningstarz.myflashcards.tools.Tools;
import com.learningstarz.myflashcards.types.Deck;
import com.learningstarz.myflashcards.ui.components.adapters.EditCardsAdapter;
import com.learningstarz.myflashcards.ui.fragments.CardEditFragment;

/**
 * Created by ZahARin on 08.02.2016.
 */
public class CardsActivity extends AppCompatActivity implements DataTransferable {

    private Deck deck;
    TextView cardsCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cards);

        deck = getIntent().getParcelableExtra(Tools.deckExtraTag);

        initToolbar();
        initRecyclerView();
        initTitleBar();
        initFAB();
    }

    private void initFAB() {
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.CardsActivity_fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CardEditFragment cardEditFragment =  CardEditFragment.newInstance();
                android.support.v4.app.FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.add(R.id.CardsActivity_rlContainer, cardEditFragment);
                transaction.addToBackStack("tag");
                transaction.commit();
            }
        });
    }

    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.CardsActivity_toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    private void initRecyclerView() {
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.CardsActivity_rvCards);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(llm);
        recyclerView.setAdapter(new EditCardsAdapter(CardsActivity.this, deck, this));
    }

    private void initTitleBar () {
        ((TextView) findViewById(R.id.CardsActivity_tvDeckTitle)).setText(deck.getTitle());
        ((TextView) findViewById(R.id.CardsActivity_tvDeckAuthor)).setText(deck.getAuthor());
        cardsCount = (TextView) findViewById(R.id.CardsActivity_tvCardsCount);
        cardsCount.setText(deck.getCardsCount() + "");
        findViewById(R.id.CardsActivity_btnEditDeck).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.cards_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onSetIntData(int i) {
        cardsCount.setText(i+"");
    }
}
