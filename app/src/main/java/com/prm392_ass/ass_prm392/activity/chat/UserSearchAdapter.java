package com.prm392_ass.ass_prm392.activity.chat;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.prm392_ass.ass_prm392.R;
import java.util.List;
import java.util.Map;

public class UserSearchAdapter extends RecyclerView.Adapter<UserSearchAdapter.UserViewHolder> {
    public interface OnUserClickListener {
        void onUserClick(String userId, String userName);
    }
    private List<Map<String, String>> userList; // mỗi user có id và name
    private OnUserClickListener listener;

    public UserSearchAdapter(List<Map<String, String>> userList, OnUserClickListener listener) {
        this.userList = userList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_search_user_result, parent, false);
        return new UserViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        String userName = userList.get(position).get("name");
        String userId = userList.get(position).get("id");
        holder.tvUserName.setText(userName);
        holder.itemView.setOnClickListener(v -> listener.onUserClick(userId, userName));
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    static class UserViewHolder extends RecyclerView.ViewHolder {
        TextView tvUserName;
        public UserViewHolder(@NonNull View itemView) {
            super(itemView);
            tvUserName = itemView.findViewById(R.id.tvUserSearchName);
        }
    }
}
