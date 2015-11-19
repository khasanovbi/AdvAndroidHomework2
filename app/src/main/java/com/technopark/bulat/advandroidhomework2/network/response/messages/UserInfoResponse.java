package com.technopark.bulat.advandroidhomework2.network.response.messages;

import android.util.Log;

import com.technopark.bulat.advandroidhomework2.models.User;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by bulat on 15.11.15.
 */
public class UserInfoResponse {
    private static final String LOG_TAG = "Message: userInfo";
    private int status;
    private String error;
    private User user;

    public UserInfoResponse(JSONObject jsonData) {
        Log.d(LOG_TAG, jsonData.toString());
        try {
            status = jsonData.getInt("status");
            if (status == 0) {
                user = new User();
                user.setNickname(jsonData.getString("nick"));
                user.setStatus(jsonData.getString("user_status"));
            } else {
                error = jsonData.getString("error");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public int getStatus() {
        return status;
    }

    public String getError() {
        return error;
    }

    public User getUser() {
        return user;
    }
}
