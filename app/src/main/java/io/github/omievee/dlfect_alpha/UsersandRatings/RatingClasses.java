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
    private long friendlyCOUNT = 0;
    private double friendlySUM;
    private double mannersSUM;

    public double getFriendly() {
        return Friendly;
    }

    public void setFriendly(double friendly) {
        Friendly = friendly;
    }

    public double getPresentation() {
        return Presentation;
    }

    public void setPresentation(double presentation) {
        Presentation = presentation;
    }

    public double getEngagement() {
        return Engagement;
    }

    public void setEngagement(double engagement) {
        Engagement = engagement;
    }

    public double getManners() {
        return Manners;
    }

    public void setManners(double manners) {
        Manners = manners;
    }

    public double getProfessionalism() {
        return Professionalism;
    }

    public void setProfessionalism(double professionalism) {
        Professionalism = professionalism;
    }

    public double getOverAll() {
        return OverAll;
    }

    public void setOverAll(double overAll) {
        OverAll = overAll;
    }

    public long getFriendlyCOUNT() {
        return friendlyCOUNT;
    }

    public void setFriendlyCOUNT(long friendlyCOUNT) {
        this.friendlyCOUNT = friendlyCOUNT;
    }

    public double getFriendlySUM() {
        return friendlySUM;
    }

    public void setFriendlySUM(double friendlySUM) {
        this.friendlySUM = friendlySUM;
    }

    public double getMannersSUM() {
        return mannersSUM;
    }

    public void setMannersSUM(double mannersSUM) {
        this.mannersSUM = mannersSUM;
    }

    public long getMannersCOUNT() {
        return mannersCOUNT;
    }

    public void setMannersCOUNT(long mannersCOUNT) {
        this.mannersCOUNT = mannersCOUNT;
    }

    public double getEngSUM() {
        return engSUM;
    }

    public void setEngSUM(double engSUM) {
        this.engSUM = engSUM;
    }

    public int getEngCOUNT() {
        return engCOUNT;
    }

    public void setEngCOUNT(int engCOUNT) {
        this.engCOUNT = engCOUNT;
    }

    public double getProSUM() {
        return proSUM;
    }

    public void setProSUM(double proSUM) {
        this.proSUM = proSUM;
    }

    public long getProCOUNT() {
        return proCOUNT;
    }

    public void setProCOUNT(long proCOUNT) {
        this.proCOUNT = proCOUNT;
    }

    public double getPreSUM() {
        return preSUM;
    }

    public void setPreSUM(double preSUM) {
        this.preSUM = preSUM;
    }

    public long getPreCOUNT() {
        return preCOUNT;
    }

    public void setPreCOUNT(long preCOUNT) {
        this.preCOUNT = preCOUNT;
    }

    private long mannersCOUNT = 0;

    public RatingClasses() {
    }

    private double engSUM;
    private int engCOUNT = 0;
    private double proSUM;
    private long proCOUNT = 0;
    private double preSUM;
    private long preCOUNT = 0;


    public RatingClasses(double friendly, double presentation, double engagement, double manners, double professionalism, double overAll, long friendlyCOUNT, double friendlySUM, double mannersSUM, long mannersCOUNT, double engSUM, int engCOUNT, double proSUM, long proCOUNT, double preSUM, long preCOUNT) {
        Friendly = friendly;
        Presentation = presentation;
        Engagement = engagement;
        Manners = manners;
        Professionalism = professionalism;
        OverAll = overAll;
        this.friendlyCOUNT = friendlyCOUNT;
        this.friendlySUM = friendlySUM;
        this.mannersSUM = mannersSUM;
        this.mannersCOUNT = mannersCOUNT;
        this.engSUM = engSUM;
        this.engCOUNT = engCOUNT;
        this.proSUM = proSUM;
        this.proCOUNT = proCOUNT;
        this.preSUM = preSUM;
        this.preCOUNT = preCOUNT;
    }
}

