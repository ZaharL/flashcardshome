package com.learningstarz.myflashcards.data_storage.sqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.CursorIndexOutOfBoundsException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;
import android.util.Log;
import android.widget.ArrayAdapter;

import com.learningstarz.myflashcards.types.Card;
import com.learningstarz.myflashcards.types.Deck;
import com.learningstarz.myflashcards.types.User;
import com.learningstarz.myflashcards.types.UserClass;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ZahARin on 06.03.2016.
 */
public class FCDatabaseHelper extends SQLiteOpenHelper {

    private static FCDatabaseHelper sInstance;
    private static String sInstName = "";
    //db version
    public static int DATABASE_VERSION = 1;
    //db name
    public static final String DATABASE_NAME = "_MyFC_DB";
    //table names
    public static final String TABLE_USER = "user";
    public static final String TABLE_CLASS = "class";
    public static final String TABLE_DECK = "deck";
    public static final String TABLE_CARD = "card";
    public static final String TABLE_NLUSER = "nl_user";
    //common column names
    public static final String KEY_ID = "id";
    public static final String KEY_EXT_ID = "ext_id";
    public static final String KEY_UID = "uid";
    public static final String KEY_LAST_DATE_UPDATED = "last_date_updated";
    //USER table column names
    public static final String KEY_FULL_NAME = "full_name";
    public static final String KEY_TOKEN = "token";
    public static final String KEY_ROLE_ID = "role_id";
    //NLUSER table column names
    public static final String KEY_EMAIL = "email";
    //CLASS table column names
    public static final String KEY_NLUSER_ID = "nl_user_id";
    public static final String KEY_CLASS_NAME = "class_name";
    public static final String KEY_CHECKED = "checked";
    //DECK table column names
    public static final String KEY_CLASS_ID = "class_id";
    public static final String KEY_DECK_TITLE = "title";
    public static final String KEY_AUTHOR = "author";
    public static final String KEY_CARDS_COUNT = "cards_count";
    public static final String KEY_DATE_CREATED = "date_created";
    public static final String KEY_PROGRESS = "progress";
    public static final String KEY_DECK_TYPE = "deck_type";
    public static final String KEY_OWNER = "owner";
    public static final String KEY_DECK_TIME = "deck_time";
    public static final String KEY_KEY_WORDS = "key_words";
    public static final String KEY_DESCRIPTION = "description";
    //CARD table names
    public static final String KEY_DECK_ID = "deck_id";
    public static final String KEY_DECK_UID = "deck_uid";
    public static final String KEY_QUESTION = "question";
    public static final String KEY_ANSWER = "answer";
    public static final String KEY_IMAGE1 = "image1";
    public static final String KEY_IMAGE2 = "image2";
    public static final String KEY_IMAGE_PATH = "image_path";
    public static final String KEY_CARD_TIME = "card_time";
    public static final String KEY_KNOW_STATUS = "know_status";

    //TABLE CRATE STATEMENTS
    //NLUSER table create statement
    public static final String CREATE_TABLE_NLUSER = "CREATE TABLE " + TABLE_NLUSER + "(" +
            KEY_ID + " INTEGER PRIMARY KEY," +
            KEY_EMAIL + " TEXT" + ")";
    //USER table crate statement
    public static final String CREATE_TABLE_USER = "CREATE TABLE " + TABLE_USER + "(" +
            KEY_ID + " INTEGER PRIMARY KEY," +
            KEY_NLUSER_ID + " INTEGER," +
            KEY_FULL_NAME + " TEXT," +
            KEY_TOKEN + " TEXT," +
            KEY_ROLE_ID + " INTEGER" + ")";
    //CLASS table create statement
    public static final String CREATE_TABLE_CLASS = "CREATE TABLE " + TABLE_CLASS + "(" +
            KEY_ID + " INTEGER PRIMARY KEY," +
            KEY_NLUSER_ID + " INTEGER," +
            KEY_EXT_ID + " INTEGER," +
            KEY_CLASS_NAME + " TEXT," +
            KEY_CHECKED + " INTEGER DEFAULT 0" + ")";
    //DECK table create statement
    public static final String CREATE_TABLE_DECK = "CREATE TABLE " + TABLE_DECK + "(" +
            KEY_ID + " INTEGER PRIMARY KEY," +
            KEY_CLASS_ID + " INTEGER," +
            KEY_EXT_ID + " INTEGER," +
            KEY_UID + " TEXT," +
            KEY_DECK_TITLE + " TEXT," +
            KEY_AUTHOR + " TEXT," +
            KEY_CARDS_COUNT + " INTEGER," +
            KEY_DATE_CREATED + " INTEGER," +
            KEY_LAST_DATE_UPDATED + " INTEGER," +
            KEY_PROGRESS + " INTEGER," +
            KEY_DECK_TYPE + " INTEGER," +
            KEY_OWNER + " TEXT," +
            KEY_DECK_TIME + " INTEGER," +
            KEY_KEY_WORDS + " TEXT," +
            KEY_DESCRIPTION + " TEXT" + ")";
    //CARD table crate statement
    public static final String CREATE_TABLE_CARD = "CREATE TABLE " + TABLE_CARD + "(" +
            KEY_ID + " INTEGER PRIMARY KEY," +
            KEY_DECK_ID + " INTEGER," +
            KEY_EXT_ID + " INTEGER," +
            KEY_UID + " TEXT," +
            KEY_LAST_DATE_UPDATED + " INTEGER," +
            KEY_DECK_UID + " TEXT," +
            KEY_QUESTION + " TEXT," +
            KEY_ANSWER + " TEXT," +
            KEY_IMAGE1 + " TEXT," +
            KEY_IMAGE2 + " TEXT," +
            KEY_IMAGE_PATH + " TEXT," +
            KEY_CARD_TIME + " INTEGER," +
            KEY_KNOW_STATUS + " INTEGER" + ")";

