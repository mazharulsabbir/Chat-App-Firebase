package tarms.dev.whatsapp.adapter;

import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import tarms.dev.whatsapp.model.Friend;

public class FriendListAdapter extends RecyclerView.Adapter<FriendListAdapter.FriendHolder> {
    private List<Friend> friendList;

    public FriendListAdapter(List<Friend> friendList) {
        this.friendList = friendList;
    }

    @NonNull
    @Override
    public FriendHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull FriendHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return friendList.size();
    }

    public class FriendHolder extends RecyclerView.ViewHolder {
        public FriendHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
