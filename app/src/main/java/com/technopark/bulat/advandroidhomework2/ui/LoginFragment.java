package com.technopark.bulat.advandroidhomework2.ui;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.EditText;

import com.technopark.bulat.advandroidhomework2.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class LoginFragment extends Fragment implements OnClickListener {
    private SharedPreferences sharedPreferences;
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
        sharedPreferences = getActivity().getSharedPreferences("auth_settings", Context.MODE_PRIVATE);
        mLoginEditText.setText(sharedPreferences.getString("login", ""));
        mPasswordEditText.setText(sharedPreferences.getString("password", ""));

        rootView.findViewById(R.id.login_button).setOnClickListener(this);
        return rootView;
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.login_button: {
                Editor sharedPreferencesEditor = sharedPreferences.edit();
                String login = mLoginEditText.getText().toString();
                String password = mPasswordEditText.getText().toString();
                sharedPreferencesEditor.putString("login", login);
                sharedPreferencesEditor.putString("password", password);
                sharedPreferencesEditor.apply();
                //requestTask.execute(prepareLoginRequestString(login, password));
            }
        }
    }

    /*
    public void onRequestResult(String result) {
        try {
            JSONObject jsonObject = new JSONObject(result);
            JSONObject data = jsonObject.getJSONObject("data");
            int status = Integer.valueOf(data.getString("status"));
            if (status != 0) {
                Toast.makeText(getActivity(), data.getString("error"), Toast.LENGTH_SHORT).show();
            } else {
                GlobalUserIds.getInstance().cid = data.getString("cid");
                GlobalUserIds.getInstance().sid = data.getString("sid");
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragments_container, new ChannelListFragment()).commit();
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    */
}
