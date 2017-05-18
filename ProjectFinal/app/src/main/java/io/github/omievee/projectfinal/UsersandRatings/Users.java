package io.github.omievee.projectfinal.UsersandRatings;

/**
 * Created by omievee on 5/18/17.
 */

public class Users {
    private String mName;
    private String mLastName;
    private String mEmail;
    private double mRating;

    public double getmRating() {
        return mRating;
    }

    public void setmRating(double mRating) {
        this.mRating = mRating;
    }

    public Users(String mName, String mLastName, String mEmail, String mID) {
        this.mName = mName;
        this.mLastName = mLastName;
        this.mEmail = mEmail;
        this.mID = mID;
        mRating = 2.5;
    }

    private String mID;

    public Users() {
    }

    public Users(String uid, String displayName, String email) {
        mID = uid;
        mName = displayName;
        mEmail = email;
        mRating = 2.5;
    }

    public String getmName() {
        return mName;
    }

    public void setmName(String mName) {
        this.mName = mName;
    }

    public String getmLastName() {
        return mLastName;
    }

    public void setmLastName(String mLastName) {
        this.mLastName = mLastName;
    }

    public String getmEmail() {
        return mEmail;
    }

    public void setmEmail(String mEmail) {
        this.mEmail = mEmail;
    }

    public String getmID() {
        return mID;
    }

    public void setmID(String mID) {
        this.mID = mID;
    }
}
