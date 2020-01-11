package com.example.huntandroid.model;

import java.io.Serializable;

public enum Exit implements Serializable {
    TOP_EXIT(0,-1,0),
    RIGHT_EXIT(1,0,1),
    BOT_EXIT(2,1,0),
    LEFT_EXIT(3,0,-1);

    private int position;
    private int x;
    private int y;

    Exit(int position, int x, int y) {
        this.position = position;
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getPosition() {
        return position;
    }
}
