package com.technopark.bulat.advandroidhomework2.ui;


import android.os.Bundle;

import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.technopark.bulat.advandroidhomework2.socket.RequestListener;
import com.technopark.bulat.advandroidhomework2.socket.SocketRequestTask;
import com.technopark.bulat.advandroidhomework2.R;


public class SplashScreenFragment extends Fragment implements RequestListener {
    private SocketRequestTask mRequestTask;

    public SplashScreenFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        if (mRequestTask != null) {
            mRequestTask.cancel(true);
        }
        mRequestTask = new SocketRequestTask(SplashScreenFragment.this);
        mRequestTask.execute("http://mail.ru", "80");
        return inflater.inflate(R.layout.fragment_splash_screen, container, false);
    }

    @Override
    public void onStop() {
        super.onStop();
        mRequestTask.cancel(true);
    }

    @Override
    public void onRequestResult(String result) {
        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragments_container, new LoginFragment()).commit();
    }

    @Override
    public void onRequestError(int errorStringID) {
        Toast.makeText(getActivity(), errorStringID, Toast.LENGTH_SHORT).show();
        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragments_container, new ChatFragment()).commit();
    }
}
