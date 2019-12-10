package tarms.dev.whatsapp.model;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class Msg {
    private String senderUid, receiverUid, message, time;

    public Msg() {
    }

    public Msg(String senderUid, String receiverUid, String message, String time) {
        this.senderUid = senderUid;
        this.receiverUid = receiverUid;
        this.message = message;
        this.time = time;
    }

    public String getSenderUid() {
        return senderUid;
    }

    public String getReceiverUid() {
        return receiverUid;
    }

    public String getMessage() {
        return message;
    }

    public String getTime() {
        return time;
    }
}
