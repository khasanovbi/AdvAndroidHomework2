package com.technopark.bulat.advandroidhomework2.network.response.messages;

import android.util.Log;

import com.technopark.bulat.advandroidhomework2.network.response.ResponseMessage;

import org.json.JSONException;
import org.json.JSONObject;

public class Auth {
    private static final String LOG_TAG = "Message: auth";
    private int status;
    private String error;
    private String sid;
    private String cid;

    public int getStatus() {
        return status;
    }

    public String getError() {
        return error;
    }

    public String getSid() {
        return sid;
    }

    public String getCid() {
        return cid;
    }

    public void parse(JSONObject jsonData) {
        Log.d(LOG_TAG, jsonData.toString());
        try {

            status = jsonData.getInt("status");
            if (status == 0) {
                sid = jsonData.getString("sid");
                cid = jsonData.getString("cid");
            } else {
                error = jsonData.getString("error");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}