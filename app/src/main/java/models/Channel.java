package models;

/**
 * Created by bulat on 07.11.15.
 */
public class Channel {
    private String image;
    private String name;
    private String description;
    private int onlineCount;

    public Channel(String image, String name, String description, int onlineCount) {
        this.image = image;
        this.name = name;
        this.description = description;
        this.onlineCount = onlineCount;
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
