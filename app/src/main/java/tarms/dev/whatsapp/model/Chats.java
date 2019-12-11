package tarms.dev.whatsapp.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class Chats implements Parcelable {
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

    protected Chats(Parcel in) {
        uid = in.readString();
        imageUrl = in.readString();
        name = in.readString();
        msg = in.readString();
        time = in.readString();
        isSeen = in.readByte() != 0;
    }

    public static final Creator<Chats> CREATOR = new Creator<Chats>() {
        @Override
        public Chats createFromParcel(Parcel in) {
            return new Chats(in);
        }

        @Override
        public Chats[] newArray(int size) {
            return new Chats[size];
        }
    };

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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(uid);
        parcel.writeString(imageUrl);
        parcel.writeString(name);
        parcel.writeString(msg);
        parcel.writeString(time);
        parcel.writeByte((byte) (isSeen ? 1 : 0));
    }
}
