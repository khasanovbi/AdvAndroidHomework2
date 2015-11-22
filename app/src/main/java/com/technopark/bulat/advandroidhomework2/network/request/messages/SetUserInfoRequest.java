package com.technopark.bulat.advandroidhomework2.network.request.messages;

import com.technopark.bulat.advandroidhomework2.network.request.RequestMessage;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by bulat on 20.11.15.
 */
public class SetUserInfoRequest implements RequestMessage {
    private final String cid;
    private final String sid;
    private final String status;

    public SetUserInfoRequest(String cid, String sid, String status) {
        this.cid = cid;
        this.sid = sid;
        this.status = status;
    }

    @Override
    public String getRequestString() {
        Map<String, String> data = new HashMap<>();
        data.put("cid", cid);
        data.put("sid", sid);
        data.put("user_status", status);
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("action", "setuserinfo");
            jsonObject.put("data", new JSONObject(data));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject.toString();
    }
}
