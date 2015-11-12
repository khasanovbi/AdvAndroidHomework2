package com.technopark.bulat.advandroidhomework2.models;

/**
 * Created by bulat on 08.11.15.
 */
public class User {
    private String nickname;
    private String password;
    private String email;
    private String status;
    private String image;

    public User(String nickname, String email, String status, String image) {
        this.nickname = nickname;
        this.password = password;
        this.email = email;
        this.status = status;
        this.image = image;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
