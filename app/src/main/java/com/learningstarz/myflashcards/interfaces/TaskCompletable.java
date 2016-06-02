package com.learningstarz.myflashcards.interfaces;

import com.learningstarz.myflashcards.types.Deck;

import java.util.ArrayList;

/**
 * Created by ZahARin on 01.06.2016.
 */
public interface TaskCompletable {
    void onBackgroundTaskCompleted(Object result);
    void onBackgroundTaskCompleted(ArrayList<Deck> result);
}
