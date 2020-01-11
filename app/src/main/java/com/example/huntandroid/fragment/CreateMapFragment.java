package com.example.huntandroid.fragment;


import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.example.huntandroid.R;
import com.example.huntandroid.data.Database;
import com.example.huntandroid.model.FloorTile;
import com.example.huntandroid.model.GameManager;
import com.example.huntandroid.model.GameManagerImpl;
import com.example.huntandroid.model.GameMap;
import com.otaliastudios.zoom.ZoomLayout;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.SaveCallback;
import com.shashank.sony.fancytoastlib.FancyToast;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;


/**
 * A simple {@link Fragment} subclass.
 */
public class CreateMapFragment extends Fragment {

    private GameManager gameManager;
    private Button btnCreateMap, btnSaveMap;
    private ZoomLayout zoomLayout;

    public CreateMapFragment() {
        if (gameManager == null) {
            gameManager = new GameManagerImpl();
        }
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_create_map, container, false);
        btnSaveMap = view.findViewById(R.id.btnSaveMap);
        btnSaveMap.setEnabled(false);
        btnSaveMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMapToServer();
            }
        });
        btnCreateMap = view.findViewById(R.id.btnCreateMap);
        btnCreateMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    gameManager.loadTiles();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                printMapOnTheLayout(gameManager.createMap());
                btnSaveMap.setEnabled(true);
            }
        });
        zoomLayout = view.findViewById(R.id.zoomLayout);
        return view;
    }

    private void sendMapToServer() {
        GameMap gameMap = gameManager.getMap();
        if (gameMap != null) {
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

            final ParseObject object = new ParseObject("Map");
            object.put("tiles", tilesToSend);
            object.put("rotation",rotationToSend);
            object.saveInBackground(new SaveCallback() {
                @Override
                public void done(ParseException e) {
                    if (e == null) {
                        FancyToast.makeText(getContext(), "Map saved successfully!", Toast.LENGTH_SHORT, FancyToast.SUCCESS, false).show();
                        btnSaveMap.setEnabled(false);
                    } else {
                        FancyToast.makeText(getContext(), "Oops! Map couldn't be saved!", Toast.LENGTH_SHORT, FancyToast.ERROR, false).show();
                    }
                }
            });
        }
    }

    private void printMapOnTheLayout(GameMap gameMap) {
        zoomLayout.removeAllViews();
        TableLayout tableLayout = new TableLayout(getContext());
        for(FloorTile[] row : gameMap.getGameMap()) {
            TableRow tableRow = new TableRow(getContext());
            for (FloorTile floorTile : row) {
                if (floorTile == null){
                    TextView textView = new TextView(getContext());
                    textView.setText("");
                    tableRow.addView(textView);
                } else {
                    ImageView imageView = new ImageView(getContext());
                    imageView.setImageBitmap(Database.getDatabase().getImage(getContext(),floorTile.getName()));
                    imageView.setRotation((float) floorTile.getRotate());
                    tableRow.addView(imageView);
                }
            }
            tableLayout.addView(tableRow);
        }
        zoomLayout.addView(tableLayout);
    }

}
