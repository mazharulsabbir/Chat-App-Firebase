package tarms.dev.whatsapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import tarms.dev.whatsapp.adapter.ChatsAdapter;
import tarms.dev.whatsapp.model.Chats;
import tarms.dev.whatsapp.utils.Utils;

public class MainActivity extends AppCompatActivity {

    private FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    private DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
    private ImageButton avatar;

    private List<Chats> chatsList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        init();

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(view -> startActivity(new Intent(getApplicationContext(), NewChatActivity.class)));
    }

    private void init() {
        avatar = findViewById(R.id.avatar);
        Glide.with(getApplicationContext()).load("https://firebasestorage.googleapis.com/v0/b/whats-app-messanger-clone-app.appspot.com/o/avatar%2Fic_male_avatar.png?alt=media&token=71bda7b7-ca45-4f0f-b53d-e3980faf5301")
                .circleCrop().into(avatar);

        getChatLists();
    }

    private void getChatLists() {
        ChatsAdapter chatsAdapter = new ChatsAdapter(getApplicationContext(), chatsList);
        RecyclerView chatsView = findViewById(R.id.chat_holder);
        chatsView.setHasFixedSize(true);
        chatsView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

        chatsView.setAdapter(chatsAdapter);

        reference.child(Utils.getChatsReference(user))
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        chatsList.clear();

                        if (dataSnapshot.hasChildren()) {
                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                if (snapshot.getKey() != null) {
                                    Chats chats = snapshot.getValue(Chats.class);

                                    chatsList.add(chats);
                                    chatsAdapter.notifyDataSetChanged();

                                    chatsAdapter.setOnItemClickListener(pos -> {
                                        Chats updateChat = chatsList.get(pos);

                                        updateChat.setSeen(true);

                                        reference.child(Utils.setChatsReference(user.getUid()))
                                                .child(chatsList.get(pos).getUid())
                                                .setValue(updateChat);

                                        chatsAdapter.notifyDataSetChanged();
                                    });
                                }
                            }
                        } else {
                            dummyChats();/*remove this dummy chats*/
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        View view = findViewById(R.id.root_layout);

                        Snackbar.make(view, databaseError.getMessage(), Snackbar.LENGTH_SHORT).show();
                    }
                });

    }

    private void dummyChats() {
        for (int i = 0; i < 20; i++) {
            String senderId = reference.push().getKey();

            reference.child(Utils.setChatsReference(user.getUid()))
                    .child(senderId) /*todo: this will be sender uid*/
                    .setValue(new Chats(senderId/*todo: change with sender id*/,
                            String.valueOf(user.getPhotoUrl()),
                            user.getDisplayName(), "Just A Message " + i
                            , String.valueOf(new Date().getTime()),
                            false)
                    );
        }
    }

}
