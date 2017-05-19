package io.github.omievee.dlfect_alpha.UsersandRatings;

import static android.R.attr.rating;

/**
 * Created by omievee on 5/18/17.
 */

public class MyUsers {

    private String mName;
    private String mLastName;
    private String mEmail;
    private double mRating = 2.5;


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

    public double getmRating() {
        return mRating;
    }

    public void setmRating(double mRating) {
        this.mRating = mRating;
    }

    public MyUsers(String mName, String mLastName, String mEmail, double mRating) {
        this.mName = mName;
        this.mLastName = mLastName;
        this.mEmail = mEmail;
        this.mRating = mRating;
    }

    public MyUsers() {
    }
}

