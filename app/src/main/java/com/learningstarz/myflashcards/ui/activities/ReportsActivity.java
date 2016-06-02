package com.learningstarz.myflashcards.ui.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import com.learningstarz.myflashcards.R;
import com.learningstarz.myflashcards.data_storage.DataManager;
import com.learningstarz.myflashcards.tools.Tools;
import com.learningstarz.myflashcards.types.User;
import com.learningstarz.myflashcards.ui.async_tasks.GetSubscriptionTask;
import com.learningstarz.myflashcards.ui.async_tasks.PostRequestSenderAsyncTask;

import java.util.Formatter;

/**
 * Created by ZahARin on 04.02.2016.
 */
public class ReportsActivity extends AppCompatActivity {

    Toolbar toolbar;
    User user;
    String token;

    public RadioButton rbDaily;
    public RadioButton rbWeekly;
    public RadioButton rbDisabled;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reports);

        user = DataManager.getUser();
        token = user.getToken();

        initToolbar();
        init();
    }

    private void init() {
        EditText etEmail = (EditText) findViewById(R.id.ReportsActivity_etEmail);
        etEmail.setText(user.getEmail());
        rbDaily = (RadioButton) findViewById(R.id.ReportsActivity_rbDaily);
        rbWeekly = (RadioButton) findViewById(R.id.ReportsActivity_rbWeekly);
        rbDisabled = (RadioButton) findViewById(R.id.ReportsActivity_rbDisabled);
        initRadioButtons();
        findViewById(R.id.ReportsActivity_btnSubscribe).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Formatter fm = new Formatter();
                if (rbDaily.isChecked()) {
                    Toast.makeText(ReportsActivity.this, getString(R.string.subscribe) + " " + convertHours(Tools.DAILY_SUBSCRIPTION), Toast.LENGTH_LONG).show();
                    fm.format(getString(R.string.url_set_subscription), token, Tools.DAILY_SUBSCRIPTION);
                    new PostRequestSenderAsyncTask().execute(fm.toString());
                    fm.close();
                } else if (rbWeekly.isChecked()) {
                    Toast.makeText(ReportsActivity.this, getString(R.string.subscribe) + " " + convertHours(Tools.WEEKLY_SUBSCRIPTION), Toast.LENGTH_LONG).show();
                    fm.format(getString(R.string.url_set_subscription), token, Tools.WEEKLY_SUBSCRIPTION);
                    new PostRequestSenderAsyncTask().execute(fm.toString());
                    fm.close();
                } else if (rbDisabled.isChecked()) {
                    Toast.makeText(ReportsActivity.this, getString(R.string.subscribe) + " " + convertHours(Tools.DISABLE_SUBSCRIPTION), Toast.LENGTH_LONG).show();
                    fm.format(getString(R.string.url_set_subscription), token, Tools.DISABLE_SUBSCRIPTION);
                    new PostRequestSenderAsyncTask().execute(fm.toString());
                    fm.close();
                }
                finish();
            }
        });
    }

    private void initRadioButtons() {
        Formatter fm = new Formatter();
        fm.format(getString(R.string.url_get_subscription), token);
        new GetSubscriptionTask(ReportsActivity.this).execute(fm.toString());
        fm.close();
    }

    private String convertHours(int h) {
        switch (h) {
            case Tools.WEEKLY_SUBSCRIPTION:
                return getString(R.string.every_week);
            case Tools.DAILY_SUBSCRIPTION:
                return getString(R.string.every_day);
            case Tools.DISABLE_SUBSCRIPTION:
                return getString(R.string.disabled);
            default:
                return getString(R.string.disabled);
        }
    }

    private void initToolbar() {
        toolbar = (Toolbar) findViewById(R.id.ReportsActivity_toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.rports_menu, menu);
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
