package com.technopark.bulat.advandroidhomework2.network.response.events;

import android.util.Log;

import com.technopark.bulat.advandroidhomework2.models.Message;
import com.technopark.bulat.advandroidhomework2.network.response.ResponseMessage;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by bulat on 15.11.15.
 */
public class MessageEvent {
    private static final String LOG_TAG = "Message: messageEvent";

    public String getChannelId() {
        return channelId;
    }

    public Message getMessage() {
        return message;
    }

    private String channelId;
    private Message message;

    public void parse(JSONObject jsonData) {
        Log.d(LOG_TAG, jsonData.toString());
        try {
            channelId = jsonData.getString("chid");
            message.setAuthorId(jsonData.getString("from"));
            message.setAuthorNickname(jsonData.getString("nick"));
            message.setText("body");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
