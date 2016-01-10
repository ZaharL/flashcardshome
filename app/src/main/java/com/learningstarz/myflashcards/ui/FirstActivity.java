package com.learningstarz.myflashcards.ui;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.learningstarz.myflashcards.R;
import com.learningstarz.myflashcards.Types.UserClass;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Formatter;

public class FirstActivity extends AppCompatActivity {

    ProgressBar classProgressbar;
    RelativeLayout btnChooseClass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first);
        classProgressbar = (ProgressBar) findViewById(R.id.SignInActivity_pbChooseClass);
        btnChooseClass = (RelativeLayout) findViewById(R.id.SignInActivity_rlChooseButton);
        btnChooseClass.setEnabled(false);
        initTextViews();
        initEditTextViews();
    }

    public void showPB() {
        classProgressbar.setVisibility(View.VISIBLE);
    }

    public void hidePB() {
        classProgressbar.setVisibility(View.INVISIBLE);
    }

    private void initTextViews() {
        TextView tvSignUp = (TextView) findViewById(R.id.tvSignUp);
        tvSignUp.setText(Html.fromHtml(getString(R.string.sign_up_now)));
        tvSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FirstActivity.this, SignUpActivity.class);
                startActivity(intent);
            }
        });
        TextView tvQuestion = (TextView) findViewById(R.id.tvAccountHaveQuestion);
        tvQuestion.setText(Html.fromHtml(getString(R.string.no_account)));

        TextView tvContactUs = (TextView) findViewById(R.id.tvContactUs);
        tvContactUs.setText(Html.fromHtml(getString(R.string.contact_us)));
    }

    private void initEditTextViews() {
        ((EditText) findViewById(R.id.SignInActivity_etLogin)).addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String str = s.toString();
                if (str.contains("@") && str.contains(".")) {
                    Formatter urlCreator = new Formatter();
                    urlCreator.format(getString(R.string.get_user_classes), str);
                    new GetClasses().execute(urlCreator.toString());
                    urlCreator.close();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

    private class GetClasses extends AsyncTask<String, Void, String> {

        HttpURLConnection con = null;
        BufferedReader br = null;
        String resJSONString = "";
        ArrayList<UserClass> userClassesArray = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showPB();
        }

        @Override
        protected String doInBackground(String... params) {
            try {
                URL url = new URL(params[0]);
                con = (HttpURLConnection) url.openConnection();
                con.setRequestMethod("GET");
                con.connect();

                InputStream is = con.getInputStream();
                br = new BufferedReader(new InputStreamReader(is));

                StringBuffer sb = new StringBuffer();
                String line;

                while ((line = br.readLine()) != null) {
                    sb.append(line);
                }

                resJSONString = sb.toString();
                br.close();
                is.close();

            } catch (java.io.IOException e) {
                e.printStackTrace();
            }

            return resJSONString;
        }

        @Override
        protected void onPostExecute(String resString) {
            super.onPostExecute(resString);
            JSONObject data;
            UserClass userClass;
            userClassesArray = new ArrayList<>();

            try {
                data = new JSONObject(resString);
                JSONArray classes = data
                        .getJSONObject("result")
                        .getJSONObject("data")
                        .getJSONArray("classes");

                for (int i = 0; i < classes.length(); i++) {
                    JSONObject uClass = classes.getJSONObject(i);
                    userClass = new UserClass(
                            uClass.getInt("id"),
                            uClass.getString("name"),
                            uClass.getString("section_id"),
                            uClass.getInt("teacher_id"),
                            uClass.getString("class_code"),
                            uClass.getString("call_number"),
                            uClass.getString("url"),
                            uClass.getString("baseurl"));
                    userClassesArray.add(userClass);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            userClassesArray.trimToSize();
            instantiateClassesButton(userClassesArray);
            hidePB();
        }
    }

    private void instantiateClassesButton(ArrayList<UserClass> userClasses) {
        if (!userClasses.isEmpty()) {
            btnChooseClass.setEnabled(true);
            //TODO Create transfer to other activity with classes list
        } else {
            btnChooseClass.setEnabled(false);
        }
    }

}
