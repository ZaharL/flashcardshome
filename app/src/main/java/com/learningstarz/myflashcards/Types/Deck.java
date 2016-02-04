package com.learningstarz.myflashcards.Types;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by ZahARin on 24.01.2016.
 */
public class Deck implements Parcelable{
    private int id;
    private String uid;
    private String title;
    private String author;
    private ArrayList<Card> cards;
    private int cardsCount;
    private long dateCreated;
    private long lastDateUpdated;
    private int progress;
    private int deckType;
    private int owner;
    private int deckTime;
    private String keyWords;
    private String description;

    public Deck(int id, String uid, String title, String author, ArrayList<Card> cards,
                int cardsCount, long dateCreated, long lastDateUpdated, int progress,
                int deckType, int owner, int deckTime, String keyWords, String description) {
        this.id = id;
        this.uid = uid;
        this.title = title;
        this.author = author;
        this.cards = cards;
        this.cardsCount = cardsCount;
        this.dateCreated = dateCreated;
        this.lastDateUpdated = lastDateUpdated;
        this.progress = progress;
        this.deckType = deckType;
        this.owner = owner;
        this.deckTime = deckTime;
        this.keyWords = keyWords;
        this.description = description;
    }

    protected Deck(Parcel in) {
        id = in.readInt();
        uid = in.readString();
        title = in.readString();
        author = in.readString();
        cardsCount = in.readInt();
        dateCreated = in.readLong();
        lastDateUpdated = in.readLong();
        progress = in.readInt();
        deckType = in.readInt();
        owner = in.readInt();
        deckTime = in.readInt();
        keyWords = in.readString();
        description = in.readString();
    }

    public static final Creator<Deck> CREATOR = new Creator<Deck>() {
        @Override
        public Deck createFromParcel(Parcel in) {
            return new Deck(in);
        }

        @Override
        public Deck[] newArray(int size) {
            return new Deck[size];
        }
    };

    public int getId() {
        return id;
    }

    public String getUid() {
        return uid;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public ArrayList<Card> getCards() {
        return cards;
    }

    public int getCardsCount() {
        return cardsCount;
    }

    public Date getDateCreated() {
        dateCreated = dateCreated * 1000;
        return new Date(dateCreated);
    }

    public Date getLastDateUpdated() {
        return new Date((lastDateUpdated * 1000));
    }

    public int getProgress() {
        return progress;
    }

    public int getDeckType() {
        return deckType;
    }

    public int getOwner() {
        return owner;
    }

    public int getDeckTime() {
        return deckTime;
    }

    public String getKeyWords() {
        return keyWords;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(uid);
        dest.writeString(title);
        dest.writeString(author);
        dest.writeInt(cardsCount);
        dest.writeLong(dateCreated);
        dest.writeLong(lastDateUpdated);
        dest.writeInt(progress);
        dest.writeInt(deckType);
        dest.writeInt(owner);
        dest.writeInt(deckTime);
        dest.writeString(keyWords);
        dest.writeString(description);
    }
}
