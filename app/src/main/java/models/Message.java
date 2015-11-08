package models;

/**
 * Created by bulat on 08.11.15.
 */
public class Message {
    private int id;
    private User author;
    private String text;

    public Message(String text, User author) {
        this.author = author;
        this.text = text;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public User getAuthor() {
        return author;
    }

    public void setAuthor(User author) {
        this.author = author;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
