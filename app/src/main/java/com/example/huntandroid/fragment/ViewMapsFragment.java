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
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.shashank.sony.fancytoastlib.FancyToast;

import java.util.List;


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
        return view;
    }

    @Override
    public void onClick(View v) {
        layoutForMaps.removeAllViews();
        ParseQuery<ParseObject> queryAll = ParseQuery.getQuery(Service.OBJECT_CLASSNAME);
        queryAll.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if (objects.size() > 0 && e == null) {
                    for (ParseObject object : objects) {
                        GameMap gameMap = Service.getInstance().parseObjectToMap(object);
                        if (gameMap != null) {
                            TableLayout tableLayout = Service.getInstance().printMapOnTheTableLayout(getContext(), gameMap);
                            ImageView imageView = Service.getInstance().createImageViewOutOfTableLayout(getContext(),tableLayout);
                            layoutForMaps.addView(imageView);
                        } else {
                            FancyToast.makeText(getContext(), "Oops! We encountered some error!", Toast.LENGTH_SHORT, FancyToast.ERROR, false).show();
                        }
                    }
                } else {
                    FancyToast.makeText(getContext(), "No maps to download", Toast.LENGTH_SHORT, FancyToast.INFO, false).show();
                }
            }
        });
    }
}
