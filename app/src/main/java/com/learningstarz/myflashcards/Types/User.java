package com.learningstarz.myflashcards.Types;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by ZahARin on 20.01.2016.
 */
public class User implements Parcelable{
    private String fullName;
    private String token;
    private int roleId;

    public User(String fullName, String token, int roleId) {
        this.fullName = fullName;
        this.token = token;
        this.roleId = roleId;
    }

    protected User(Parcel in) {
        fullName = in.readString();
        token = in.readString();
        roleId = in.readInt();
    }

    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };

    public String getFullName() {
        return fullName;
    }

    public String getToken() {
        return token;
    }

    public int getRoleId() {
        return roleId;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(fullName);
        dest.writeString(token);
        dest.writeInt(roleId);
    }
}
