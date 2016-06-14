package com.learningstarz.myflashcards.ui.activities;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;

import com.learningstarz.myflashcards.R;
import com.learningstarz.myflashcards.data_storage.DataManager;
import com.learningstarz.myflashcards.interfaces.TaskCompletable;
import com.learningstarz.myflashcards.tools.Tools;
import com.learningstarz.myflashcards.types.Deck;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Formatter;

/**
 * Created by ZahARin on 30.05.2016.
 */
public class EditDeckActivity extends AppCompatActivity implements TaskCompletable {

    EditText etTitle;
    EditText etDescription;
    EditText etKeyWords;
    Button btnSave;
    Switch swtcAccess;

    private static String token;
    private static Deck deckToEdit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_deck);

        deckToEdit = getIntent().getParcelableExtra(Tools.deckExtraTag);

        initViews();

        token = DataManager.getUserToken();
        if (deckToEdit != null) {
            fillWithInfo();
            initToolbar(false);
        } else {
            initToolbar(true);
        }
    }

    private void fillWithInfo() {
        etTitle.setText(deckToEdit.getTitle());
        etDescription.setText(deckToEdit.getDescription());
        etKeyWords.setText(deckToEdit.getKeyWords());
        swtcAccess.setChecked(deckToEdit.isDeckPrivate());
    }

    private void initToolbar(boolean newDeck) {
        Toolbar toolbar = (Toolbar) findViewById(R.id.EditDeckActivity_toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            if (newDeck) {
                getSupportActionBar().setTitle(R.string.create_deck_title);
            } else {
                getSupportActionBar().setTitle(R.string.edit_deck_title);
            }
        }
    }

    private void initViews() {
        etTitle = (EditText) findViewById(R.id.EditDeckActivity_etTitle);
        etDescription = (EditText) findViewById(R.id.EditDeckActivity_etDescription);
        etKeyWords = (EditText) findViewById(R.id.EditDeckActivity_etKeyWords);
        btnSave = (Button) findViewById(R.id.EditDeckActivity_btnSave);
        btnSave.setOnClickListener(ocl);
        swtcAccess = (Switch) findViewById(R.id.EditDeckActivity_switch);
    }

    View.OnClickListener ocl = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            AlertDialog.Builder alDialog = new AlertDialog.Builder(EditDeckActivity.this);
            alDialog.setPositiveButton(R.string.ok, null);
            alDialog.setTitle(R.string.form_validation);
            if (etTitle.getText().toString().equals("")) {
                alDialog.setMessage(R.string.title_fd_invalid);
                alDialog.show();
            } else if (etDescription.getText().toString().equals("")) {
                alDialog.setMessage(R.string.desc_fd_invalid);
                alDialog.show();
            } else {
                Formatter urlCreator = new Formatter();
                int ch = swtcAccess.isChecked() ? 1 : 0;
                if (deckToEdit != null) {
                    try {
                        urlCreator.format(getString(R.string.url_update_deck),
                                token,
                                deckToEdit.getUid(),
                                URLEncoder.encode(etTitle.getText().toString(), "UTF-8"),
                                URLEncoder.encode(etDescription.getText().toString(), "UTF-8"),
                                URLEncoder.encode(etKeyWords.getText().toString(), "UTF-8"),
                                ch,
                                System.currentTimeMillis() / 1000);
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                } else {
                    try {
                        urlCreator.format(getString(R.string.url_add_deck),
                                token,
                                Tools.createUID(),
                                URLEncoder.encode(etTitle.getText().toString(), "UTF-8"),
                                URLEncoder.encode(etDescription.getText().toString(), "UTF-8"),
                                URLEncoder.encode(etKeyWords.getText().toString(), "UTF-8"),
                                ch,
                                System.currentTimeMillis() / 1000);
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                }
                Tools.editUpdateDeck(EditDeckActivity.this, urlCreator.toString());
            }
        }
    };

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

    public void onBackgroundTaskCompleted(Object result) {
        Intent cardsIntent = new Intent(EditDeckActivity.this, CardsActivity.class);
        cardsIntent.putExtra(Tools.deckExtraTag, (Deck) result);
        startActivity(cardsIntent);

    }

    @Override
    public void onBackgroundTaskCompleted(ArrayList<Deck> result) {

    }
}
