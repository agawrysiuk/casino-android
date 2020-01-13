package com.example.huntandroid.fragment;


import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.Toast;

import com.example.huntandroid.R;
import com.example.huntandroid.data.Service;
import com.example.huntandroid.model.GameMap;
import com.shashank.sony.fancytoastlib.FancyToast;

import java.util.Date;
import java.util.Map;


/**
 * A simple {@link Fragment} subclass.
 */
public class ViewMapsFragment extends Fragment{

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
        btnRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                printMaps(Service.getInstance().getMapsFromTheServer());
            }
        });
        layoutForMaps = view.findViewById(R.id.layoutForMaps);
        printMaps(Service.getInstance().getMapsToPrint());
        return view;
    }

    private void printMaps(Map<Date, GameMap> maps) {
        layoutForMaps.removeAllViews();
        for (GameMap map : maps.values()) {
            if (map != null) {
                TableLayout tableLayout = Service.getInstance().printMapOnTheTableLayout(getContext(), map);
                ImageView imageView = Service.getInstance().createImageViewOutOfTableLayout(getContext(),tableLayout);
                layoutForMaps.addView(imageView);
            } else {
                FancyToast.makeText(getContext(), "Oops! We encountered some error!", Toast.LENGTH_SHORT, FancyToast.ERROR, false).show();
            }
        }
    }


}
