package com.learningstarz.myflashcards.Types;

import java.util.Date;

/**
 * Created by ZahARin on 24.01.2016.
 */
public class Card {
    private int id;
    private String uid;
    private int lastDateUpdated; //TODO create int to date transformation
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
}
