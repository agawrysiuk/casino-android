package com.example.huntandroid.data;

import android.content.Context;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.example.huntandroid.model.FloorTile;
import com.example.huntandroid.model.GameMap;
import com.example.huntandroid.model.GameMapImpl;
import com.parse.ParseObject;

import java.util.HashMap;
import java.util.Map;

public class Service {
    private static Service instance = null;

    private Service() {
    }

    public static Service getInstance() {
        if (instance == null) {
            instance = new Service();
        }
        return instance;
    }

    public TableLayout printMapOnTheLayout(Context context, GameMap gameMap) {
        TableLayout tableLayout = new TableLayout(context);
        for(FloorTile[] row : gameMap.getGameMap()) {
            TableRow tableRow = new TableRow(context);
            for (FloorTile floorTile : row) {
                if (floorTile == null){
                    TextView textView = new TextView(context);
                    textView.setText("");
                    tableRow.addView(textView);
                } else {
                    ImageView imageView = new ImageView(context);
                    imageView.setImageBitmap(Database.getDatabase().getImage(context,floorTile.getName()));
                    imageView.setRotation((float) floorTile.getRotate());
                    tableRow.addView(imageView);
                }
            }
            tableLayout.addView(tableRow);
        }
        return tableLayout;
    }

    public ParseObject parseMapToObject(GameMap gameMap) {
        // == transform map ==
        FloorTile[][] floorTiles = gameMap.getGameMap();
        Map<String, String> tilesToSend = new HashMap<>();
        Map<String, Double> rotationToSend = new HashMap<>();
        for (int i = 0; i < floorTiles.length; i++) {
            for (int j = 0; j < floorTiles[i].length; j++) {
                if(floorTiles[i][j] != null) {
                    String keyMap = i + "" + j;
                    tilesToSend.put(keyMap, floorTiles[i][j].getName());
                    rotationToSend.put(keyMap,floorTiles[i][j].getRotate());
                }
            }
        }

        // == get parseobject ==
        final ParseObject object = new ParseObject("Map");
        object.put("tiles", tilesToSend);
        object.put("rotation",rotationToSend);
        return object;
    }

    public GameMap parseObjectToMap(ParseObject parseObject) {
        //todo implement parsing object
        GameMap gameMap = new GameMapImpl();
        return new GameMapImpl();
    }

}
