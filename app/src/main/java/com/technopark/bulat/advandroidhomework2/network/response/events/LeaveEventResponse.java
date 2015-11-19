package com.technopark.bulat.advandroidhomework2.network.response.events;

import android.util.Log;

import com.technopark.bulat.advandroidhomework2.models.User;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by bulat on 15.11.15.
 */
public class LeaveEventResponse {
    private static final String LOG_TAG = "Message: leaveEvent";
    private User user;

    public LeaveEventResponse(JSONObject jsonData) {
        Log.d(LOG_TAG, jsonData.toString());
        try {
            channelId = jsonData.getString("chid");
            user = new User();
            user.setId(jsonData.getString("uid"));
            user.setNickname(jsonData.getString("nick"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private String channelId;

    public User getUser() {
        return user;
    }

    public String getChannelId() {
        return channelId;
    }
}
