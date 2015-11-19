package com.technopark.bulat.advandroidhomework2.network.socket.socketObserver;

import com.technopark.bulat.advandroidhomework2.network.response.RawResponse;

/**
 * Created by bulat on 17.11.15.
 */
public interface Observable {
    void registerObserver(Observer o);
    void removeObserver(Observer o);
    void notifyObservers(RawResponse rawResponse);
}
