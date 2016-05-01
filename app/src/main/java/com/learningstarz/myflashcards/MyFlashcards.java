package com.learningstarz.myflashcards;

import android.app.Application;
import android.content.Context;

/**
 * Created by ZahARin on 07.03.2016.
 */
public class MyFlashcards extends Application {

    private static Context mContext;

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = getApplicationContext();
    }

    public static Context getMyFlashcardsContext() {
        return mContext;
    }
}
