package com.learningstarz.myflashcards.ui.async_tasks;

import android.app.AlertDialog;
import android.content.Context;
import android.os.AsyncTask;

import com.learningstarz.myflashcards.R;
import com.learningstarz.myflashcards.tools.Tools;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by ZahARin on 02.05.2016.
 */
public class PostRequestSenderAsyncTask extends AsyncTask<String, Void, String> {
    @Override
    protected String doInBackground(String... params) {
        StringBuilder sb = null;
        try {
            URL url = new URL(params[0]);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("POST");
            con.connect();
            InputStream is = con.getInputStream();

            BufferedReader br = new BufferedReader(new InputStreamReader(is));

            String line = "";
            sb = new StringBuilder();

            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        assert sb != null;
        return sb.toString();
    }


}
