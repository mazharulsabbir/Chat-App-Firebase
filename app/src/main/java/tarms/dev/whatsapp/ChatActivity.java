package tarms.dev.whatsapp;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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

import tarms.dev.whatsapp.adapter.MsgAdapter;
import tarms.dev.whatsapp.model.Chats;
import tarms.dev.whatsapp.model.Msg;
import tarms.dev.whatsapp.model.User;
import tarms.dev.whatsapp.utils.Utils;

public class ChatActivity extends AppCompatActivity {

    private FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    private DatabaseReference reference = FirebaseDatabase.getInstance().getReference();

    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        recyclerView = findViewById(R.id.message_holder);

        initUser();

        chatting();

        findViewById(R.id.send_message).setOnClickListener(view -> {
            EditText mMsg = findViewById(R.id.message);
            String msg = mMsg.getText().toString();
            String receiverUid = getIntent().getStringExtra(Utils.RECEIVER_UID);

            if (!msg.trim().isEmpty()) {
                mMsg.setText("");
                newMessage(msg, receiverUid);
            }
        });
    }

    private void initUser() {
        if (getIntent() != null) {
            User user = getIntent().getParcelableExtra(Utils.RECEIVER);

            if (user != null) {
//                Glide.with(this).asDrawable().load(user.getImage())
//                        .circleCrop()
//                        .apply(new RequestOptions().override(50, 50))
//                        .into(new CustomTarget<Drawable>() {
//                            @Override
//                            public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
//                                if (getSupportActionBar() != null) {
//                                    getSupportActionBar().setLogo(resource);
//
//                                }
//                            }
//
//                            @Override
//                            public void onLoadCleared(@Nullable Drawable placeholder) {
//
//                            }
//                        });

                if (getSupportActionBar() != null) {
                    getSupportActionBar().setTitle(user.getName());
//                    getSupportActionBar().setSubtitle(user.getPhone());
                }
            }
        }
    }

    private void chatting() {
        List<Msg> msgs = new ArrayList<>();

        reference.child(Utils.CHAT).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                msgs.clear();

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Msg msg = snapshot.getValue(Msg.class);

                    if (msg != null) {
                        if (msg.getSenderUid().equals(user.getUid()) | msg.getReceiverUid().equals(user.getUid())) {
                            msgs.add(msg);
                        }
                    }
                }

                MsgAdapter msgAdapter = new MsgAdapter(msgs);

                recyclerView.setHasFixedSize(true);
                recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

                recyclerView.setAdapter(msgAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void newMessage(String msg, String receiveUid) {
        User receiver = getIntent().getParcelableExtra(Utils.RECEIVER);

        Msg msg1 = new Msg(user.getUid(), receiveUid, msg, String.valueOf(new Date().getTime()));
        reference.child(Utils.CHAT).push().setValue(msg1);

        /*sender site*/
        reference.child(Utils.setChatsReference(receiveUid))
                .child(user.getUid()) /*this will be sender uid*/
                .setValue(new Chats(user.getUid()/*sender id*/,
                        String.valueOf(user.getPhotoUrl()),
                        user.getDisplayName(), msg
                        , String.valueOf(new Date().getTime()),
                        false)
                );

        /*receiver site*/
        reference.child(Utils.setChatsReference(user.getUid()))
                .child(receiveUid) /*this will be receiver uid*/
                .setValue(new Chats(receiveUid,
                        String.valueOf(receiver.getImage()),
                        receiver.getName(), msg
                        , String.valueOf(new Date().getTime()),
                        true)
                );
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
