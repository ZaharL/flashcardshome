package com.learningstarz.myflashcards.ui.async_tasks;

import android.content.Context;
import android.os.AsyncTask;

import com.learningstarz.myflashcards.R;
import com.learningstarz.myflashcards.tools.Tools;
import com.learningstarz.myflashcards.ui.activities.ReportsActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by ZahARin on 02.06.2016.
 */
public class GetSubscriptionTask extends AsyncTask<String, Void, String> {

    ReportsActivity context;
    public static final String PERIODICITY = "periodicity";

    public GetSubscriptionTask(Context c) {
        context = (ReportsActivity) c;
    }

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

            String line;
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
        super.onPostExecute(s);
        try {
            JSONObject result = new JSONObject(s).getJSONObject(Tools.jsonResult).getJSONObject(Tools.jsonData);
            int code = result.getInt(PERIODICITY);

            switch (code) {
                case Tools.DAILY_SUBSCRIPTION:
                    context.rbDaily.setChecked(true);
                    break;
                case Tools.WEEKLY_SUBSCRIPTION:
                    context.rbWeekly.setChecked(true);
                    break;
                case Tools.DISABLE_SUBSCRIPTION:
                    context.rbDisabled.setChecked(true);
                    break;
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
}
