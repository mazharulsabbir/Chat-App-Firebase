package tarms.dev.whatsapp.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import tarms.dev.whatsapp.R;
import tarms.dev.whatsapp.model.Chats;
import tarms.dev.whatsapp.model.OnItemClickListener;

public class ChatsAdapter extends RecyclerView.Adapter<ChatsAdapter.ChatHolder> {
    private Context context;
    private List<Chats> chatsList;
    private OnItemClickListener onItemClickListener;

    public ChatsAdapter(Context context, List<Chats> chatsList) {
        this.context = context;
        this.chatsList = chatsList;
    }

    @NonNull
    @Override
    public ChatHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chats, parent, false);
        return new ChatHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ChatHolder holder, int position) {
        Chats chats = chatsList.get(position);

        @SuppressLint("SimpleDateFormat")
        String dateString = new SimpleDateFormat("MM/dd/yyyy").format(new Date(Long.parseLong(chats.getTime())));
        Glide.with(context).load(chats.getImageUrl()).circleCrop().into(holder.avatar);

        holder.name.setText(chats.getName());
        holder.msgTime.setText(dateString);
        holder.lastMsg.setText(chats.getMsg());

        if (chats.isSeen()) {
            holder.lastMsg.setTextColor(Color.parseColor("#CCCCCC"));
            holder.lastMsg.setTypeface(null, Typeface.NORMAL);
        } else {
            holder.lastMsg.setTextColor(Color.parseColor("#000000"));
            holder.lastMsg.setTypeface(null, Typeface.BOLD_ITALIC);
        }

    }

    @Override
    public int getItemCount() {
        return chatsList.size();
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public class ChatHolder extends RecyclerView.ViewHolder {
        private ImageView avatar;
        private TextView name, lastMsg, msgTime;

        public ChatHolder(@NonNull View itemView) {
            super(itemView);

            avatar = itemView.findViewById(R.id.avatar);
            name = itemView.findViewById(R.id.user_name);
            lastMsg = itemView.findViewById(R.id.last_msg);
            msgTime = itemView.findViewById(R.id.last_message_time);

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
