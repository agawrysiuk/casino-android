package com.example.casinoandroid.model;

import java.io.IOException;
import java.util.List;

public interface GameManager {

    GameManager loadTiles() throws IOException;

    GameMap createMap();

    List<FloorTile> getTiles();

    FloorTile getTile(int id);
}
