package com.aadevelopers.cashkingapp.csm.model;

public class OfferWall_Model {
    String title;
    String disc;
    String image;
    String type;
    String points;
    public OfferWall_Model(String title, String disc, String image, String type, String points) {
        this.title = title;
        this.disc = disc;
        this.image = image;
        this.type = type;
        this.points = points;
    }

    public String getTitle() {
        return title;
    }

    public String getDisc() {
        return disc;
    }

    public String getImage() {
        return image;
    }

    public String getType() {
        return type;
    }

    public String getPoints() {
        return points;
    }

    public void setPoints(String points) {
        this.points = points;
    }
}
