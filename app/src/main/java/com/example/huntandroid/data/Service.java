package com.example.huntandroid.data;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.example.huntandroid.model.FloorTile;
import com.example.huntandroid.model.FloorTileImpl;
import com.example.huntandroid.model.GameMap;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.shashank.sony.fancytoastlib.FancyToast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class Service {
    public static final String OBJECT_CLASSNAME = "Map";
    private static final String SEPARATOR = "/";
    private static final String OBJECT_TILES = "tiles";
    private static final String OBJECT_ROTATION = "rotation";
    private static final String OBJECT_WIDTH = "width";
    private static final String OBJECT_HEIGHT = "height";
    private static final String OBJECT_DATE_ID = "dateId";
    private static Service instance = null;
    private static Map<Date,GameMap> maps;

    private Service() {
    }

    public static Service getInstance() {
        if (instance == null) {
            instance = new Service();
            maps = new HashMap<>();
        }
        return instance;
    }

    public TableLayout printMapOnTheTableLayout(Context context, GameMap gameMap) {
        TableLayout tableLayout = new TableLayout(context);
        for (FloorTile[] row : gameMap.getGameMap()) {
            if (isRowEmpty(row)) {
                continue;
            }
            TableRow tableRow = new TableRow(context);
            for (FloorTile floorTile : row) {
                if (floorTile == null) {
                    TextView textView = new TextView(context);
                    textView.setText("");
                    tableRow.addView(textView);
                } else {
                    ImageView imageView = new ImageView(context);
                    imageView.setImageBitmap(Database.getDatabase().getImage(context, floorTile.getName()));
                    imageView.setRotation((float) floorTile.getRotate());
                    tableRow.addView(imageView);
                }
            }
            tableLayout.addView(tableRow);
        }
        return tableLayout;
    }

    private boolean isRowEmpty(Object[] objects) {
        for (Object object : objects) {
            if (object != null) {
                return false;
            }
        }
        return true;
    }

    public ParseObject parseMapToObject(GameMap gameMap) {
        // == transform map ==
        FloorTile[][] floorTiles = gameMap.getGameMap();
        Map<String, String> tilesToSend = new HashMap<>();
        Map<String, Double> rotationToSend = new HashMap<>();
        for (int i = 0; i < floorTiles.length; i++) {
            for (int j = 0; j < floorTiles[i].length; j++) {
                if (floorTiles[i][j] != null) {
                    String keyMap = i + SEPARATOR + j;
                    tilesToSend.put(keyMap, floorTiles[i][j].getName());
                    rotationToSend.put(keyMap, floorTiles[i][j].getRotate());
                }
            }
        }
        // == get parseobject ==
        final ParseObject object = new ParseObject(OBJECT_CLASSNAME);
        object.put(OBJECT_TILES, tilesToSend);
        object.put(OBJECT_ROTATION, rotationToSend);
        object.put(OBJECT_WIDTH, floorTiles[0].length);
        object.put(OBJECT_HEIGHT, floorTiles.length);
        object.put(OBJECT_DATE_ID,new Date(System.currentTimeMillis()));
        return object;
    }

    public GameMap parseObjectToMap(ParseObject object) {
        //== getting object details
        Map<String, String> tilesReceived = (Map<String, String>) object.get(OBJECT_TILES);
        Map<String, Integer> rotationReceived = (Map<String, Integer>) object.get(OBJECT_ROTATION);
        int width = (int) object.get(OBJECT_WIDTH);
        int height = (int) object.get(OBJECT_HEIGHT);

        //create GameMap
        GameMap gameMap = new GameMapForPrinting(width, height);

        // == populate GameMap ==
        for (Map.Entry<String, String> entry : tilesReceived.entrySet()) {
            String coordinates = entry.getKey();
            int i = Integer.parseInt(coordinates.split(SEPARATOR)[0]);
            int y = Integer.parseInt(coordinates.split(SEPARATOR)[1]);
            gameMap.getGameMap()[i][y] = new FloorTileImpl(0, entry.getValue());
        }
        for (Map.Entry<String, Integer> entry : rotationReceived.entrySet()) {
            String coordinates = entry.getKey();
            int i = Integer.parseInt(coordinates.split(SEPARATOR)[0]);
            int y = Integer.parseInt(coordinates.split(SEPARATOR)[1]);
            int rotation = entry.getValue();
            gameMap.getGameMap()[i][y].setRotate(rotation);
        }
        return gameMap;
    }

    public ImageView createImageViewOutOfTableLayout(Context context, TableLayout tableLayout) {
        tableLayout.measure(TableLayout.LayoutParams.WRAP_CONTENT, TableLayout.LayoutParams.WRAP_CONTENT);
        Bitmap b = Bitmap.createBitmap(tableLayout.getMeasuredWidth(), tableLayout.getMeasuredHeight(), Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(b);
        tableLayout.layout(0, 0, tableLayout.getMeasuredWidth(), tableLayout.getMeasuredHeight());
        tableLayout.draw(c);
        ImageView imageView = new ImageView(context);
        imageView.setImageBitmap(b);
        return imageView;
    }

    public Map<Date, GameMap> getMapsFromTheServer() {
        ParseQuery<ParseObject> queryAll = ParseQuery.getQuery(Service.OBJECT_CLASSNAME);
        if(!maps.isEmpty()) {
            Date latest = Collections.max(maps.keySet());
            queryAll.whereGreaterThan(OBJECT_DATE_ID, latest);
        }
        try {
            List<ParseObject> objects = queryAll.find();
            for (ParseObject object : objects) {
                Log.i("MAP", "Map with the date id " + object.getDate(OBJECT_DATE_ID) + " downloaded.");
                GameMap gameMap = parseObjectToMap(object);
                maps.put(object.getDate("dateId"), gameMap);
            }
        } catch (ParseException e) {
            return null;
        }
        return maps;
    }

    public Map<Date, GameMap> getMapsToPrint() {
        return maps;
    }

    private class GameMapForPrinting implements GameMap {

        private FloorTile[][] floorTiles;

        public GameMapForPrinting(int MAP_WIDTH, int MAP_HEIGHT) {
            this.floorTiles = new FloorTile[MAP_HEIGHT][MAP_WIDTH];
        }

        @Override
        public FloorTile[][] getGameMap() {
            return floorTiles;
        }

        @Override
        public boolean addFloorTile(FloorTile floorTile) {
            return false;
        }

        @Override
        public List<FloorTile> getFillerList() {
            return null;
        }

        @Override
        public boolean isFinished() {
            return true;
        }

    }

}
