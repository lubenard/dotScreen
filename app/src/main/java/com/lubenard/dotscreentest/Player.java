package com.lubenard.dotscreentest;

public class Player {
    private String name;
    private int score;
    private int imageId;
    private char uniqueId;

    public Player(String playerName, int imageRessource, char uniqueId) {
        this.name = playerName;
        this.score = 0;
        this.imageId = imageRessource;
        this.uniqueId = uniqueId;
    }

    public String getPlayerName() {
        return this.name;
    }

    public int getScore() {
        return this.score;
    }

    public void increaseScore() {
        this.score++;
    }

    public int getImageId() {
        return this.imageId;
    }

    public char getUniqueId() {
        return this.uniqueId;
    }

}