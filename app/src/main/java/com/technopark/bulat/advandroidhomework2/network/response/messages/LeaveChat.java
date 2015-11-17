package com.technopark.bulat.advandroidhomework2.network.response.messages;

import android.util.Log;

import com.technopark.bulat.advandroidhomework2.network.response.ResponseMessage;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by bulat on 15.11.15.
 */
public class LeaveChat {
    private static final String LOG_TAG = "Message: leaveChat";
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
        try {
            status = jsonData.getInt("status");
            if (status != 0) {
                error = jsonData.getString("error");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
