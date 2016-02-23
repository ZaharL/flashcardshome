package com.learningstarz.myflashcards.data_storage;

import android.util.Log;

import com.learningstarz.myflashcards.types.Card;
import com.learningstarz.myflashcards.types.Deck;
import com.learningstarz.myflashcards.types.User;
import com.learningstarz.myflashcards.types.UserClass;

import java.util.ArrayList;

/**
 * Created by ZahARin on 11.02.2016.
 */
public class DataManager {

    private static User user; // current user data;
    private static ArrayList<Deck> decks;
    private static ArrayList<UserClass> userClasses;

    private DataManager() {
    }

    public static void setUser(User u) {
        DataManager.user = u;
    }

    public static User getUser() {
        return user;
    }

    public static String getUserToken() {
        return user.getToken();
    }

    public static void setDecks(ArrayList<Deck> decks) {
            DataManager.decks = decks;
    }

    public static ArrayList<Deck> getDecks() {
        return decks;
    }

    public static Deck getDeck(int i) {
        return decks.get(i);
    }

    public static void setUserClasses(ArrayList<UserClass> userClasses) {
        DataManager.userClasses = userClasses;
    }

    public static void deleteDeckByUId(String uid) {
        for (Deck d : decks) {
            if (d.getUid().equals(uid)) {
                decks.remove(d);
                return;
            }
        }
    }

    public static void deleteCardByUId(Deck deck, Card card) {
        for (Deck d : decks) {
            if (d.getUid().equals(deck.getUid())) {
                for (Card c : d.getCards()) {
                    if (c.getUid().equals(card.getUid())) {
                        d.getCards().remove(c);
                        return;
                    }
                }
            }
        }
    }


}
