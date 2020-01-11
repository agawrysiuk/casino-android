package com.example.casinoandroid.fragment;


import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.casinoandroid.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class ViewMapsFragment extends Fragment {


    public ViewMapsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_view_maps, container, false);
    }

}
