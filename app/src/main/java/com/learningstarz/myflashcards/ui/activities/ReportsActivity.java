package com.learningstarz.myflashcards.ui.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

import com.learningstarz.myflashcards.R;
import com.learningstarz.myflashcards.Tools.Tools;
import com.learningstarz.myflashcards.Types.User;

/**
 * Created by ZahARin on 04.02.2016.
 */
public class ReportsActivity extends AppCompatActivity {

    Toolbar toolbar;
    User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reports);

        user = getIntent().getParcelableExtra(Tools.firstActivity_userExtraTag);

        initToolbar();
        init();
    }

    private void init() {
        EditText etEmail = (EditText) findViewById(R.id.ReportsActivity_etEmail);
        etEmail.setText(user.getEmail());
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
