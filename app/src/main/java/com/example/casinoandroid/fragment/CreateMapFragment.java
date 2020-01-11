package com.example.casinoandroid.fragment;


import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.casinoandroid.R;
import com.example.casinoandroid.model.GameManager;
import com.example.casinoandroid.model.GameManagerImpl;
import com.example.casinoandroid.model.GameMap;

import java.io.IOException;


/**
 * A simple {@link Fragment} subclass.
 */
public class CreateMapFragment extends Fragment {

    private GameManager gameManager;
    private Button btnCreateMap;

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
            }
        });
        return view;
    }

    private void printMapOnTheLayout(GameMap gameMap) {

    }

}
