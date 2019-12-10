package tarms.dev.whatsapp.utils;

import com.google.firebase.auth.FirebaseUser;

public class Utils {

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
