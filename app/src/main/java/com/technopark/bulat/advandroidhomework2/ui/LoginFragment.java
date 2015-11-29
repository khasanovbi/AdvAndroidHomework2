package com.technopark.bulat.advandroidhomework2.ui;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.technopark.bulat.advandroidhomework2.R;
import com.technopark.bulat.advandroidhomework2.models.GlobalUserIds;
import com.technopark.bulat.advandroidhomework2.network.request.messages.AuthRequest;
import com.technopark.bulat.advandroidhomework2.network.request.messages.UserInfoRequest;
import com.technopark.bulat.advandroidhomework2.network.response.RawResponse;
import com.technopark.bulat.advandroidhomework2.network.response.messages.AuthResponse;
import com.technopark.bulat.advandroidhomework2.network.response.messages.UserInfoResponse;
import com.technopark.bulat.advandroidhomework2.network.socket.GlobalSocket;
import com.technopark.bulat.advandroidhomework2.network.socket.socketObserver.Observer;

public class LoginFragment extends Fragment implements OnClickListener, Observer {
    private SharedPreferences mSharedPreferences;
    private EditText mLoginEditText;
    private EditText mPasswordEditText;

    public LoginFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_login, container, false);

        mLoginEditText = (EditText) rootView.findViewById(R.id.login_edit_text);
        mPasswordEditText = (EditText) rootView.findViewById(R.id.password_edit_text);
        mSharedPreferences = getActivity().getSharedPreferences("auth_settings", Context.MODE_PRIVATE);
        mLoginEditText.setText(mSharedPreferences.getString("login", ""));
        mPasswordEditText.setText(mSharedPreferences.getString("password", ""));

        rootView.findViewById(R.id.login_button).setOnClickListener(this);
        return rootView;
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.login_button: {
                Editor sharedPreferencesEditor = mSharedPreferences.edit();
                String login = mLoginEditText.getText().toString();
                String password = mPasswordEditText.getText().toString();
                sharedPreferencesEditor.putString("login", login);
                sharedPreferencesEditor.putString("password", password);
                sharedPreferencesEditor.apply();
                GlobalSocket.getInstance().performAsyncRequest(new AuthRequest(login, password));
            }
            case R.id.register_button: {
                Fragment registerFragment = getActivity().getSupportFragmentManager().findFragmentById(R.id.fragment_register);
                if (registerFragment == null) {
                    registerFragment = new RegisterFragment();
                }
                getActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragments_container, registerFragment)
                        .commit();
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        /* Subscribe to socket messages */
        GlobalSocket.getInstance().registerObserver(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        /* Unsubscribe from socket messages */
        GlobalSocket.getInstance().removeObserver(this);
    }

    @Override
    public void handleResponseMessage(RawResponse rawResponse) {
        switch (rawResponse.getAction()) {
            case "auth":
                final AuthResponse authResponse = new AuthResponse(rawResponse.getJsonData());
                int status = authResponse.getStatus();
                if (status == 0) {
                    GlobalUserIds.getInstance().cid = authResponse.getCid();
                    GlobalUserIds.getInstance().sid = authResponse.getSid();
                    GlobalSocket.getInstance().performAsyncRequest(
                            new UserInfoRequest(
                                    GlobalUserIds.getInstance().cid,
                                    GlobalUserIds.getInstance().cid,
                                    GlobalUserIds.getInstance().sid
                            )
                    );
                } else {
                    getActivity().runOnUiThread(new Runnable() {
                        public void run() {
                            Toast.makeText(getActivity().getBaseContext(), authResponse.getError(), Toast.LENGTH_LONG).show();
                        }
                    });
                    switch (status) {
                        case 7:
                            Fragment registerFragment = getActivity().getSupportFragmentManager().findFragmentById(R.id.fragment_register);
                            if (registerFragment == null) {
                                registerFragment = new RegisterFragment();
                            }
                            getActivity().getSupportFragmentManager()
                                    .beginTransaction()
                                    .replace(R.id.fragments_container, registerFragment)
                                    .commit();
                            break;
                        default:
                            Fragment loginFragment = getActivity().getSupportFragmentManager().findFragmentById(R.id.fragment_login);
                            if (loginFragment == null) {
                                loginFragment = new LoginFragment();
                            }
                            getActivity().getSupportFragmentManager()
                                    .beginTransaction()
                                    .replace(R.id.fragments_container, loginFragment)
                                    .commit();
                            break;
                    }
                }
                break;
            case "userinfo":
                final UserInfoResponse userInfoResponse = new UserInfoResponse(rawResponse.getJsonData());
                if (userInfoResponse.getStatus() == 0) {
                    getActivity().runOnUiThread(new Runnable() {
                        public void run() {
                            String userStatus = userInfoResponse.getUser().getStatus();
                            String nickname = userInfoResponse.getUser().getNickname();
                            SharedPreferences.Editor sharedPreferencesEditor = mSharedPreferences.edit();
                            sharedPreferencesEditor.putString("status", userStatus);
                            sharedPreferencesEditor.putString("nickname", nickname);
                            sharedPreferencesEditor.apply();
                            DrawerLayout drawerLayout = ((MainActivity) getActivity()).getDrawerLayout();
                            ((TextView) drawerLayout.findViewById(R.id.nickname)).setText(nickname);
                            ((TextView) drawerLayout.findViewById(R.id.status)).setText(userStatus);
                        }
                    });
                    Fragment channelListFragment = getActivity().getSupportFragmentManager().findFragmentById(R.id.fragment_channel_list);
                    if (channelListFragment == null) {
                        channelListFragment = new ChannelListFragment();
                    }
                    getActivity().getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.fragments_container, channelListFragment)
                            .commit();
                } else {
                    getActivity().runOnUiThread(new Runnable() {
                        public void run() {
                            Toast.makeText(getActivity().getBaseContext(), userInfoResponse.getError(), Toast.LENGTH_LONG).show();
                        }
                    });
                }
                break;
        }
    }
}
