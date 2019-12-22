package tarms.dev.whatsapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

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
import java.util.List;

import tarms.dev.whatsapp.adapter.ChatsAdapter;
import tarms.dev.whatsapp.model.Chats;
import tarms.dev.whatsapp.model.User;
import tarms.dev.whatsapp.utils.SwipeToRemoveItem;
import tarms.dev.whatsapp.utils.Utils;

public class MainActivity extends AppCompatActivity {

    private FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    private DatabaseReference reference = FirebaseDatabase.getInstance().getReference();

    private SwipeRefreshLayout reloadData;

    private List<Chats> chatsList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        init();
        getChatLists();

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(view -> startActivity(new Intent(getApplicationContext(), NewChatActivity.class)));
    }

    private void init() {
        reloadData = findViewById(R.id.refresh_data);

        reloadData.setRefreshing(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.action_logout) {
            FirebaseAuth.getInstance().signOut();
            startActivity(new Intent(getApplicationContext(), UserActivity.class));
            finish();
        }

        if (item.getItemId() == R.id.profile) {
            startActivity(new Intent(getApplicationContext(), ProfileActivity.class));
        }
        return super.onOptionsItemSelected(item);
    }

    private void getChatLists() {
        reloadData.setOnRefreshListener(this::getChatLists);

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

                                        User user = new User(updateChat.getName(), "", "", "", updateChat.getImageUrl());

                                        Intent intent = new Intent(getApplicationContext(), ChatActivity.class);
                                        intent.putExtra(Utils.RECEIVER_UID, updateChat.getUid());
                                        intent.putExtra(Utils.RECEIVER, user);

                                        startActivity(intent);

                                        chatsAdapter.notifyDataSetChanged();
                                    });
                                }

                            }
                        } else {
                            chatsAdapter.notifyDataSetChanged();
                            //todo: show no chats icon and start chat button
                        }

                        reloadData.setRefreshing(false);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        View view = findViewById(R.id.root_layout);

                        Snackbar.make(view, databaseError.getMessage(), Snackbar.LENGTH_SHORT).show();
                    }
                });

        SwipeToRemoveItem swipeToRemoveItem = new SwipeToRemoveItem(getApplicationContext()) {
            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                int pos = viewHolder.getAdapterPosition();

                Chats chats = chatsList.get(pos);

                reference.child(Utils.setChatsReference(user.getUid()))
                        .child(chatsList.get(pos).getUid())
                        .setValue(null);

                View view = findViewById(R.id.view);

                Snackbar.make(view, "Chat is removed", Snackbar.LENGTH_LONG).show();
            }
        };

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(swipeToRemoveItem);
//        itemTouchHelper.attachToRecyclerView(chatsView);

    }

}
