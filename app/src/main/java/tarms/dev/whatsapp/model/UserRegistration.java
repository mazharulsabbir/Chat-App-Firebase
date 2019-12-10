package tarms.dev.whatsapp.model;

import android.os.Parcel;
import android.os.Parcelable;

public class UserRegistration implements Parcelable {
    public static final Creator<UserRegistration> CREATOR = new Creator<UserRegistration>() {
        @Override
        public UserRegistration createFromParcel(Parcel in) {
            return new UserRegistration(in);
        }

        @Override
        public UserRegistration[] newArray(int size) {
            return new UserRegistration[size];
        }
    };
    private String name, email, phone, password, image;

    public UserRegistration() {
    }

    public UserRegistration(String name, String email, String phone, String password, String image) {
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.password = password;
        this.image = image;
    }

    protected UserRegistration(Parcel in) {
        name = in.readString();
        email = in.readString();
        phone = in.readString();
        password = in.readString();
        image = in.readString();
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPhone() {
        return phone;
    }

    public String getPassword() {
        return password;
    }

    public String getImage() {
        return image;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(name);
        parcel.writeString(email);
        parcel.writeString(phone);
        parcel.writeString(password);
        parcel.writeString(image);
    }
}
