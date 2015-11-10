package com.technopark.bulat.advandroidhomework2.ui;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import com.technopark.bulat.advandroidhomework2.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class ChangeContactInfoFragment extends Fragment {


    public ChangeContactInfoFragment() {


        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_change_contact_info, container, false);
    }


}
