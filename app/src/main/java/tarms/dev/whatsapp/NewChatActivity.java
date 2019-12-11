package tarms.dev.whatsapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import tarms.dev.whatsapp.adapter.FriendListAdapter;
import tarms.dev.whatsapp.model.User;
import tarms.dev.whatsapp.utils.Utils;

public class NewChatActivity extends AppCompatActivity {
    private FirebaseUser mUser = FirebaseAuth.getInstance().getCurrentUser();
    private DatabaseReference reference = FirebaseDatabase.getInstance().getReference();

    private List<User> userList = new ArrayList<>();
    private List<String> userUid;

    private SwipeRefreshLayout refreshLayout;

    private FriendListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_chat);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("New Conversation");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        refreshLayout = findViewById(R.id.refresh_data);
        refreshLayout.setRefreshing(true);
        refreshLayout.setOnRefreshListener(this::getUserLists);

        getUserLists();
        init();
    }

    private void init() {
        RecyclerView recyclerView = findViewById(R.id.user_list);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        adapter = new FriendListAdapter(userList, getApplicationContext());

        recyclerView.setAdapter(adapter);

    }

    private void getUserLists() {
        reference.child(Utils.USERS).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                userList.clear();
                userUid = new ArrayList<>();

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    User user = snapshot.child("info").getValue(User.class);

                    if (user != null) {
                        userUid.add(snapshot.getKey());
                        if (!Objects.equals(snapshot.getKey(), mUser.getUid()))
                            userList.add(user);
                    }

                    adapter.notifyDataSetChanged();
                    adapter.setOnItemClickListener(pos -> {
                        Intent intent = new Intent(getApplicationContext(), ChatActivity.class);
                        intent.putExtra(Utils.RECEIVER, userList.get(pos));
                        intent.putExtra(Utils.RECEIVER_UID, userUid.get(pos));

                        startActivity(intent);
                        finish();
                    });

                }

                refreshLayout.setRefreshing(false);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                refreshLayout.setRefreshing(false);
            }
        });
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