    public FCDatabaseHelper(Context context, String dbName) {
        super(context, dbName + DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //creating required tables
        db.execSQL(CREATE_TABLE_NLUSER);
        db.execSQL(CREATE_TABLE_USER);
        db.execSQL(CREATE_TABLE_CLASS);
        db.execSQL(CREATE_TABLE_DECK);
        db.execSQL(CREATE_TABLE_CARD);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //on upgrade drop older tables
        db.execSQL("DROP TABLE IF EXISTS " + CREATE_TABLE_NLUSER);
        db.execSQL("DROP TABLE IF EXISTS " + CREATE_TABLE_USER);
        db.execSQL("DROP TABLE IF EXISTS " + CREATE_TABLE_CLASS);
        db.execSQL("DROP TABLE IF EXISTS " + CREATE_TABLE_DECK);
        db.execSQL("DROP TABLE IF EXISTS " + CREATE_TABLE_CARD);

        DATABASE_VERSION = newVersion;

        //create new tables
        onCreate(db);
    }

    public void setUserDeckCards(ArrayList<Card> cards, long deckId) {
        SQLiteDatabase db = getWritableDatabase();
        db.beginTransactionNonExclusive();
        for (int i = 0; i < cards.size(); i++) {
            int row_id = i + 1;
            Card card = cards.get(i);
            String sql = "INSERT OR REPLACE INTO " + TABLE_CARD + " ( " +
                    KEY_ID + ", " +
                    KEY_DECK_ID + ", " +
                    KEY_EXT_ID + ", " +
                    KEY_UID + ", " +
                    KEY_LAST_DATE_UPDATED + ", " +
                    KEY_DECK_UID + ", " +
                    KEY_QUESTION + ", " +
                    KEY_ANSWER + ", " +
                    KEY_IMAGE1 + ", " +
                    KEY_IMAGE2 + ", " +
                    KEY_IMAGE_PATH + ", " +
                    KEY_CARD_TIME + ", " +
                    KEY_KNOW_STATUS +
                    " ) VALUES ( " +
                    row_id + ", " +
                    deckId + ", " +
                    card.getId() + ", \"" +
                    card.getUid() + "\", " +
                    card.getTimestampLastDateUpdated() + ", \"" +
                    card.getDeckUid() + "\", \"" +
                    card.getQuestion() + "\", \"" +
                    card.getAnswer() + "\", \"" +
                    card.getImage1() + "\", \"" +
                    card.getImage2() + "\", \"" +
                    card.getImagePath() + "\", " +
                    card.getCardTime() + ", " +
                    card.getKnowStatus() + " )";

            SQLiteStatement stmt = db.compileStatement(sql);
            stmt.execute();
            stmt.clearBindings();
        }
        db.setTransactionSuccessful();
        db.endTransaction();
        db.close();
    }

    public static synchronized FCDatabaseHelper getInstance(Context context, String string) {
        if (!sInstName.equals(string)) sInstance = null;
        if (sInstance == null) sInstance = new FCDatabaseHelper(context.getApplicationContext(), string);
        return sInstance;
    }

    private FCDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public ArrayList<Card> getUserDeckCards(long deckId) {
        ArrayList<Card> result = new ArrayList<>();

        String query = "SELECT * FROM " + TABLE_CARD + " WHERE " + KEY_DECK_ID + " = " + deckId;

        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.rawQuery(query, null);

        if (c.moveToFirst()) {
            do {
                result.add(new Card(
                        c.getInt(c.getColumnIndex(KEY_EXT_ID)),
                        c.getString(c.getColumnIndex(KEY_UID)),
                        c.getInt(c.getColumnIndex(KEY_LAST_DATE_UPDATED)),
                        c.getString(c.getColumnIndex(KEY_DECK_UID)),
                        c.getString(c.getColumnIndex(KEY_QUESTION)),
                        c.getString(c.getColumnIndex(KEY_ANSWER)),
                        c.getString(c.getColumnIndex(KEY_IMAGE1)),
                        c.getString(c.getColumnIndex(KEY_IMAGE2)),
                        c.getString(c.getColumnIndex(KEY_IMAGE_PATH)),
                        c.getInt(c.getColumnIndex(KEY_CARD_TIME)),
                        c.getInt(c.getColumnIndex(KEY_KNOW_STATUS))
                ));
            } while (c.moveToNext());
        }
        return result;
    }

    public void setUserDecks(ArrayList<Deck> decks) {

        for (int i = 0; i < decks.size(); i++) {
            SQLiteDatabase db = getWritableDatabase();
            db.beginTransactionNonExclusive();
            int row_id = i + 1;
            Deck deck = decks.get(i);
            String sql = "INSERT OR REPLACE INTO " + TABLE_DECK + " ( " +
                    KEY_ID + "," +
                    KEY_CLASS_ID + "," +
                    KEY_EXT_ID + "," +
                    KEY_UID + "," +
                    KEY_DECK_TITLE + "," +
                    KEY_AUTHOR + "," +
                    KEY_CARDS_COUNT + "," +
                    KEY_DATE_CREATED + "," +
                    KEY_LAST_DATE_UPDATED + "," +
                    KEY_PROGRESS + "," +
                    KEY_DECK_TYPE + "," +
                    KEY_OWNER + "," +
                    KEY_DECK_TIME + "," +
                    KEY_KEY_WORDS + "," +
                    KEY_DESCRIPTION +
                    " ) VALUES ( " +
                    row_id + ", " +
                    getCheckedClassId() + ", " +
                    deck.getId() + ", \"" +
                    deck.getUid() + "\", \"" +
                    deck.getTitle() + "\", \"" +
                    deck.getAuthor() + "\", " +
                    deck.getCardsCount() + ", " +
                    deck.getTimestampDateCreated() + ", " +
                    deck.getTimestampLastDateUpdated() + ", " +
                    deck.getProgress() + ", " +
                    deck.getDeckType() + ", " +
                    deck.getOwner() + ", " +
                    deck.getDeckTime() + ", \"" +
                    deck.getKeyWords() + "\", \"" +
                    deck.getDescription() + "\" )";

            SQLiteStatement stmt = db.compileStatement(sql);
            stmt.execute();
            stmt.clearBindings();
            db.setTransactionSuccessful();
            db.endTransaction();
            db.close();
            setUserDeckCards(deck.getCards(), row_id);
        }
    }

    public ArrayList<Deck> getUserDecks() {
        ArrayList<Deck> res = new ArrayList<>();

        String query = "SELECT * FROM " + TABLE_DECK;

        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.rawQuery(query, null);

        if (c.moveToFirst()) {
            do {
                res.add(new Deck(
                        c.getInt(c.getColumnIndex(KEY_EXT_ID)),
                        c.getString(c.getColumnIndex(KEY_UID)),
                        c.getString(c.getColumnIndex(KEY_DECK_TITLE)),
                        c.getString(c.getColumnIndex(KEY_AUTHOR)),
                        getUserDeckCards(c.getInt(c.getColumnIndex(KEY_ID))),
                        c.getInt(c.getColumnIndex(KEY_CARDS_COUNT)),
                        c.getLong(c.getColumnIndex(KEY_DATE_CREATED)),
                        c.getLong(c.getColumnIndex(KEY_LAST_DATE_UPDATED)),
                        c.getInt(c.getColumnIndex(KEY_PROGRESS)),
                        c.getInt(c.getColumnIndex(KEY_DECK_TYPE)),
                        c.getInt(c.getColumnIndex(KEY_OWNER)),
                        c.getInt(c.getColumnIndex(KEY_DECK_TIME)),
                        c.getString(c.getColumnIndex(KEY_KEY_WORDS)),
                        c.getString(c.getColumnIndex(KEY_DESCRIPTION))
                ));
            } while (c.moveToNext());
        }
        c.close();
        return res;
    }

    public void deleteDeckByUID(String uid) {
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("DELETE FROM " + TABLE_DECK + " WHERE " + KEY_UID + " = " + uid);
        db.close();
    }

    public void deleteCardByUID(String uid) {
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("DELETE FROM " + TABLE_CARD + " WHERE " + KEY_UID + " = " + uid);
        db.close();
    }

    /**
    * creating not login-ed user which stores email or updating if exists
    */
    public long insertNLUser(String email) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(KEY_EMAIL, email);
//        long nluserid;
//        if ((nluserid = getNLUserID()) > 0) {
//            return db.update(TABLE_NLUSER, cv, KEY_ID + " = ?", new String[]{String.valueOf(nluserid)});
//        } else {
            return db.insert(TABLE_NLUSER, null, cv);
//        }
    }

