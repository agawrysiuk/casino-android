package com.example.casinoandroid.model;

public interface FloorTile {

    int getId();

    FloorTile setExits(int... exits);

    FloorTile setOneExit(int position);

    Exit[] getExits();

    boolean deleteExit(Exit exit);

    String getName();

    FloorTile rotate();

    double getRotate();

    FloorTile setRotate(double rotate);

    FloorTile setCoordinates(int x, int y);

    Coordinates getCoordinates();

    void goBackToDefault();

    void setImgPath(String imgPath);

    String getImgPath();
}
