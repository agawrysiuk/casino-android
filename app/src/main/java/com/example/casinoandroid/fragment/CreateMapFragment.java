package com.example.casinoandroid.fragment;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.example.casinoandroid.R;
import com.example.casinoandroid.data.Database;
import com.example.casinoandroid.model.FloorTile;
import com.example.casinoandroid.model.GameManager;
import com.example.casinoandroid.model.GameManagerImpl;
import com.example.casinoandroid.model.GameMap;
import com.otaliastudios.zoom.ZoomLayout;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.SaveCallback;
import com.shashank.sony.fancytoastlib.FancyToast;

import java.io.IOException;


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
                //todo implement saving
//                final ParseObject mapToSend = new ParseObject("Map");
//                mapToSend.put("map",gameManager.getMap());
//                mapToSend.saveInBackground(new SaveCallback() {
//                    @Override
//                    public void done(ParseException e) {
//                        if (e == null) {
//                            FancyToast.makeText(getContext(),"Map saved successfully!", Toast.LENGTH_SHORT,FancyToast.SUCCESS,false).show();
//                            btnSaveMap.setEnabled(false);
//                        } else {
//                            FancyToast.makeText(getContext(),"Oops! Map couldn't be saved!", Toast.LENGTH_SHORT,FancyToast.ERROR,false).show();
//                        }
//                    }
//                });
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
