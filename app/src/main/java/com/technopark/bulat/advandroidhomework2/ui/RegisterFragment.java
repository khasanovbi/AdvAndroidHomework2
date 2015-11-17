package com.technopark.bulat.advandroidhomework2.ui;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.technopark.bulat.advandroidhomework2.R;
import com.technopark.bulat.advandroidhomework2.network.request.messages.Auth;
import com.technopark.bulat.advandroidhomework2.network.request.messages.Registration;
import com.technopark.bulat.advandroidhomework2.network.response.ResponseMessage;
import com.technopark.bulat.advandroidhomework2.network.socket.GlobalSocket;
import com.technopark.bulat.advandroidhomework2.network.socket.socketObserver.Observer;

/**
 * A simple {@link Fragment} subclass.
 */
public class RegisterFragment extends Fragment implements View.OnClickListener, Observer {
    private SharedPreferences sharedPreferences;
    private EditText mLoginEditText;
    private EditText mPasswordEditText;
    private EditText mNicknameEditText;

    public RegisterFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        GlobalSocket.getInstance().registerObserver(this);
        View rootView = inflater.inflate(R.layout.fragment_register, container, false);
        mLoginEditText = (EditText) rootView.findViewById(R.id.login_edit_text);
        mPasswordEditText = (EditText) rootView.findViewById(R.id.password_edit_text);
        mNicknameEditText = (EditText) rootView.findViewById(R.id.nickname_edit_text);

        sharedPreferences = getActivity().getSharedPreferences("auth_settings", Context.MODE_PRIVATE);
        mLoginEditText.setText(sharedPreferences.getString("login", ""));
        mPasswordEditText.setText(sharedPreferences.getString("password", ""));
        mNicknameEditText.setText(sharedPreferences.getString("nickname", ""));

        rootView.findViewById(R.id.register_button).setOnClickListener(this);
        return rootView;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.register_button: {
                SharedPreferences.Editor sharedPreferencesEditor = sharedPreferences.edit();
                String login = mLoginEditText.getText().toString();
                String password = mPasswordEditText.getText().toString();
                String nickname = mNicknameEditText.getText().toString();
                sharedPreferencesEditor.putString("login", login);
                sharedPreferencesEditor.putString("password", password);
                sharedPreferencesEditor.putString("nickname", nickname);
                sharedPreferencesEditor.apply();
                GlobalSocket.getInstance().performAsyncRequest(new Registration(login, password, nickname));
            }
        }
    }

    @Override
    public void handleResponseMessage(ResponseMessage responseMessage) {
        if (responseMessage.getAction().equals("register")) {
            final com.technopark.bulat.advandroidhomework2.network.response.messages.Registration registration = new com.technopark.bulat.advandroidhomework2.network.response.messages.Registration();
            registration.parse(responseMessage.getJsonData());
            if (registration.getStatus() == 0) {
                GlobalSocket.getInstance().removeObserver(this);
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragments_container, new ChannelListFragment()).commit();
            } else {
                getActivity().runOnUiThread(new Runnable() {
                    public void run() {
                        Toast.makeText(getActivity().getBaseContext(), registration.getError(), Toast.LENGTH_LONG).show();
                    }
                });
            }
        }
    }
}
