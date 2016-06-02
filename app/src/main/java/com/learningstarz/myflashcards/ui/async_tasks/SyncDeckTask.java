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
import com.learningstarz.myflashcards.ui.activities.EditDeckActivity;

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
public class SyncDeckTask extends AsyncTask<String, Void, String> {


    ProgressBar pb;
    TaskCompletable c;
    boolean sync;

    public SyncDeckTask(Context c, ProgressBar pb, boolean sync) {
        this.pb = pb;
        this.c = (TaskCompletable) c;
        this.sync = sync;
    }

    public SyncDeckTask(Context c, boolean sync) {
        this.c = (TaskCompletable) c;
        this.sync = sync;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        if (pb != null) pb.setVisibility(View.VISIBLE);
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

        try {
            Deck deck;
            JSONObject jsonDeck;
            JSONObject firstData = new JSONObject(s);
            if (!sync) {
                jsonDeck = firstData.getJSONObject(Tools.jsonResult).getJSONObject(Tools.jsonData).getJSONObject(Tools.jsonDeck);
            } else {
                jsonDeck = firstData.getJSONObject(Tools.jsonResult).getJSONObject(Tools.jsonData);
            }
            JSONArray cards = jsonDeck.getJSONArray(Tools.jsonCards);
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
            deck = new Deck(
                    jsonDeck.getInt("id"),
                    jsonDeck.getString("uid"),
                    jsonDeck.getString("title"),
                    jsonDeck.getString("author"),
                    cardsArray,
                    jsonDeck.getInt("cardsCount"),
                    jsonDeck.getInt("dateCreated"),
                    jsonDeck.getInt("lastDateUpdated"),
                    jsonDeck.getInt("progress"),
                    jsonDeck.getInt("deckType"),
                    jsonDeck.getInt("owner"),
                    jsonDeck.getInt("deckTime"),
                    jsonDeck.getString("keywords"),
                    jsonDeck.getString("description")
            );

            DataManager.setDeck(deck);
            c.onBackgroundTaskCompleted(deck);
        } catch (JSONException e1) {
            e1.printStackTrace();
        }

        if (pb != null) pb.setVisibility(View.INVISIBLE);
    }
}
