package com.example.huntandroid.model;

import java.io.IOException;
import java.util.List;

public interface GameManager {

    GameManager loadTiles() throws IOException;

    GameMap createMap();

    GameMap getMap();
}
