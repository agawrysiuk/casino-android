package com.example.huntandroid.data;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.example.huntandroid.R;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public final class Database {

    private static final String TILES_PATH = "res/raw/tiles.txt";

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

    public Bitmap getImage(Context context, String name) {
/*        14x Straight Corridors    id 1-14
        8x  L-Shape Tiles             id 15-22
        8x  T-Shape Tiles             id 23-30
        12x Crossroads                id 31-42
        12x Dead Ends                 id 43-54
        12x Air Vent Tiles
        5   Rooms (Escape Pod / Laboratory,
                Armoury, Bridge, Hibernation Room,
                Engine Room)            id 55-59*/
        int drawable = 0;
        switch (name) {
            case "Straight Corridor":
                drawable = R.drawable.straight_corridor;
                break;
            case "L-Shape Tile_R":
                drawable = R.drawable.l_shape_tile_right;
                break;
            case "L-Shape Tile_L":
                drawable = R.drawable.l_shape_tile_left;
                break;
            case "T-Shape Tile_U":
                drawable = R.drawable.t_shape_tile_up;
                break;
            case "T-Shape Tile_D":
                drawable = R.drawable.t_shape_tile_down;
                break;
            case "CrossRoad":
                drawable = R.drawable.crossroad;
                break;
            case "Dead End":
                drawable = R.drawable.dead_end;
                break;
            case "Laboratory":
                drawable = R.drawable.laboratory;
                break;
            case "Armoury":
                drawable = R.drawable.armoury;
                break;
            case "Bridge":
                drawable = R.drawable.bridge;
                break;
            case "Hibernation Room":
                drawable = R.drawable.hibernation_room;
                break;
            case "Engine Room":
                drawable = R.drawable.engine_room;
                break;
            default:
                drawable = R.drawable.dead_end;
                break;
        }
        return BitmapFactory.decodeResource(context.getResources(), drawable);
    }
}
