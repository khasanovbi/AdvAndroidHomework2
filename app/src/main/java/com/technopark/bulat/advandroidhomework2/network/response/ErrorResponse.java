package com.technopark.bulat.advandroidhomework2.network.response;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by bulat on 29.11.15.
 */
public class ErrorResponse {
    private static final String LOG_TAG = "ConnectionError";
    private int errorCode;

    public ErrorResponse(JSONObject jsonData) {
        try {
            this.errorCode = jsonData.getInt("errorCode");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.d(LOG_TAG, String.valueOf(errorCode));
    }

    public int getErrorCode() {
        return errorCode;
    }
}
