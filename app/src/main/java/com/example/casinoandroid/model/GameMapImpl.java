package com.example.casinoandroid.model;

import android.util.Log;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class GameMapImpl implements GameMap {
    private FloorTile[][] gameMap;
    private List<FloorTile> fillerList;
    private boolean finished;
    private final int MAP_WIDTH = 40;
    private final int MAP_HEIGHT = 40;
    private final int FIRST_TILE_X = 10;
    private final int FIRST_TILE_Y = 20;

    public GameMapImpl() {
        this.gameMap = new FloorTile[MAP_HEIGHT][MAP_WIDTH];
        this.finished = false;
        this.fillerList = new ArrayList<>();
    }

    @Override
    public boolean addFloorTile(FloorTile newFloorTile) {
        //0. we start with a unique tale
        //1. if there is a unique tale, we check where there is a filler tile on the board
        //2. we try to add a tile to this exit
        //3. whether it happens or not, we return true or false;
        //3a. if it's true, we also connect two tales to each other
//        log.info("addFloorTile() started. newFloorTile = {}", newFloorTile.toString());
        if (fillerList.isEmpty() && !finished) {
            if(!Arrays.deepEquals(gameMap, new FloorTile[MAP_HEIGHT][MAP_WIDTH])) {
                //game over
                finished = true;
                return false;
            }
            //here is a code for the opening tile
//            log.info("Adding floorTile as the opening tile.");
            gameMap[FIRST_TILE_X][FIRST_TILE_Y] = newFloorTile;
            newFloorTile.setCoordinates(FIRST_TILE_X, FIRST_TILE_Y);

            addFillerTiles(newFloorTile);
            return true;
        } else {
            //todo add here some check to the boolean field if it's true for a special tile needed
            //todo if true, we need to wait for this special tile
            //here is the code for the rest of the mapping
//            log.info("Trying to find a filler tile.");
            if (fillerList.isEmpty()) {
                //there is no more tiles with open exits
                Log.i("INFO","No filler tile found. Map is completed.");
                return false;
            }
            FloorTile filler = fillerList.get(0); //we pick the first filler we get
//            log.info("Filler tile found. fillerTile at {}", filler.getCoordinates());
            int rotated = 0;
            do {
                //here we add our tile on top of filler if it has the same exit
                //else, we rotate and try it again
                //if it fits, we check the rules, add it and return true
                //if it doesn't end after three tries, we return false;
//                log.info("Checking for validity. Rotation = {}",rotated*90);
                Exit exitToDelete = checkExits(newFloorTile, filler);
                if (exitToDelete != null) {
//                    log.info("exitToDelete = {}, not null. Checking other.",exitToDelete);
                    if(!newFloorTile.getName().equals("Straight Corridor") && !newFloorTile.getName().equals("Dead End")) {
                        //it's a unique tile
                        if (doUniqueTilesCollide(filler.getCoordinates(),exitToDelete)) {
                            return false;
                        }
                    }
                    newFloorTile.deleteExit(exitToDelete);
                    if (newFloorTile.getName().equals("L-Shape Tile") || newFloorTile.getName().equals("T-Shape Tile") || newFloorTile.getName().equals("CrossRoad")) {
                         //we need to delete an exit that connect to the other tile first
                        if (doExitsLeadToOtherTiles(newFloorTile, filler.getCoordinates())) {
                            newFloorTile.setOneExit(exitToDelete.getPosition());
                            return false;
                        }
                    }
                    int x = filler.getCoordinates().getX();
                    int y = filler.getCoordinates().getY();
                    gameMap[x][y] = newFloorTile;
                    newFloorTile.setCoordinates(x, y);
                    newFloorTile.setRotate(rotated*90);
                    fillerList.remove(0);
                    addFillerTiles(newFloorTile);
                    checkIfOver();
                    return true;
                }
                newFloorTile.rotate();
                rotated++;
            } while (rotated < 4);
            //if it doesn't fit after rotating it three times, we exit the method
            return false;
        }
    }

    private void checkIfOver() {
        if(fillerList.isEmpty()) {
            this.finished = true;
        }
    }

    private boolean doExitsLeadToOtherTiles(FloorTile newFloorTile, Coordinates coordinates) {
//        log.info("doExitsLeadToOtherTiles() started.");
        Exit[] exits = newFloorTile.getExits();
        //we check every exit
        for(Exit exit : exits) {
            if(exit!=null){
                //if it's not null, we go in the given direction and see if we stumble upon some tile
//                log.info("newFloorTile has an exit = {}",exit);
                try {
                    int x = coordinates.getX();
                    int y = coordinates.getY();
                    for(;;){
                        x = x+exit.getX();
                        y = y+exit.getY();
                        FloorTile checkedTile = gameMap[x][y];
                        if(checkedTile != null) {
                            return true;
                        }
                    }
                } catch (ArrayIndexOutOfBoundsException e) {
//                    log.info("Reached the end of the map with no tiles on the way.");
                }
            }
        }
        return false;
    }

    private boolean doUniqueTilesCollide(Coordinates coordinates,Exit exit) {
//        log.info("doUniqueTilesCollide() started.");
        int x = coordinates.getX() + exit.getX();
        int y = coordinates.getY() + exit.getY();
        FloorTile nearbyTile = gameMap[x][y];
        if (nearbyTile == null) {
            Log.i("INFO","Something went wrong when looking for the nearbyTile!");
            return true;
        }
        if(!nearbyTile.getName().equals("Straight Corridor") && !nearbyTile.getName().equals("Dead End")) {
//            log.info("Two unique tiles next to each other. Not a valid tile.");
            return true;
        }
        //else the nearby tile is not unique and we are good to go
        return false;
    }

    private Exit checkExits(FloorTile newFloorTile, FloorTile filler) {
//        log.info("checkExits() started.");
        for (int i = 0; i < 4; i++) {
            if (newFloorTile.getExits()[i] != null && filler.getExits()[i] != null) {
                //there is an exit that will connect to the existing tile
//                log.info("Exits match.");
                return filler.getExits()[i];
            }
        }
//        log.info("Exits don't match.");
        //there is no way to connect those tiles
        return null;
    }

    private void addFillerTiles(FloorTile floorTile) {
        Exit[] exits = floorTile.getExits();
        for (int i = 0; i < 4; i++) {
            //if there is an exit option, we add a filler tile
            //which needs to be replaced with an actual tile in the future
            if (exits[i] != null) {
                FloorTile fillerTale =
                        new FloorTileImpl(-1, FillerTile.FILLER_TILE_NAME)
                                .setCoordinates(
                                        floorTile.getCoordinates().getX() + exits[i].getX(),
                                        floorTile.getCoordinates().getY() + exits[i].getY())
                                .setOneExit(i)
                                .rotate()
                                .rotate();
                fillerList.add(fillerTale);
            }
        }
    }

    @Override
    public boolean isValid() {
        //1. all tiles need to be connected;
        //2. tiles not connected can't be placed next to each other
        return false;
    }

    @Override
    public boolean isFinished() {
        if (finished) {
            return true;
        }
        //else check:
        //it's finished when it's valid and
        //tiles don't have open exits
        return false;
    }

    //for testing purposes
    public void printMap() {
        for(int i = 0; i<MAP_HEIGHT;i++) {
            for(int j = 0; j<MAP_WIDTH; j++) {
                FloorTile floorTile = gameMap[i][j];
                if(floorTile==null) {
                    System.out.print("[  ]");
                } else {
                    System.out.print("[" + String.format("%02d", floorTile.getId())+"]");
                }
            }
            System.out.print("\n");
        }
    }


    @Override
    public FloorTile[][] getGameMap() {
        return gameMap;
    }

    @Override
    public List<FloorTile> getFillerList() {
        return fillerList;
    }
}
