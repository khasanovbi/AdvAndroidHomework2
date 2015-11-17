package com.technopark.bulat.advandroidhomework2.network.response.messages;

import android.util.Log;

import com.technopark.bulat.advandroidhomework2.network.response.ResponseMessage;

import org.json.JSONObject;

/**
 * Created by bulat on 15.11.15.
 */
public class SendMessage {
    private static final String LOG_TAG = "Message: sendMessage";
    private int status;
    private String error;

    public int getStatus() {
        return status;
    }

    public String getError() {
        return error;
    }

    public void parse(JSONObject jsonData) {
        Log.d(LOG_TAG, jsonData.toString());
        // TODO не описано в задании
    }
}
