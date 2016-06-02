package com.learningstarz.myflashcards.ui.activities;

import android.content.Intent;
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
import com.learningstarz.myflashcards.data_storage.DataManager;
import com.learningstarz.myflashcards.interfaces.DataTransferable;
import com.learningstarz.myflashcards.interfaces.TaskCompletable;
import com.learningstarz.myflashcards.tools.Tools;
import com.learningstarz.myflashcards.types.Card;
import com.learningstarz.myflashcards.types.Deck;
import com.learningstarz.myflashcards.ui.components.adapters.EditCardsAdapter;

import java.util.ArrayList;

/**
 * Created by ZahARin on 08.02.2016.
 */
public class CardsActivity extends AppCompatActivity implements DataTransferable, TaskCompletable {

    private static Deck deck;
    TextView cardsCount;
    private String token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cards);

        deck = getIntent().getParcelableExtra(Tools.deckExtraTag);
        token = DataManager.getUserToken();

        if (deck.getCardsCount() < 1) {
            Tools.syncDeck(CardsActivity.this, token, deck.getUid());
            Intent editCard = new Intent(CardsActivity.this, EditCardsActivity.class);
            editCard.putExtra(Tools.deckExtraTag, deck);
            startActivityForResult(editCard, Tools.editCardActivityResult);
        }

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
                Intent editCard = new Intent(CardsActivity.this, EditCardsActivity.class);
                editCard.putExtra(Tools.deckExtraTag, deck);
                startActivityForResult(editCard, Tools.editCardActivityResult);
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
                Intent intent = new Intent(CardsActivity.this, EditDeckActivity.class);
                intent.putExtra(Tools.deckExtraTag, deck);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Tools.editCardActivityResult) {
            Tools.syncDeck(CardsActivity.this, token, deck.getUid());
        }
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

    @Override
    public void onBackgroundTaskCompleted(Object result) {
        deck = (Deck) result;
        initRecyclerView();
        initTitleBar();
    }

    @Override
    public void onBackgroundTaskCompleted(ArrayList<Deck> result) {

    }
}
