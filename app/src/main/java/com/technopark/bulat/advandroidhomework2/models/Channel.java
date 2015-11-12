package com.technopark.bulat.advandroidhomework2.models;

import java.io.Serializable;

/**
 * Created by bulat on 07.11.15.
 */
public class Channel implements Serializable {
    public static final String descriptionKey = "Channel";
    private String chid;
    private String image;
    private String name;
    private String description;
    private int onlineCount;

    public Channel() {
    }

    public Channel(String image, String name, String description, int onlineCount) {
        this.image = image;
        this.name = name;
        this.description = description;
        this.onlineCount = onlineCount;
    }

    public String getChid() {
        return chid;
    }

    public void setChid(String chid) {
        this.chid = chid;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public int getOnlineCount() {
        return onlineCount;
    }

    public void setOnlineCount(int onlineCount) {
        this.onlineCount = onlineCount;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
