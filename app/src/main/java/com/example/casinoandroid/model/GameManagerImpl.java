package com.example.casinoandroid.model;

import android.util.Log;

import com.example.casinoandroid.data.Database;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.*;

public class GameManagerImpl implements GameManager {

    private GameMap gameMap;
    private List<FloorTile> tiles;
    private Random random;

    public GameManagerImpl() {
        this.tiles = new ArrayList<>();
    }

    /*    14x Straight Corridors    id 1-14
      8x  L-Shape Tiles             id 15-22
      8x  T-Shape Tiles             id 23-30
      12x Crossroads                id 31-42
      12x Dead Ends                 id 43-54
      12x Air Vent Tiles
      5   Rooms (Escape Pod / Laboratory,
            Armoury, Bridge, Hibernation Room,
            Engine Room)            id 55-59
      12  Door Tiles*/
    @Override
    public GameManager loadTiles() throws IOException {
        tiles.clear();
        BufferedReader br = Database.getDatabase().getData();
        String currentLine;
        while ((currentLine = br.readLine()) != null) {
            String[] array = currentLine.split(",");
            FloorTile floorTile =
                    new FloorTileImpl(Integer.parseInt(array[0]), array[1])
                    .setExits(
                            Integer.parseInt(array[2]),
                            Integer.parseInt(array[3]),
                            Integer.parseInt(array[4]),
                            Integer.parseInt(array[5]))
                    .setRotate(Double.parseDouble(array[6]));
            tiles.add(floorTile);
        }
        return this;
    }

    @Override
    public GameMap createMap() {
        this.gameMap = new GameMapImpl();
        //1. check if it's empty
        //  - yes -> put first tile, a Dead End or a Special tile, loop;
        //  - no -> move to point 2;
        //2. check if map is finished
        //  - yes -> end it;
        //  - no -> go to point 3
        //3. try to add a tile
        if (random == null) {
            random = new Random();
        }
        int i = 0;
        while (!gameMap.isFinished()) {
            i++;
            if(i%10==0) {
                Log.i("INFO","Iteration number = "+i);

            }

            FloorTile tileToAdd;

            int tileNumber;

            if (gameMap.getFillerList().isEmpty()) {//we need to choose a starting tile if it's an empty map
                tileNumber = tiles.size() - 1 - random.nextInt(5);
                tileToAdd = tiles.get(tileNumber);
            } else {
                tileNumber = random.nextInt(tiles.size()-1);
                tileToAdd = tiles.get(tileNumber);
                if(tileToAdd==null) {
                    continue;
                } else if(tiles.size()>35){
                    if(tileToAdd.getName().equals("Dead End")) {
//                        log.info("We reached dead end too soon. Trying another tile.");
                        continue;
                    } else if(tileToAdd.getId()>=55) {
//                        log.info("We reached unique tile too soon. Trying another tile.");
                        continue;
                    }

                }

            }
            if (gameMap.addFloorTile(tileToAdd)) { //we try adding this tile
                tiles.remove(tileNumber);
            } else {
                tileToAdd.goBackToDefault();
            }
            if(i==1000) { //we don't want it to take too long, so we break it early; if it doesn't finish by 1000 iterations, it won't at all
                break;
            }
        }
        Log.i("INFO","Map created.");
        gameMap.printMap();
        return gameMap;
    }

    public FloorTile getTile(int id) {
        return tiles.get(id);
    }


    @Override
    public List<FloorTile> getTiles() {
        return tiles;
    }
}
