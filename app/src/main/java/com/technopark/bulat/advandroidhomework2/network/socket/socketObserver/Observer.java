package com.technopark.bulat.advandroidhomework2.network.socket.socketObserver;

import com.technopark.bulat.advandroidhomework2.network.response.RawResponse;

/**
 * Created by bulat on 17.11.15.
 */
public interface Observer {
    void handleResponseMessage(RawResponse rawResponse);
}
