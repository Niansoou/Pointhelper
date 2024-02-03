package com.example.pointhelper.ui.theme;

import java.io.Serializable;

public class Battle implements Serializable {

    private int Battleid;
    private  int betnum;
    public int getBetnum() {
        return betnum;
    }

    public void setBetnum(int betnum) {
        this.betnum = betnum;
    }
    public int getBattleid() {
        return Battleid;
    }
    public void setBattleid(int battleid) {
        Battleid = battleid;
    }
}
