package com.learningstarz.myflashcards.Types;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;

/**
 * Created by ZahARin on 24.01.2016.
 */
public class Card implements Parcelable {
    private int id;
    private String uid;
    private int lastDateUpdated;
    private String deckUid;
    private String question;
    private String answer;
    private String image1;
    private String image2;
    private String imagePath;
    private int cardTime;
    private int knowStatus;

    public Card(int id, String uid, int lastDateUpdated, String deckUid, String question, String answer, String image1, String image2, String imagePath, int cardTime, int knowStatus) {
        this.id = id;
        this.uid = uid;
        this.lastDateUpdated = lastDateUpdated;
        this.deckUid = deckUid;
        this.question = question;
        this.answer = answer;
        this.image1 = image1;
        this.image2 = image2;
        this.imagePath = imagePath;
        this.cardTime = cardTime;
        this.knowStatus = knowStatus;
    }

    protected Card(Parcel in) {
        id = in.readInt();
        uid = in.readString();
        lastDateUpdated = in.readInt();
        deckUid = in.readString();
        question = in.readString();
        answer = in.readString();
        image1 = in.readString();
        image2 = in.readString();
        imagePath = in.readString();
        cardTime = in.readInt();
        knowStatus = in.readInt();
    }

    public static final Creator<Card> CREATOR = new Creator<Card>() {
        @Override
        public Card createFromParcel(Parcel in) {
            return new Card(in);
        }

        @Override
        public Card[] newArray(int size) {
            return new Card[size];
        }
    };

    public int getId() {
        return id;
    }

    public String getUid() {
        return uid;
    }

    public Date getLastDateUpdated() {
        return new Date(lastDateUpdated * 1000);
    }

    public String getDeckUid() {
        return deckUid;
    }

    public String getQuestion() {
        return question;
    }

    public String getAnswer() {
        return answer;
    }

    public String getImage1() {
        return image1;
    }

    public String getImage2() {
        return image2;
    }

    public String getImagePath() {
        return imagePath;
    }

    public int getCardTime() {
        return cardTime;
    }

    public int getKnowStatus() {
        return knowStatus;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(uid);
        dest.writeInt(lastDateUpdated);
        dest.writeString(deckUid);
        dest.writeString(question);
        dest.writeString(answer);
        dest.writeString(image1);
        dest.writeString(image2);
        dest.writeString(imagePath);
        dest.writeInt(cardTime);
        dest.writeInt(knowStatus);
    }
}
