package io.github.omievee.dlfect_alpha.UsersandRatings;

/**
 * Created by omievee on 5/16/17.
 */

public class RatingActions {

     private String mUserRating, mUserRated;
     private double mRateSent;

    public RatingActions() {

    }
    public RatingActions(String mUserRating, String mUserRated, double mRateSent) {

        this.mUserRating = mUserRating;
        this.mUserRated = mUserRated;
        this.mRateSent = mRateSent;
    }

    public String getmUserRating() {
        return mUserRating;
    }

    public void setmUserRating(String mUserRating) {
        this.mUserRating = mUserRating;
    }

    public String getmUserRated() {
        return mUserRated;
    }

    public void setmUserRated(String mUserRated) {
        this.mUserRated = mUserRated;
    }

    public double getmRateSent() {
        return mRateSent;
    }

    public void setmRateSent(double mRateSent) {
        this.mRateSent = mRateSent;
    }




}
