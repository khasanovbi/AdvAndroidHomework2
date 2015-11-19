package com.technopark.bulat.advandroidhomework2.network.response.messages;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by bulat on 15.11.15.
 */
public class LeaveChatResponse {
    private static final String LOG_TAG = "Message: leaveChat";
    private int status;
    private String error;

    public LeaveChatResponse(JSONObject jsonData) {

        Log.d(LOG_TAG, jsonData.toString());
        try {
            status = jsonData.getInt("status");
            if (status != 0) {
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
}
