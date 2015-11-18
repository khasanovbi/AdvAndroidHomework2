package com.technopark.bulat.advandroidhomework2.ui;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.technopark.bulat.advandroidhomework2.R;
import com.technopark.bulat.advandroidhomework2.asyncTasks.OnPreloadTaskDone;
import com.technopark.bulat.advandroidhomework2.asyncTasks.PreloadTask;
import com.technopark.bulat.advandroidhomework2.models.GlobalUserIds;
import com.technopark.bulat.advandroidhomework2.network.response.ResponseMessage;
import com.technopark.bulat.advandroidhomework2.network.socket.GlobalSocket;
import com.technopark.bulat.advandroidhomework2.network.request.messages.Auth;
import com.technopark.bulat.advandroidhomework2.network.socket.socketObserver.Observer;

public class SplashScreenFragment extends Fragment implements OnPreloadTaskDone, Observer {
    private static final String LOG_TAG = "SplashScreenFragment";
    private PreloadTask mPreloadTask;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_splash_screen, container, false);
    }

    @Override
    public void onResume() {
        super.onResume();
        /* Subscribe to socket messages */
        GlobalSocket.getInstance().registerObserver(this);
        /* Try get credentials from sharedPreferences */
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("auth_settings", Context.MODE_PRIVATE);
        String login = sharedPreferences.getString("login", null);
        String password = sharedPreferences.getString("password", null);
        if (login != null && password != null) {
            GlobalSocket.getInstance().performAsyncRequest(new Auth(login, password));
        } else {
            if (mPreloadTask != null) {
                mPreloadTask.cancel(true);
            }
            mPreloadTask = new PreloadTask(SplashScreenFragment.this);
            mPreloadTask.execute();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mPreloadTask != null) {
            mPreloadTask.cancel(true);
        }
        GlobalSocket.getInstance().removeObserver(this);
    }

    @Override
    public void onPreloadTaskDone() {
        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragments_container, new LoginFragment()).commit();
    }

    @Override
    public void handleResponseMessage(ResponseMessage responseMessage) {
        if (responseMessage.getAction().equals("auth")) {
            final com.technopark.bulat.advandroidhomework2.network.response.messages.Auth auth = new com.technopark.bulat.advandroidhomework2.network.response.messages.Auth();
            auth.parse(responseMessage.getJsonData());
            int status = auth.getStatus();
            if (status == 0) {
                GlobalUserIds.getInstance().cid = auth.getCid();
                GlobalUserIds.getInstance().sid = auth.getSid();
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragments_container, new ChannelListFragment()).commit();
            } else {
                getActivity().runOnUiThread(new Runnable() {
                    public void run() {
                        Toast.makeText(getActivity().getBaseContext(), auth.getError(), Toast.LENGTH_LONG).show();
                    }
                });
                switch (status) {
                    case 7:
                        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragments_container, new RegisterFragment()).commit();
                        break;
                    default:
                        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragments_container, new LoginFragment()).commit();
                        break;
                }
            }
        }
    }
}