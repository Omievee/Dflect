package io.github.omievee.dlfect_alpha.UsersandRatings;

/**
 * Created by omievee on 5/19/17.
 */

public class RatingClasses {

    private double Friendly = .5;
    private double Presentation = .5;
    private double Engagement = .5;
    private double Manners = .5;
    private double Professionalism = .5;
    private double OverAll = 2.0;

    public RatingClasses() {
    }

    public RatingClasses(double Friendly, double Presentation, double Engagement, double Manners, double Professionalism, double OverAll) {
        this.Friendly = Friendly;
        this.Presentation = Presentation;
        this.Engagement = Engagement;
        this.Manners = Manners;
        this.Professionalism = Professionalism;
        this.OverAll = OverAll;
    }

    public double getFriendly() {

        return Friendly;
    }

    public void setFriendly(double Friendly) {
        this.Friendly = Friendly;
    }

    public double getPresentation() {
        return Presentation;
    }

    public void setPresentation(double Presentation) {
        this.Presentation = Presentation;
    }

    public double getEngagement() {
        return Engagement;
    }

    public void setEngagement(double Engagement) {
        this.Engagement = Engagement;
    }

    public double getManners() {
        return Manners;
    }

    public void setManners(double Manners) {
        this.Manners = Manners;
    }

    public double getProfessionalism() {
        return Professionalism;
    }

    public void setProfessionalism(double Professionalism) {
        this.Professionalism = Professionalism;
    }

    public double getOverAll() {
        return OverAll;
    }

    public void setOverAll(double OverAll) {
       this.OverAll = OverAll;
    }
}

