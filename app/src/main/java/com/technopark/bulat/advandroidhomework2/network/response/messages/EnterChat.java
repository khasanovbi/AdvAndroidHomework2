package com.technopark.bulat.advandroidhomework2.network.response.messages;

import android.util.Log;


import com.technopark.bulat.advandroidhomework2.models.Message;
import com.technopark.bulat.advandroidhomework2.models.User;
import com.technopark.bulat.advandroidhomework2.network.response.ResponseMessage;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by bulat on 15.11.15.
 */
public class EnterChat {
    private static final String LOG_TAG = "Message: enterChat";
    private int status;
    private List<User> users;
    private List<com.technopark.bulat.advandroidhomework2.models.Message> lastMessages;
    private String error;

    public int getStatus() {
        return status;
    }

    public List<User> getUsers() {
        return users;
    }

    public List<Message> getLastMessages() {
        return lastMessages;
    }

    public String getError() {
        return error;
    }

    public void parse(JSONObject jsonData) {
        Log.d(LOG_TAG, jsonData.toString());
        try {
            status = jsonData.getInt("status");
            if (status == 0) {
                users = new ArrayList<>();
                JSONArray jsonUsers = jsonData.getJSONArray("users");
                for (int i = 0; i < jsonUsers.length(); ++i) {
                    JSONObject jsonUser = (JSONObject) jsonUsers.get(i);
                    User user = new User();
                    user.setId(jsonUser.getString("uid"));
                    user.setNickname(jsonUser.getString("nick"));
                    users.add(user);
                }
                lastMessages = new ArrayList<>();
                JSONArray jsonLastMessages = jsonData.getJSONArray("last_msg");
                for (int i = 0; i < jsonLastMessages.length(); ++i) {
                    JSONObject jsonMessage = (JSONObject) jsonLastMessages.get(i);
                    Message message = new Message();
                    message.setId(jsonMessage.getString("mid"));
                    message.setAuthorId(jsonMessage.getString("from"));
                    message.setAuthorNickname(jsonMessage.getString("nick"));
                    message.setText(jsonMessage.getString("body"));
                    message.setTime(new Date(jsonMessage.getLong("time")));
                    lastMessages.add(message);
                    // TODO Temp mock
                    message = new Message();
                    message.setId("123");
                    message.setAuthorId("321");
                    message.setAuthorNickname("alesha");
                    message.setText("Привет лунатикам!");
                    lastMessages.add(message);
                    message = new Message();
                    message.setId("123");
                    message.setAuthorId("123");
                    message.setAuthorNickname("alesha2");
                    message.setText("Привет лунатикам!2");
                    lastMessages.add(message);
                    // TODO End of mock
                }
            } else {
                error = jsonData.getString("error");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
