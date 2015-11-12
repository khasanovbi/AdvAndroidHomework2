package com.technopark.bulat.advandroidhomework2.models;

/**
 * Created by bulat on 08.11.15.
 */
public class Message {
    private String id;
    private String authorId;
    private String authorNickname;
    private String text;
    private String time;

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public Message() {
    }

    public Message(String id, String authorId, String authorNickname, String text, String time) {
        this.id = id;
        this.authorId = authorId;
        this.authorNickname = authorNickname;
        this.text = text;
        this.time = time;
    }

    public String getAuthorNickname() {

        return authorNickname;
    }

    public void setAuthorNickname(String authorNickname) {
        this.authorNickname = authorNickname;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAuthorId() {
        return authorId;
    }

    public void setAuthorId(String authorId) {
        this.authorId = authorId;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
