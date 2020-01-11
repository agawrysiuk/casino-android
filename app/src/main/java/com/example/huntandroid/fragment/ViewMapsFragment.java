package com.example.huntandroid.fragment;


import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import com.example.huntandroid.R;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * A simple {@link Fragment} subclass.
 */
public class ViewMapsFragment extends Fragment implements View.OnClickListener {

    private LinearLayout layoutForMaps;
    private Button btnRefresh;

    public ViewMapsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_view_maps, container, false);
        btnRefresh = view.findViewById(R.id.btnRefresh);
        btnRefresh.setOnClickListener(this);
        layoutForMaps = view.findViewById(R.id.layoutForMaps);
        onClick(view);
        return view;
    }

    @Override
    public void onClick(View v) {
        ParseQuery<ParseObject> queryAll = ParseQuery.getQuery("Map");
        queryAll.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if (objects.size() > 0 && e == null) {
                    for (ParseObject object : objects) {
                        Map<String, String> tilesReceived = (Map<String, String>) object.get("tiles");
                        Map<String, Double> rotationReceived = (Map<String, Double>) object.get("rotation");

                    }
                } else {
                    //kod jak jest błąd
                }
            }
        });
    }
}
