package com.technopark.bulat.advandroidhomework2.ui;


import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.technopark.bulat.advandroidhomework2.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 */
public class RegisterFragment extends Fragment {


    public RegisterFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_register, container, false);
    }

    public String prepareRegisterRequestString(String login, String password, String nickname) {
        Map<String, String> data = new HashMap<>();
        data.put("login", login);
        data.put("pass", password);
        data.put("nickname", nickname);

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("action", "register");
            jsonObject.put("data", new JSONObject(data));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject.toString();
    }
}
