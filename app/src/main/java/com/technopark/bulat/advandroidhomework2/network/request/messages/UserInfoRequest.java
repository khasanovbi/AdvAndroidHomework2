package com.technopark.bulat.advandroidhomework2.network.request.messages;

import com.technopark.bulat.advandroidhomework2.network.request.RequestMessage;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by bulat on 16.11.15.
 */
public class UserInfoRequest implements RequestMessage {
    private final String userId;
    private final String cid;
    private final String sid;

    public UserInfoRequest(String userId, String cid, String sid) {
        this.userId = userId;
        this.cid = cid;
        this.sid = sid;
    }

    @Override
    public String getRequestString() {
        Map<String, String> data = new HashMap<>();
        data.put("user", userId);
        data.put("cid", cid);
        data.put("sid", sid);
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("action", "userinfo");
            jsonObject.put("data", new JSONObject(data));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject.toString();
    }
}
