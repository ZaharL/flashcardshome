package com.learningstarz.myflashcards.ui.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ListView;

import com.learningstarz.myflashcards.R;
import com.learningstarz.myflashcards.data_storage.DataManager;
import com.learningstarz.myflashcards.tools.Tools;
import com.learningstarz.myflashcards.types.UserClass;

import java.util.ArrayList;

/**
 * Created by ZahARin on 10.01.2016.
 */
public class ChooseClass extends AppCompatActivity {

    ArrayList<UserClass> alUCl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.choose_activity);

        alUCl = DataManager.getUserClasses();

        ListView lvUserClassesList = (ListView) findViewById(R.id.ChooseActivity_lvClasses);
        lvUserClassesList.setAdapter(new UserClassesAdapter(this, R.layout.choose_class_list_view_item, alUCl));

        initToolbar();

    }

    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.ChooseActivity_toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setTitle(R.string.choose_class_title);
        }
    }

    private class UserClassesAdapter extends ArrayAdapter<UserClass> {

        ArrayList<UserClass> array;

        public UserClassesAdapter(Context context, int resource, ArrayList<UserClass> array) {
            super(context, resource, array);
            this.array = array;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            UserClass uClass = array.get(position);

            if (convertView == null) {
                convertView = getLayoutInflater().inflate(R.layout.choose_class_list_view_item, parent, false);
            }

            CheckBox chbChoose = (CheckBox) convertView.findViewById(R.id.ChooseActivity_chbMainCheck);
            chbChoose.setText(uClass.getName());
            if (uClass.isChecked()) {
                chbChoose.setChecked(true);
            } else {
                chbChoose.setChecked(false);
            }
            chbChoose.setOnCheckedChangeListener(new CheckedListener(uClass));
            return convertView;
        }
    }

    private class CheckedListener implements CompoundButton.OnCheckedChangeListener {

        UserClass userClass;

        public CheckedListener(UserClass uClass) {
            userClass = uClass;
        }

        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            if (isChecked) {
                DataManager.setChecked(userClass.getId(), 1);
                Intent returnIntent = new Intent();
                returnIntent.putExtra(Tools.firstActivity_userClassNameExtraTag, userClass);
                setResult(RESULT_OK, returnIntent);
                finish();
            } else {
                DataManager.setChecked(userClass.getId(), 0);
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.choose_classes, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                super.onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
