package com.learningstarz.myflashcards.ui.async_tasks;

import android.content.Context;
import android.os.AsyncTask;
import android.view.View;
import android.widget.ProgressBar;

import com.learningstarz.myflashcards.data_storage.DataManager;
import com.learningstarz.myflashcards.interfaces.TaskCompletable;
import com.learningstarz.myflashcards.tools.Tools;
import com.learningstarz.myflashcards.types.Card;
import com.learningstarz.myflashcards.types.Deck;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by ZahARin on 01.06.2016.
 */
public class SyncAllTask extends AsyncTask<String, Void, String> {
    ProgressBar pb;
    ArrayList<Deck> array;
    TaskCompletable c;

    public SyncAllTask(Context c, ProgressBar pb) {
        this.pb = pb;
        this.c = (TaskCompletable) c;
    }

    public SyncAllTask(Context c) {
        this.c = (TaskCompletable) c;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        if (pb != null) pb.setVisibility(View.VISIBLE);
        array = new ArrayList<>();
    }

    @Override
    protected String doInBackground(String... params) {
        String result = "";

        try {
            URL url = new URL(params[0]);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod("GET");
            httpURLConnection.connect();

            InputStream is = httpURLConnection.getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(is));

            String line = "";
            StringBuilder sb = new StringBuilder();

            while ((line = br.readLine()) != null) {
                sb.append(line);
            }

            result = sb.toString();

            br.close();
            is.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);

        super.onPostExecute(s);
        try {
            JSONObject firstData = new JSONObject(s);
            JSONArray arrayOfData = firstData.getJSONObject(Tools.jsonResult).getJSONArray(Tools.jsonData);
            for (int i = 0; i < arrayOfData.length(); i++) {
                JSONObject data = arrayOfData.getJSONObject(i);

                JSONArray cards = data.getJSONArray(Tools.jsonCards);
                ArrayList<Card> cardsArray = new ArrayList<>(cards.length());
                for (int j = 0; j < cards.length(); j++) {
                    JSONObject card = cards.getJSONObject(j);
                    cardsArray.add(new Card(
                            card.getInt("id"),
                            card.getString("uid"),
                            card.getInt("lastDateUpdated"),
                            card.getString("deckUId"),
                            card.getString("question"),
                            card.getString("answer"),
                            card.getString("image"),
                            card.getString("image2"),
                            card.getString("imagepath"),
                            card.getInt("cardTime"),
                            card.getInt("knowStatus")
                    ));
                }
                array.add(new Deck(
                        data.getInt("id"),
                        data.getString("uid"),
                        data.getString("title"),
                        data.getString("author"),
                        cardsArray,
                        data.getInt("cardsCount"),
                        data.getInt("dateCreated"),
                        data.getInt("lastDateUpdated"),
                        data.getInt("progress"),
                        data.getInt("deckType"),
                        data.getInt("owner"),
                        data.getInt("deckTime"),
                        data.getString("keywords"),
                        data.getString("description")
                ));
            }

            DataManager.setDecks(array);
            c.onBackgroundTaskCompleted(array);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (pb != null) pb.setVisibility(View.INVISIBLE);
    }
}