    public long getNLUserID() {
        SQLiteDatabase db = getReadableDatabase();

        String selectQuery = "SELECT " + KEY_ID + " FROM " + TABLE_NLUSER;
        Cursor c = db.rawQuery(selectQuery, null);

        long ret = -1;
        try {
            if (c != null) {
                c.moveToFirst();
                ret = c.getLong(c.getColumnIndex(KEY_ID));
                c.close();
            }
        } catch (CursorIndexOutOfBoundsException e) {
            return ret;
        }
        return ret;
    }

    //NLUser has only one field (email) that's why there is some fp in oop:):)
    public String getNLUserEmail() {
        String ret = "";
        if (getNLUserID() > 0) {
            SQLiteDatabase db = getReadableDatabase();
            String selectQuery = "SELECT " + KEY_EMAIL + " FROM " + TABLE_NLUSER +
                    " WHERE " + KEY_ID + " = " + getNLUserID();

            Cursor c = db.rawQuery(selectQuery, null);

            if (c != null) {
                c.moveToFirst();
                ret = c.getString(c.getColumnIndex(KEY_EMAIL));
                c.close();
            }
            return ret;
        } else {
            return ret;
        }
    }

    /**
     * @return current user id or -1 if table with user is empty
     */
    public long getCurrentUserID() {
        SQLiteDatabase db = getReadableDatabase();

        String selectQuery = "SELECT " + KEY_ID + " FROM " + TABLE_USER;

        Cursor c = db.rawQuery(selectQuery, null);
        long ret = -1;
        try {
            if (c != null) {
                c.moveToFirst();
                ret = c.getLong(c.getColumnIndex(KEY_ID));
                c.close();
            }
        } catch (CursorIndexOutOfBoundsException e) {
            return ret;
        }
        return ret;
    }

