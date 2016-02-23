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
import java.net.URL;

/**
 * Created by ZahARin on 11.02.2016.
 */
public class DeleteCardAsyncTask extends AsyncTask<String, Void, String> {

    Context mContext;
    AlertDialog.Builder builder;

    public DeleteCardAsyncTask(Context context) {
        this.mContext = context;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        builder = new AlertDialog.Builder(mContext);
    }

    @Override
    protected String doInBackground(String... params) {
        StringBuilder sb = null;
        try {
            URL url = new URL(params[0]);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");
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

    @Override
    protected void onPostExecute(String s) {
        try {
            JSONObject result = new JSONObject(s).getJSONObject(Tools.jsonResult).getJSONObject(Tools.jsonStatus);
            int code = result.getInt(Tools.jsonStatusCode);

            switch (code) {
                case Tools.errOk:
                    return;
                case Tools.errDelete:
                    builder.setTitle(R.string.error);
                    builder.setTitle(result.getString(Tools.jsonStatusDesc));
                    builder.setNegativeButton(R.string.ok, null);
                    builder.show();
                    break;
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
