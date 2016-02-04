package com.learningstarz.myflashcards.Tools;

import android.util.Log;

import com.learningstarz.myflashcards.Types.Deck;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by ZahARin on 11.01.2016.
 */
public class Tools {
    //ExtraTags
    public static final String firstActivity_userClassExtraTag = "uclextra";
    public static final String firstActivity_userClassNameExtraTag = "uclnmextra";
    public static final String firstActivity_userExtraTag = "userextra";

    public static final String jsonData = "data";
    public static final String jsonResult = "result";
    public static final String jsonStatus = "status";
    public static final String jsonCards = "cards";

    public static final int errLogIn = 111;
    public static final int errDelete = 105;
    public static final int errFieldsCannotBeEmpty = 102;
    public static final int errInvToken = 122;
    public static final int errOk = 200;
    public static final int errReserved = 1000;
    public static final int errUserNotEnrolled = 1001;
    public static final int errInvDeckId = 1002;
    public static final int errFuncUnderConstruct = 1003;

    private static final String EMAIL_PATTERN =
            "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                    + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
    static Pattern pattern = Pattern.compile(EMAIL_PATTERN);


    public static boolean validateEmail(String str) {
        Matcher matcher = pattern.matcher(str);
        return matcher.matches();
    }

    /**
     * @param decks decks to sort.
     * @param sortType needs to sort decks. 1 - by date, 2 - name, 3 - author
     * @return array of sorted decks
     */
    public static ArrayList<Deck> sortDecks(ArrayList<Deck> decks, int sortType) {
        ArrayList<Deck> result = new ArrayList<>(decks.size());
        result.addAll(decks);
        Deck tempDeck;
        switch (sortType) {
            case 1:
                Collections.sort(result, ORDER_DECKS_BY_DATE);
                break;
            case 2:

                Collections.sort(result, ALPHABETICAL_ORDER_BY_NAME);
                break;
            case 3:
                Collections.sort(result, ALPHABETICAL_ORDER_BY_AUTHOR);
                break;
        }
        return result;
    }

    private static Comparator<Deck> ALPHABETICAL_ORDER_BY_NAME = new Comparator<Deck>() {
        @Override
        public int compare(Deck lhs, Deck rhs) {
            int res = String.CASE_INSENSITIVE_ORDER.compare(lhs.getTitle(), rhs.getTitle());
            return (res != 0) ? res : lhs.getTitle().compareTo(rhs.getTitle());
        }
    };

    private static Comparator<Deck> ALPHABETICAL_ORDER_BY_AUTHOR = new Comparator<Deck>() {
        @Override
        public int compare(Deck lhs, Deck rhs) {
            int res = String.CASE_INSENSITIVE_ORDER.compare(lhs.getAuthor(), rhs.getAuthor());
            return (res != 0) ? res : lhs.getAuthor().compareTo(rhs.getAuthor());
        }
    };

    private static Comparator<Deck> ORDER_DECKS_BY_DATE = new Comparator<Deck>() {
        @Override
        public int compare(Deck lhs, Deck rhs) {
            return rhs.getLastDateUpdated().compareTo(lhs.getLastDateUpdated());
        }
    };
}