    /**
     * method to get class id by
     *
     * @return
     */
    public long getClassID() {
        SQLiteDatabase db = getReadableDatabase();

        String selectQuery = "SELECT " + KEY_ID + " FROM " + TABLE_USER;

        Cursor c = db.rawQuery(selectQuery, null);
        long ret = -1;
        try {
            if (c != null) {
                c.moveToFirst();
                ret = c.getLong(c.getColumnIndex(KEY_ID));
                c.close();
            }
        } catch (CursorIndexOutOfBoundsException e) {
            return ret;
        }
        return ret;
    }

    /**
     * saves User class in DB. If one field already exists simply updates it. (Saves one user at time)
     *
     * @param usr - User class to save in DB
     * @return row id as result of query
     */
    public long setUser(User usr) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(KEY_FULL_NAME, usr.getFullName());
        cv.put(KEY_TOKEN, usr.getToken());
        cv.put(KEY_ROLE_ID, usr.getRoleId());
        cv.put(KEY_NLUSER_ID, getNLUserID());
        long curUserId;
        if ((curUserId = getCurrentUserID()) > 0) {
            return db.update(TABLE_USER, cv, KEY_ID + " = ?", new String[]{String.valueOf(curUserId)});
        } else {
            return db.insert(TABLE_USER, null, cv);
        }
    }

    /**
     * @return returns current logined User from db
     */
    public User getUser() {
        SQLiteDatabase db = getReadableDatabase();
        String selectQuery = "SELECT * FROM " + TABLE_USER;
        Cursor c = db.rawQuery(selectQuery, null);
        User usr = null;
        if (c != null) {
            c.moveToFirst();
            usr = new User(
                    c.getString(c.getColumnIndex(KEY_FULL_NAME)),
                    getNLUserEmail(),
                    c.getString(c.getColumnIndex(KEY_TOKEN)),
                    c.getInt(c.getColumnIndex(KEY_ROLE_ID))
            );
        }
        return usr;
    }

    /**
     * Returns current user token or NULL if ot exist
     *
     * @return current user token
     */
    public String getUserToken() {
        SQLiteDatabase db = getReadableDatabase();
        String selectQuery = "SELECT " + KEY_TOKEN + " FROM " + TABLE_USER;
        Cursor c = db.rawQuery(selectQuery, null);
        String usr = null;
        if (c != null) {
            c.moveToFirst();
            usr = c.getString(c.getColumnIndex(KEY_TOKEN));
        }
        return usr;
    }


    /**
     * insert new classes or replace if exists
     *
     * @param classes user classes
     */
    public void setUserClasses(ArrayList<UserClass> classes) {
        SQLiteDatabase db = getWritableDatabase();
        db.beginTransactionNonExclusive();

        for (int i = 0; i < classes.size(); i++) {
            int row_id = i + 1;
            String sql = "INSERT OR REPLACE INTO " + TABLE_CLASS + " ( " +
                    KEY_ID + ", " +
                    KEY_NLUSER_ID + ", " +
                    KEY_EXT_ID + ", " +
                    KEY_CLASS_NAME + ", " +
                    KEY_CHECKED +
                    " ) VALUES ( " +
                    row_id + ", " +
                    getNLUserID() + ", " +
                    classes.get(i).getId() + ", \"" +
                    classes.get(i).getName() + "\", " +
                    classes.get(i).getChecked() + " )";

            SQLiteStatement stmt = db.compileStatement(sql);
            stmt.execute();
            stmt.clearBindings();
        }
        db.setTransactionSuccessful();
        db.endTransaction();
        db.close();
    }

    public void updateSetChecked(int classId, int checked) {
        SQLiteDatabase db = getWritableDatabase();
        db.beginTransactionNonExclusive();
        String sql = "UPDATE " + TABLE_CLASS + " SET " + KEY_CHECKED + " = " + checked + " WHERE " + KEY_EXT_ID + " = " + classId;
        Log.d("MyLogs", "update checked query = " + sql);
        SQLiteStatement stmt = db.compileStatement(sql);
        stmt.executeUpdateDelete();
        stmt.clearBindings();
        db.setTransactionSuccessful();
        db.endTransaction();
        db.close();
//        ContentValues cv = new ContentValues();
//        cv.put(KEY_CHECKED, checked);
//        db.update(TABLE_CLASS, cv, "id = 1 ", null);

    }

    public ArrayList<UserClass> getUserClasses() {
        ArrayList<UserClass> result = new ArrayList<>();
        String query = "SELECT * FROM " + TABLE_CLASS;

        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.rawQuery(query, null);

        if (c.moveToFirst()) {
            do {
                UserClass uc = new UserClass(
                        c.getInt(c.getColumnIndex(KEY_EXT_ID)),
                        c.getString(c.getColumnIndex(KEY_CLASS_NAME)),
                        (c.getInt(c.getColumnIndex(KEY_CHECKED)) == 1)
                );
                result.add(uc);
            } while (c.moveToNext());
        }
        c.close();
        return result;
    }

    public UserClass getCheckedClass() {
        UserClass result;
        String query = "SELECT * FROM " + TABLE_CLASS + " WHERE " + KEY_CHECKED + " = 1";

        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.rawQuery(query, null);
        try {
            c.moveToFirst();
            result = new UserClass(
                    c.getInt(c.getColumnIndex(KEY_EXT_ID)),
                    c.getString(c.getColumnIndex(KEY_CLASS_NAME)),
                    (c.getInt(c.getColumnIndex(KEY_CHECKED)) == 1)
            );
            c.close();
        } catch (CursorIndexOutOfBoundsException e) {
            return null;
        }
        return result;
    }

    /**
     * @return returns checked class
     */
    public long getCheckedClassId() {
        long result;
        String query = "SELECT * FROM " + TABLE_CLASS + " WHERE " + KEY_CHECKED + " = 1";
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.rawQuery(query, null);
        try {
            c.moveToFirst();
            result = c.getLong(c.getColumnIndex(KEY_ID));
            c.close();
        } catch (CursorIndexOutOfBoundsException e) {
            return 0;
        }
        return result;
    }

}
