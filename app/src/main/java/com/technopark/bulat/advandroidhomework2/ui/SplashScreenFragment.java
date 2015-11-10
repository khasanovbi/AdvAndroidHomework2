package com.technopark.bulat.advandroidhomework2.ui;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Toast;

import com.technopark.bulat.advandroidhomework2.asyncTasks.OnPreloadTaskDone;
import com.technopark.bulat.advandroidhomework2.asyncTasks.PreloadTask;
import com.technopark.bulat.advandroidhomework2.socket.RequestListener;
import com.technopark.bulat.advandroidhomework2.socket.SocketRequestTask;
import com.technopark.bulat.advandroidhomework2.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;


public class SplashScreenFragment extends Fragment implements RequestListener, OnPreloadTaskDone {
    private SocketRequestTask mRequestTask;
    private PreloadTask mPreloadTask;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("auth_settings", Context.MODE_PRIVATE);
        String login = sharedPreferences.getString("login", null);
        String password = sharedPreferences.getString("password", null);

        if (login != null && password != null) {
            if (mRequestTask != null) {
                mRequestTask.cancel(true);
            }
            mRequestTask = new SocketRequestTask(SplashScreenFragment.this);
            mRequestTask.execute(LoginFragment.prepareLoginRequestString(login, password));
        } else {
            if (mPreloadTask != null) {
                mPreloadTask.cancel(true);
            }
            mPreloadTask = new PreloadTask(SplashScreenFragment.this);
            mPreloadTask.execute();
        }
        return inflater.inflate(R.layout.fragment_splash_screen, container, false);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mRequestTask != null) {
            mRequestTask.cancel(true);
        }
        if (mPreloadTask != null) {
            mPreloadTask.cancel(true);
        }
    }

    @Override
    public void onRequestResult(String result) {
        Toast.makeText(getActivity(), result, Toast.LENGTH_SHORT).show();
        Log.d("Socket", "Response: " + result);
        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragments_container, new ChannelListFragment()).commit();
    }

    @Override
    public void onRequestError(int errorStringID) {
        Toast.makeText(getActivity(), errorStringID, Toast.LENGTH_SHORT).show();
        Log.d("Socket", "Error: " + getActivity().getResources().getString(errorStringID));
        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragments_container, new LoginFragment()).commit();
    }

    @Override
    public void onPreloadTaskDone() {
        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragments_container, new LoginFragment()).commit();
    }
}
