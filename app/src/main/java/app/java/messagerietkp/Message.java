package app.java.messagerietkp;

import java.util.HashMap;
import java.util.Map;

public class Message {
    private String id;
    private String text;
    private int likes;
    private String username;
    private Map<String, Boolean> likedBy;

    public Message() {}

    public Message(String id, String text, int likes, String username) {
        this.id = id;
        this.text = text;
        this.likes = likes;
        this.username = username;
        this.likedBy = new HashMap<>();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getLikes() {
        return likes;
    }

    public void setLikes(int likes) {
        this.likes = likes;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Map<String, Boolean> getLikedBy() {
        if (likedBy == null) {
            likedBy = new HashMap<>();
        }
        return likedBy;
    }

    public void setLikedBy(Map<String, Boolean> likedBy) {
        this.likedBy = likedBy;
    }
}
