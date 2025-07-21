package com.prm392_ass.ass_prm392.activity.chat;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.prm392_ass.ass_prm392.R;
import com.prm392_ass.ass_prm392.entity.Message;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

/**
 * Hiển thị danh sách tin nhắn gửi/nhận trong cuộc chat.
 * ViewType 0: Tin do chính người dùng gửi (bên phải)
 * ViewType 1: Tin do người khác gửi (bên trái)
 */
public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MessageViewHolder> {
    private List<Message> messageList;
    private String currentUserId;

    public MessageAdapter(List<Message> list, String currentUserId) {
        this.messageList = list;
        this.currentUserId = currentUserId;
    }

    @Override
    public int getItemViewType(int position) {
        // Nếu senderId trùng user đăng nhập -> gửi, ngược lại -> nhận
        Message message = messageList.get(position);
        boolean isSent = message.getSenderId() != null && message.getSenderId().equals(currentUserId);
        Log.d("MessageAdapter", "item at pos " + position + " senderId=" + message.getSenderId()
                + " currentUserId=" + currentUserId + " isSent=" + isSent);
        return isSent ? 0 : 1;
    }

    @NonNull
    @Override
    public MessageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        int layoutRes;
        if (viewType == 0) {
            layoutRes = R.layout.item_message_sent;     // tin gửi, bên phải
        } else {
            layoutRes = R.layout.item_message_received; // tin nhận, bên trái
        }
        View v = LayoutInflater.from(parent.getContext())
                .inflate(layoutRes, parent, false);
        return new MessageViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MessageViewHolder holder, int position) {
        Message msg = messageList.get(position);

        // Nội dung tin nhắn
        holder.txtMsg.setText(msg.getContent() != null ? msg.getContent() : "");

        // Format thời gian
        SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm", Locale.getDefault());
        String timeString = msg.getTimestamp() > 0
                ? dateFormat.format(msg.getTimestamp())
                : "";
        holder.txtTime.setText(timeString);
    }

    @Override
    public int getItemCount() {
        return messageList == null ? 0 : messageList.size();
    }

    static class MessageViewHolder extends RecyclerView.ViewHolder {
        TextView txtMsg, txtTime;
        public MessageViewHolder(@NonNull View itemView) {
            super(itemView);
            txtMsg = itemView.findViewById(R.id.tvMsgContent);
            txtTime = itemView.findViewById(R.id.tvMsgTime);
        }
    }
}
