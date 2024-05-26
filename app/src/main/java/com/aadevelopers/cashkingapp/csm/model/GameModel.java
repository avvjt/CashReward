package com.aadevelopers.cashkingapp.csm.model;

public class GameModel {
    Integer id;
    String title;
    String image;
    String gamePoint;
    String gameTime;
    int viewType = 0;

    public GameModel() {
    }

    public GameModel(Integer id, String title, String image, String game_link, String gamePoints, String gameTime) {
        this.id = id;
        this.title = title;
        this.image = image;
        this.game_link = game_link;
        this.gamePoint = gamePoints;
        this.gameTime = gameTime;
        this.viewType = 0;
    }

    public int getViewType() {
        return viewType;
    }

    public void setViewType(int viewType) {
        this.viewType = viewType;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getGame_link() {
        return game_link;
    }

    public void setGame_link(String game_link) {
        this.game_link = game_link;
    }

    String game_link;

    public String getGamePoint() {
        return gamePoint;
    }

    public void setGamePoint(String gamePoint) {
        this.gamePoint = gamePoint;
    }

    public String getGameTime() {
        return gameTime;
    }

    public void setGameTime(String gameTime) {
        this.gameTime = gameTime;
    }
}
