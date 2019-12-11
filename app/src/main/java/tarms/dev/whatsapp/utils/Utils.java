package tarms.dev.whatsapp.utils;

import com.google.firebase.auth.FirebaseUser;

public class Utils {

    public static final String[] AVATARS = {
            "https://firebasestorage.googleapis.com/v0/b/whats-app-messanger-clone-app.appspot.com/o/avatar%2Fic_male_avatar.png?alt=media&token=71bda7b7-ca45-4f0f-b53d-e3980faf5301"
    };

    public static String getUserInfoReference(FirebaseUser user) {
        return "users/" + user.getUid() + "/info/";
    }

    public static String getChatsReference(FirebaseUser user) {
        return "users/" + user.getUid() + "/chats/";
    }

    public static String setChatsReference(String userId) {
        return "users/" + userId + "/chats/";
    }

}
