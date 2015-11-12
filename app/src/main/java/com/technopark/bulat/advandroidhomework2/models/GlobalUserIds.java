package com.technopark.bulat.advandroidhomework2.models;

/**
 * Created by bulat on 11.11.15.
 */
public class GlobalUserIds {
    private static volatile GlobalUserIds instance;
    public String cid;
    public String sid;

    public static GlobalUserIds getInstance() {
        GlobalUserIds localInstance = instance;
        if (localInstance == null) {
            synchronized (GlobalUserIds.class) {
                localInstance = instance;
                if (localInstance == null) {
                    instance = localInstance = new GlobalUserIds();
                }
            }
        }
        return localInstance;
    }

    private GlobalUserIds() {

    }
}
