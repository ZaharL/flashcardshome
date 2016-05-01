package com.learningstarz.myflashcards.ui.activities;

import android.app.AlertDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.learningstarz.myflashcards.R;
import com.learningstarz.myflashcards.data_storage.DataManager;
import com.learningstarz.myflashcards.tools.Tools;
import com.learningstarz.myflashcards.types.Deck;
import com.learningstarz.myflashcards.ui.async_tasks.PostRequestSenderAsyncTask;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Formatter;
import java.util.concurrent.ExecutionException;

/**
 * Created by ZahARin on 01.05.2016.
 */
public class EditCardsActivity extends AppCompatActivity {

    private Button btnSave;
    private EditText etQuestion;
    private EditText etAnswer;
    Deck deck;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card_edit);

        deck = getIntent().getParcelableExtra(Tools.deckExtraTag);

        btnSave = (Button) findViewById(R.id.CardEditActivity_btnSave);
        btnSave.setOnClickListener(saveListener);
        etQuestion = (EditText) findViewById(R.id.CardEditActivity_etFace);
        etAnswer = (EditText) findViewById(R.id.CardEditActivity_etBack);

        initToolbar();
    }

    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.CardEditActivity_toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    Button.OnClickListener saveListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            String question = etQuestion.getText().toString();
            String answer = etAnswer.getText().toString();
            AlertDialog.Builder alDialog = new AlertDialog.Builder(EditCardsActivity.this);
            alDialog.setPositiveButton(R.string.ok, null);
            alDialog.setTitle(R.string.form_validation);
            if (question.equals("")) {
                alDialog.setMessage(R.string.dm_err_msg_face_cant_be_empty);
                alDialog.show();
            } else if (answer.equals("")) {
                alDialog.setMessage(R.string.dm_err_msg_back_cant_be_empty);
                alDialog.show();
            } else {
                Formatter urlCreator = new Formatter();
                //TODO add "add image" mechanism
                urlCreator.format(getString(R.string.url_add_deck_card), Tools.createUID(), deck.getUid(), DataManager.getUserToken(), etQuestion.getText().toString(), etAnswer.getText().toString());
                PostRequestSenderAsyncTask prs = new PostRequestSenderAsyncTask();
                prs.execute(urlCreator.toString());
                String res = null;
                try {
                    res = prs.get();
                } catch (InterruptedException | ExecutionException e) {
                    e.printStackTrace();
                }
                try {
                    JSONObject result = new JSONObject(res).getJSONObject(Tools.jsonResult).getJSONObject(Tools.jsonStatus);
                    int code = result.getInt(Tools.jsonStatusCode);
                    if (code == Tools.errOk) {
                        Toast.makeText(EditCardsActivity.this, R.string.new_card_created, Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(EditCardsActivity.this, R.string.something_wrong_try_one, Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                urlCreator.close();
                finish();
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

        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }
}
