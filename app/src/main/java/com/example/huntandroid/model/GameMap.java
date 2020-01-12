package com.example.huntandroid.model;

import java.util.List;

public interface GameMap {
    FloorTile[][] getGameMap();

    List<FloorTile> getFillerList();

    boolean addFloorTile(FloorTile floorTile);

    boolean isFinished();

    void printMapToConsole();
}
