package com.learningstarz.myflashcards.Types;

/**
 * Created by ZahARin on 09.01.2016.
 */
public class UserClass {

    private int id;
    private String name;
    private String sectionId;
    private int TeacherId;
    private String ClassCode;
    private String ClassNumber;
    private String url;
    private String baseUrl;

    public UserClass(int id, String name, String sectionId, int teacherId, String classCode, String classNumber, String url, String baseUrl) {
        this.id = id;
        this.name = name;
        this.sectionId = sectionId;
        TeacherId = teacherId;
        ClassCode = classCode;
        ClassNumber = classNumber;
        this.url = url;
        this.baseUrl = baseUrl;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getSectionId() {
        return sectionId;
    }

    public int getTeacherId() {
        return TeacherId;
    }

    public String getClassCode() {
        return ClassCode;
    }

    public String getClassNumber() {
        return ClassNumber;
    }

    public String getUrl() {
        return url;
    }

    public String getBaseUrl() {
        return baseUrl;
    }
}
