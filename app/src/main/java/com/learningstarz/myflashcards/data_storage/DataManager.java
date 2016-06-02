package com.learningstarz.myflashcards.data_storage;

import android.util.Log;

import com.learningstarz.myflashcards.MyFlashcards;
import com.learningstarz.myflashcards.data_storage.sqlite.FCDatabaseHelper;
import com.learningstarz.myflashcards.types.Deck;
import com.learningstarz.myflashcards.types.User;
import com.learningstarz.myflashcards.types.UserClass;

import java.util.ArrayList;

/**
 * Created by ZahARin on 11.02.2016.
 */
public class DataManager {

    private static FCDatabaseHelper db;

    private DataManager() {
    }

    public static void setUser(User u) {
        db.setUser(u);
    }

    public static User getUser() {
        return db.getUser();
    }

    public static String getUserToken() {
        return db.getUserToken();
    }

    public static void setDecks(ArrayList<Deck> decks) {
        db.setUserDecks(decks);
    }

    public static void setDeck(Deck deck) {
        db.setUserDeck(deck);
    }

    public static ArrayList<Deck> getDecks() {
        return db.getUserDecks();
    }

//    //public static Deck getDeck(int i) {
//        return decks.get(i);
//    }

    public static void setNLUserEmail(String email) {
        db = FCDatabaseHelper.getInstance(MyFlashcards.getMyFlashcardsContext(), email);
        db.insertNLUser(email);
    }

    public static String getNLUserEmail() {
        if (db != null) {
            return db.getNLUserEmail();
        } else {
            return "";
        }
    }

    //writing user classes into DB
    public static void setUserClasses(ArrayList<UserClass> userClasses) {
        db.setUserClasses(userClasses);
    }

    public static ArrayList<UserClass> getUserClasses() {
        return db.getUserClasses();
    }

    public static void setChecked(int classId, int checked) {
        UserClass ucl = db.getCheckedClass();
        if (ucl != null) {
            db.updateSetChecked(ucl.getId(), 0);
        }
        db.updateSetChecked(classId, checked);
    }

    public static UserClass getCheckedClass() {
        return db.getCheckedClass();
    }

    public static void deleteDeckByUId(String uid) {
        db.deleteDeckByUID(uid);
    }

    public static void deleteCardByUId(String uid) {
        db.deleteCardByUID(uid);
    }


}
