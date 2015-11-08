package models;

/**
 * Created by bulat on 08.11.15.
 */
public class User {
    private String nickName;
    private String password;
    private String email;
    private String status;
    private String image;

    public User(String nickName, String email, String status, String image) {
        this.nickName = nickName;
        this.password = password;
        this.email = email;
        this.status = status;
        this.image = image;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
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
