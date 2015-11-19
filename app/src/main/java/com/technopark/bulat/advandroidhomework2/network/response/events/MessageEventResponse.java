package com.technopark.bulat.advandroidhomework2.network.response.events;

import android.util.Log;

import com.technopark.bulat.advandroidhomework2.models.Message;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by bulat on 15.11.15.
 */
public class MessageEventResponse {
    private static final String LOG_TAG = "Message: messageEvent";
    private String channelId;
    private Message message;

    public MessageEventResponse(JSONObject jsonData) {
        Log.d(LOG_TAG, jsonData.toString());
        try {
            channelId = jsonData.getString("chid");
            message = new Message();
            message.setAuthorId(jsonData.getString("from"));
            message.setAuthorNickname(jsonData.getString("nick"));
            message.setText(jsonData.getString("body"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public String getChannelId() {
        return channelId;
    }

    public Message getMessage() {
        return message;
    }
}
