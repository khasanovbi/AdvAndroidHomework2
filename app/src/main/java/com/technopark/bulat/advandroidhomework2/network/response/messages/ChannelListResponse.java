package com.technopark.bulat.advandroidhomework2.network.response.messages;

import android.util.Log;

import com.technopark.bulat.advandroidhomework2.models.Channel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by bulat on 15.11.15.
 */
public class ChannelListResponse {
    private static final String LOG_TAG = "Message: channelList";
    private int status;
    private String error;
    private List<Channel> channels;

    public ChannelListResponse(JSONObject jsonData) {
        Log.d(LOG_TAG, jsonData.toString());
        try {
            status = jsonData.getInt("status");
            if (status == 0) {
                channels = new ArrayList<>();
                JSONArray jsonChannels = jsonData.getJSONArray("channels");
                for (int i = 0; i < jsonChannels.length(); ++i) {
                    JSONObject jsonChannel = (JSONObject) jsonChannels.get(i);
                    Channel channel = new Channel();
                    channel.setId(jsonChannel.getString("chid"));
                    channel.setName(jsonChannel.getString("name"));
                    channel.setDescription(jsonChannel.getString("descr"));
                    channel.setOnlineCount(jsonChannel.getInt("online"));
                    channels.add(channel);
                }
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

    public List<Channel> getChannels() {
        return channels;
    }
}
