package com.prm392_ass.ass_prm392.activity.chat;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.prm392_ass.ass_prm392.R;
import com.prm392_ass.ass_prm392.entity.Chat;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ChatViewHolder> {
    private List<Chat> chatList;
    private String currentUserId;
    private Map<String, String> userIdToName;
    private OnChatClickListener listener;

    public interface OnChatClickListener {
        void onChatClick(Chat chat);
    }

    public ChatAdapter(List<Chat> chatList, String currentUserId, Map<String, String> userIdToName, OnChatClickListener listener) {
        this.chatList = chatList;
        this.currentUserId = currentUserId;
        this.userIdToName = userIdToName;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ChatViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_chat, parent, false);
        return new ChatViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ChatViewHolder holder, int position) {
        Chat chat = chatList.get(position);

        String opponentId = chat.getParticipants().get(0).equals(currentUserId)
                ? chat.getParticipants().get(1)
                : chat.getParticipants().get(0);

        String opponentName = userIdToName != null ? userIdToName.get(opponentId) : null;
        // CHỈ hiện tên đối phương, nếu không có thì "Unknown"
        holder.txtOpponentName.setText(opponentName != null ? opponentName : "Unknown");

        holder.txtLastMessage.setText(chat.getLastMessage());
        SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm, dd/MM", Locale.getDefault());
        String timeString = dateFormat.format(chat.getLastUpdated());
        holder.txtTime.setText(timeString);

        holder.imgUnread.setVisibility(chat.isHasUnread() ? View.VISIBLE : View.GONE);

        holder.itemView.setOnClickListener(v -> listener.onChatClick(chat));
    }


    @Override
    public int getItemCount() {
        return chatList.size();
    }

    static class ChatViewHolder extends RecyclerView.ViewHolder {
        TextView txtOpponentName, txtLastMessage, txtTime;
        ImageView imgUnread;

        public ChatViewHolder(@NonNull View itemView) {
            super(itemView);
            txtOpponentName = itemView.findViewById(R.id.tvOpponentName);
            txtLastMessage = itemView.findViewById(R.id.tvChatLastMessage);
            txtTime = itemView.findViewById(R.id.tvChatTime);
            imgUnread = itemView.findViewById(R.id.imgChatUnread);
        }
    }
}
