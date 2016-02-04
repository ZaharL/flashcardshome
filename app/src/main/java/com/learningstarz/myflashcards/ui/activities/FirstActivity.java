package com.learningstarz.myflashcards.ui.activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.learningstarz.myflashcards.R;
import com.learningstarz.myflashcards.Tools.Tools;
import com.learningstarz.myflashcards.Types.User;
import com.learningstarz.myflashcards.Types.UserClass;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.net.HttpURLConnection;
import java.net.SocketException;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Formatter;

public class FirstActivity extends AppCompatActivity {

    ProgressBar classProgressbar;
    ProgressBar logInPB;

    RelativeLayout btnChooseClass;
    EditText etPassword;

    String validEmail = "";
    UserClass returnedClass = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first);
        btnChooseClass = (RelativeLayout) findViewById(R.id.SignInActivity_rlChooseButton);
        btnChooseClass.setEnabled(false);
        etPassword = (EditText) findViewById(R.id.SignInActivity_etPassword);
        Button btnLogin = (Button) findViewById(R.id.SignInActivity_btnLogin);
        btnLogin.setOnClickListener(btnLoginListener);
        initTextViews();
        initEditTextViews();
        initProgressBars();
    }

    private void initProgressBars() {
        classProgressbar = (ProgressBar) findViewById(R.id.SignInActivity_pbChooseClass);
        logInPB = (ProgressBar) findViewById(R.id.SignInActivity_pbLogIn);
    }

    public void showLogInPB() {
        logInPB.setVisibility(View.VISIBLE);
    }

    public void hideLoginPB() {
        logInPB.setVisibility(View.GONE);
    }

    public void showClassPB() {
        classProgressbar.setVisibility(View.VISIBLE);
    }

    public void hideClassPB() {
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

                if (Tools.validateEmail(str)) {
                    validEmail = str;
                    Formatter urlCreator = new Formatter();
                    urlCreator.format(getString(R.string.url_get_user_classes), str);
                    new GetClasses().execute(urlCreator.toString());
                    urlCreator.close();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

    View.OnClickListener btnLoginListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (!btnChooseClass.isEnabled()) {
                AlertDialog.Builder builder = new AlertDialog.Builder(FirstActivity.this);
                builder.setTitle(Html.fromHtml(getResources().getString(R.string.d_m_auth_error_title)));
                builder.setMessage(R.string.d_m_auth_error_email);
                builder.setNegativeButton(R.string.close, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                builder.show();
            } else if (etPassword.getText().toString().equals("")) {
                AlertDialog.Builder builder = new AlertDialog.Builder(FirstActivity.this);
                builder.setTitle(Html.fromHtml(getResources().getString(R.string.d_m_auth_error_title)));
                builder.setMessage(R.string.d_m_auth_error_password);
                builder.setNegativeButton(R.string.close, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                builder.show();
            } else if (btnChooseClass.isEnabled() && returnedClass == null) {
                AlertDialog.Builder builder = new AlertDialog.Builder(FirstActivity.this);
                builder.setTitle(Html.fromHtml(getResources().getString(R.string.d_m_auth_error_title)));
                builder.setMessage(R.string.d_m_auth_error_choose_class);
                builder.setNegativeButton(R.string.close, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                builder.show();
            } else {
                String password = etPassword.getText().toString();
                try {
                    MessageDigest md = MessageDigest.getInstance("SHA-256");
                    md.update(password.getBytes("UTF-8"));
                    byte[] digest = md.digest();
                    Formatter passwordCreator = new Formatter();
                    passwordCreator.format("%064x", new BigInteger(1, digest));
                    password = passwordCreator.toString();
                    passwordCreator.close();
                } catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                Formatter urlCreator = new Formatter();
                urlCreator.format(getString(R.string.url_log_in), validEmail, password, returnedClass.getId());
                new LogInTask().execute(urlCreator.toString());
                urlCreator.close();
            }
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            returnedClass = data.getParcelableExtra(Tools.firstActivity_userClassNameExtraTag);
            toggleChooseClassButton(true);
        } else {
            returnedClass = null;
        }
    }


    /**
     * @param b - toggle parameter. If false sets button to default mode, else sets name of returned class
     */
    private void toggleChooseClassButton(Boolean b) {
        if (!b) {
            TextView tvChooseButtonText = (TextView) btnChooseClass.findViewById(R.id.SignInActivity_tvChooseButtonName);
            tvChooseButtonText.setText(getString(R.string.choose_your_class));
            if (Build.VERSION.SDK_INT >= 23) {
                tvChooseButtonText.setTextColor(getResources().getColor(R.color.hint_text_color, null));
            } else {
                tvChooseButtonText.setTextColor(getResources().getColor(R.color.hint_text_color));
            }
        } else {
            TextView tvChooseButtonText = (TextView) btnChooseClass.findViewById(R.id.SignInActivity_tvChooseButtonName);
            tvChooseButtonText.setText(returnedClass.getName());
            if (Build.VERSION.SDK_INT >= 23) {
                tvChooseButtonText.setTextColor(getResources().getColor(R.color.white, null));
            } else {
                tvChooseButtonText.setTextColor(getResources().getColor(R.color.white));
            }
        }
    }

    private class LogInTask extends  AsyncTask<String, Void, String> {

        AlertDialog.Builder alertDialogBuilder;
        User resUser = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showLogInPB();
            alertDialogBuilder = new AlertDialog.Builder(FirstActivity.this);
        }

        @Override
        protected String doInBackground(String... params) {
            String result = "";

            try {
                URL url = new URL(params[0]);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                InputStream is = urlConnection.getInputStream();
                BufferedReader br = new BufferedReader(new InputStreamReader(is));

                StringBuilder sb = new StringBuilder();
                String line;

                while ((line = br.readLine()) != null) {
                    sb.append(line);
                }

                result = sb.toString();
                br.close();
                is.close();

            } catch (SocketException s) {
                Toast.makeText(FirstActivity.this, getString(R.string.toast_connection_error), Toast.LENGTH_SHORT).show();
                hideLoginPB();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return result;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            try {
                JSONObject dataFirst = new JSONObject(s);
                JSONObject status = dataFirst
                        .getJSONObject(Tools.jsonResult)
                        .getJSONObject(Tools.jsonStatus);
                if (status.getInt("code") == Tools.errLogIn) {
                    alertDialogBuilder.setTitle(Html.fromHtml(getString(R.string.d_m_login_error_title)));
                    alertDialogBuilder.setMessage(status.getString("description"));
                    alertDialogBuilder.setNegativeButton(R.string.close, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
                    alertDialogBuilder.show();
                    hideLoginPB();
                } else if (status.getInt("code") == Tools.errOk){
                    JSONObject data = dataFirst
                            .getJSONObject(Tools.jsonResult)
                            .getJSONObject(Tools.jsonData);
                    resUser = new User(
                            data.getString("fullname"),
                            validEmail,
                            data.getString("token"),
                            data.getInt("roleId"));
                    Intent loginIntent = new Intent(FirstActivity.this, MyDecksActivity.class);
                    loginIntent.putExtra(Tools.firstActivity_userExtraTag, resUser);
                    startActivity(loginIntent);
                    hideLoginPB();
                    finish();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private class GetClasses extends AsyncTask<String, Void, String> {

        HttpURLConnection con = null;
        BufferedReader br = null;
        String resJSONString = "";
        ArrayList<UserClass> userClassesArray = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showClassPB();
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

                StringBuilder sb = new StringBuilder();
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
                        .getJSONObject(Tools.jsonResult)
                        .getJSONObject(Tools.jsonData)
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
            hideClassPB();
        }
    }

    private void instantiateClassesButton(ArrayList<UserClass> userClasses) {
        toggleChooseClassButton(false);
        if (!userClasses.isEmpty()) {
            btnChooseClass.setEnabled(true);
            ClassesListener cl = new ClassesListener(userClasses);
            btnChooseClass.setOnClickListener(cl);
        } else {
            btnChooseClass.setEnabled(false);
            returnedClass = null;
        }
    }

    class ClassesListener implements View.OnClickListener {

        ArrayList<UserClass> userClasses;

        public ClassesListener(ArrayList<UserClass> userClasses) {
            this.userClasses = userClasses;
        }

        @Override
        public void onClick(View v) {
            Intent choose = new Intent(FirstActivity.this, ChooseClass.class);
            choose.putExtra(Tools.firstActivity_userClassExtraTag, userClasses);
            startActivityForResult(choose, 1);
        }
    }

}
