package com.example.huntandroid.model;


import java.io.Serializable;

public class CoordinatesImpl implements Coordinates, Serializable {
    private final int x;
    private final int y;

    public CoordinatesImpl(int x, int y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public String toString() {
        return "[" + x + "," + y + "]";
    }

    @Override
    public int getX() {
        return x;
    }

    @Override
    public int getY() {
        return y;
    }
}
