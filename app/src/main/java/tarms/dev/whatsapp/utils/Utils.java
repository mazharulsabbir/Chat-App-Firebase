package tarms.dev.whatsapp.utils;

import com.google.firebase.auth.FirebaseUser;

public class Utils {

    public static final String RECEIVER_UID = "receiver-id";
    public static final String RECEIVER = "receiver";

    public static final String[] AVATARS = {
            "https://firebasestorage.googleapis.com/v0/b/whats-app-messanger-clone-app.appspot.com/o/avatar%2Fic_male_avatar.png?alt=media&token=71bda7b7-ca45-4f0f-b53d-e3980faf5301",
            "https://firebasestorage.googleapis.com/v0/b/whats-app-messanger-clone-app.appspot.com/o/avatar%2Favatar%20(1).png?alt=media&token=5232aa37-7513-4539-9f05-ecc6eedf5077",
            "https://firebasestorage.googleapis.com/v0/b/whats-app-messanger-clone-app.appspot.com/o/avatar%2Favatar%20(2).png?alt=media&token=25e25b8f-aa83-4509-a579-c8df48ae99bf",
            "https://firebasestorage.googleapis.com/v0/b/whats-app-messanger-clone-app.appspot.com/o/avatar%2Favatar%20(3).png?alt=media&token=17eee226-31b6-490f-87f4-c5f96fb9d185",
            "https://firebasestorage.googleapis.com/v0/b/whats-app-messanger-clone-app.appspot.com/o/avatar%2Favatar%20(4).png?alt=media&token=184e147d-fab1-4a38-a447-eecd766296a9"
    };

    public static final String USERS = "users/";

    public static String getUserInfoReference(FirebaseUser user) {
        return "users/" + user.getUid() + "/info/";
    }

    public static String getUserInfoReference(String user) {
        return "users/" + user + "/info/";
    }

    public static String getChatsReference(FirebaseUser user) {
        return "users/" + user.getUid() + "/chats/";
    }

    public static String setChatsReference(String userId) {
        return "users/" + userId + "/chats/";
    }

}
