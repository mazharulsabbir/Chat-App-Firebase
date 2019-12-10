package tarms.dev.whatsapp.model;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class Chats {
    private String uid, imageUrl, name, msg, time;
    private boolean isSeen;

    public Chats() {
    }

    public Chats(String uid, String imageUrl, String name, String msg, String time, boolean isSeen) {
        this.uid = uid;
        this.imageUrl = imageUrl;
        this.name = name;
        this.msg = msg;
        this.time = time;
        this.isSeen = isSeen;
    }

    public String getUid() {
        return uid;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getName() {
        return name;
    }

    public String getMsg() {
        return msg;
    }

    public String getTime() {
        return time;
    }

    public boolean isSeen() {
        return isSeen;
    }

    public void setSeen(boolean seen) {
        isSeen = seen;
    }
}
