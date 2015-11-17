package com.technopark.bulat.advandroidhomework2.network.request;

/**
 * Created by bulat on 17.11.15.
 */
public interface RequestListener {
    void onRequestResult(String result);
    void onRequestError(int errorStringID);
}