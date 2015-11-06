package com.technopark.bulat.advandroidhomework2.ui;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.technopark.bulat.advandroidhomework2.R;

public class ChannelListFragment extends Fragment {


    public ChannelListFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_channel_list, container, false);
    }


}
