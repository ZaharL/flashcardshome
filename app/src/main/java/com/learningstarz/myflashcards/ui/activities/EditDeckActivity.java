package com.learningstarz.myflashcards.ui.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;

import com.learningstarz.myflashcards.R;

/**
 * Created by ZahARin on 30.05.2016.
 */
public class EditDeckActivity extends AppCompatActivity {

    EditText etDeckName;
    EditText etTitle;
    EditText etDescription;
    EditText etKeyWords;
    Button btnSave;
    Switch swtcAccess;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_deck);

        initToolbar();
        initViews();
    }

    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.EditDeckActivity_toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    private void initViews() {
        etDeckName = (EditText) findViewById(R.id.EditDeckActivity_etDeckName);
        etTitle = (EditText) findViewById(R.id.EditDeckActivity_etTitle);
        etDescription = (EditText) findViewById(R.id.EditDeckActivity_etDescription);
        etKeyWords = (EditText) findViewById(R.id.EditDeckActivity_etKeyWords);
        btnSave = (Button) findViewById(R.id.EditDeckActivity_btnSave);
        swtcAccess = (Switch) findViewById(R.id.EditDeckActivity_switch);
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
