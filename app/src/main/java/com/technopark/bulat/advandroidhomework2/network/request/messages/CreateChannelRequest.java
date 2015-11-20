package com.technopark.bulat.advandroidhomework2.network.request.messages;

import com.technopark.bulat.advandroidhomework2.network.request.RequestMessage;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by bulat on 20.11.15.
 */
public class CreateChannelRequest implements RequestMessage {
    private String cid;
    private String sid;
    private String name;
    private String description;

    public CreateChannelRequest(String cid, String sid, String name, String description) {
        this.cid = cid;
        this.sid = sid;
        this.name = name;
        this.description = description;
    }

    @Override
    public String getRequestString() {
        Map<String, String> data = new HashMap<>();
        data.put("cid", cid);
        data.put("sid", sid);
        data.put("name", name);
        data.put("descr", description);
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("action", "createchannel");
            jsonObject.put("data", new JSONObject(data));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject.toString();
    }
}
