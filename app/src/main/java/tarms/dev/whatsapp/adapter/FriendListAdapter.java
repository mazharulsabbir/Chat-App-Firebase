package tarms.dev.whatsapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

import tarms.dev.whatsapp.R;
import tarms.dev.whatsapp.model.OnItemClickListener;
import tarms.dev.whatsapp.model.User;

public class FriendListAdapter extends RecyclerView.Adapter<FriendListAdapter.FriendHolder> {
    private List<User> friendList;
    private Context context;

    private OnItemClickListener onItemClickListener;

    public FriendListAdapter(List<User> friendList, Context context) {
        this.friendList = friendList;
        this.context = context;
    }

    @NonNull
    @Override
    public FriendHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_item, parent, false);

        return new FriendHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FriendHolder holder, int position) {
        User user = friendList.get(position);

        holder.name.setText(user.getName());
        Glide.with(context).load(user.getImage()).circleCrop().into(holder.avatar);
    }

    @Override
    public int getItemCount() {
        return friendList.size();
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public class FriendHolder extends RecyclerView.ViewHolder {

        private ImageView avatar;
        private TextView name;

        public FriendHolder(@NonNull View itemView) {
            super(itemView);

            avatar = itemView.findViewById(R.id.avatar);
            name = itemView.findViewById(R.id.user_name);

            itemView.setOnClickListener(view -> {
                int pos = getAdapterPosition();
                if (pos != RecyclerView.NO_POSITION) {
                    if (onItemClickListener != null) {
                        onItemClickListener.itemClick(pos);
                    }
                }
            });
        }
    }
}
