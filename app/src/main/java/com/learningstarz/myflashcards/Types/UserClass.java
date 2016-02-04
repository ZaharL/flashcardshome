package com.learningstarz.myflashcards.Types;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

/**
 * Created by ZahARin on 09.01.2016.
 */
public class UserClass implements Parcelable {

    private int id;
    private String name;
    private String sectionId;
    private int TeacherId;
    private String ClassCode;
    private String ClassNumber;
    private String url;
    private String baseUrl;
    private Boolean checked = false;

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

    protected UserClass(Parcel in) {
        id = in.readInt();
        name = in.readString();
        sectionId = in.readString();
        TeacherId = in.readInt();
        ClassCode = in.readString();
        ClassNumber = in.readString();
        url = in.readString();
        baseUrl = in.readString();
    }

    public static final Creator<UserClass> CREATOR = new Creator<UserClass>() {
        @Override
        public UserClass createFromParcel(Parcel in) {
            return new UserClass(in);
        }

        @Override
        public UserClass[] newArray(int size) {
            return new UserClass[size];
        }
    };

    public boolean isChecked() {
        return checked;
    }

    public  void setChecked(Boolean b) {
        checked = b;
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(name);
        dest.writeString(sectionId);
        dest.writeInt(TeacherId);
        dest.writeString(ClassCode);
        dest.writeString(ClassNumber);
        dest.writeString(url);
        dest.writeString(baseUrl);
    }
}
