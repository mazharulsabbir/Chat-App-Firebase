package tarms.dev.whatsapp.adapter;

import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import tarms.dev.whatsapp.model.Msg;

public class MsgAdapter extends RecyclerView.Adapter<MsgAdapter.MsgHolder> {
    private List<Msg> msgList;

    public MsgAdapter(List<Msg> msgList) {
        this.msgList = msgList;
    }

    @NonNull
    @Override
    public MsgHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull MsgHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return msgList.size();
    }

    public class MsgHolder extends RecyclerView.ViewHolder{
        public MsgHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
