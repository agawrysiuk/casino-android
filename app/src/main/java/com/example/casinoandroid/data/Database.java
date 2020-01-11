package com.example.casinoandroid.data;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;

public final class Database {

    private static final String TILES_PATH = "res/raw/tiles.txt";
    private static final String SMALL_TILES_PATH = "res/raw/small_tiles.txt";

    private Database() {
    }

    public static Database getDatabase() {
        return new Database();
    }

    public BufferedReader getData() {
        return
                new BufferedReader(
                new InputStreamReader(
                        this.getClass().getClassLoader()
                                .getResourceAsStream(TILES_PATH)));
    }
}
