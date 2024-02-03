package com.example.pointhelper.ui.theme;

import java.io.Serializable;

public class Player implements Serializable {
    private int id;
    private int position;
    private String name;
    private int points;
    private int costpoint;
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public int getPoints() {
        return points;
    }
    public void setPoints(int inipoint) {
        this.points = inipoint;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    Player(){

    }
}
