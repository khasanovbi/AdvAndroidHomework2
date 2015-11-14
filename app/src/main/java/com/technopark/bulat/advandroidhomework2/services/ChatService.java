package com.technopark.bulat.advandroidhomework2.services;

import android.app.IntentService;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

/**
 * Created by bulat on 11.11.15.
 */
public class ChatService extends IntentService {
    public ChatService() {
        super("ChatService");
    }

    void connect() {

    }

    @Override
    protected void onHandleIntent(Intent intent) {
        String request = intent.getStringExtra("request");
    }
}