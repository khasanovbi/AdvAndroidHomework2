package com.technopark.bulat.advandroidhomework2.socket;

/**
 * Created by bulat on 05.11.15.
 */
public interface RequestListener {
    void onRequestResult(String result);
    void onRequestError(int errorStringID);
}
