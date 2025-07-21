package com.prm392_ass.ass_prm392.activity.chat;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.prm392_ass.ass_prm392.R;
import com.prm392_ass.ass_prm392.entity.Chat;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ChatFragment extends Fragment {

    private RecyclerView rvChatList, rvSearchUserResult;
    private EditText etSearchChat, etSearchUser;
    private ChatAdapter chatAdapter;
    private UserSearchAdapter userSearchAdapter;
    private List<Chat> chatList = new ArrayList<>();
    private List<Chat> allChats = new ArrayList<>();
    private List<Map<String, String>> foundUsers = new ArrayList<>();
    private String currentUserId;

    // Thêm map ánh xạ userId sang tên
    private Map<String, String> userIdToName = new HashMap<>();

    public ChatFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState){
        View rootView = inflater.inflate(R.layout.fragment_chat, container, false);

        rvChatList = rootView.findViewById(R.id.rvChatList);
        rvSearchUserResult = rootView.findViewById(R.id.rvSearchUserResult);
        etSearchChat = rootView.findViewById(R.id.etSearchChat);
        etSearchUser = rootView.findViewById(R.id.etSearchUser);

        currentUserId = FirebaseAuth.getInstance().getUid();

        chatAdapter = new ChatAdapter(chatList, currentUserId, userIdToName, chat -> {
            String opponentId = chat.getParticipants().get(0).equals(currentUserId)
                    ? chat.getParticipants().get(1)
                    : chat.getParticipants().get(0);
            String opponentName = userIdToName.get(opponentId);

            Intent intent = new Intent(getContext(), ChatDetailActivity.class);
            intent.putExtra("chatId", chat.getChatId());
            intent.putStringArrayListExtra("participants", new ArrayList<>(chat.getParticipants()));
            intent.putExtra("opponentName", opponentName != null ? opponentName : "Unknown"); // THÊM DÒNG NÀY
            startActivity(intent);
        });


        rvChatList.setLayoutManager(new LinearLayoutManager(getContext()));
        rvChatList.setAdapter(chatAdapter);

        userSearchAdapter = new UserSearchAdapter(foundUsers, (userId, userName) -> {
            String chatId = (currentUserId.compareTo(userId) < 0)
                    ? currentUserId + "_" + userId
                    : userId + "_" + currentUserId;

            FirebaseFirestore.getInstance().collection("chats").document(chatId)
                    .get().addOnSuccessListener(documentSnapshot -> {
                        if (!documentSnapshot.exists()) {
                            List<String> participants = new ArrayList<>();
                            participants.add(currentUserId);
                            participants.add(userId);
                            Map<String, Object> newChat = new HashMap<>();
                            newChat.put("participants", participants);
                            newChat.put("lastMessage", "");
                            newChat.put("lastUpdated", System.currentTimeMillis());
                            newChat.put("hasUnread", false);
                            FirebaseFirestore.getInstance()
                                    .collection("chats")
                                    .document(chatId)
                                    .set(newChat);
                        }
                        ArrayList<String> pl = new ArrayList<>();
                        if (currentUserId != null && userId != null) {
                            pl.add(currentUserId);
                            pl.add(userId);

                            Intent intent = new Intent(getContext(), ChatDetailActivity.class);
                            intent.putExtra("chatId", chatId);
                            intent.putStringArrayListExtra("participants", pl);
                            startActivity(intent);
                        } else {
                            Toast.makeText(getContext(), "Thiếu thông tin userId", Toast.LENGTH_SHORT).show();
                        }
                    });
            rvSearchUserResult.setVisibility(View.GONE);
            rvChatList.setVisibility(View.VISIBLE);
            etSearchUser.clearFocus();
        });

        rvSearchUserResult.setAdapter(userSearchAdapter);
        rvSearchUserResult.setLayoutManager(new LinearLayoutManager(getContext()));
        rvSearchUserResult.setVisibility(View.GONE);

        // TẢI TOÀN BỘ USER LÊN MAP userIdToName ĐỂ FILTER CHAT THEO TÊN
        loadAllUsers();

        // SEARCH USER: ĐỘC LẬP
        etSearchUser.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int i, int i1, int i2) {}
            @Override public void onTextChanged(CharSequence s, int i, int i1, int i2) {
                String key = s.toString().trim();
                if (!key.isEmpty()) {
                    rvSearchUserResult.setVisibility(View.VISIBLE);
                    rvChatList.setVisibility(View.GONE);
                    searchUsersFromFirestore(key);
                } else {
                    rvSearchUserResult.setVisibility(View.GONE);
                    foundUsers.clear();
                    userSearchAdapter.notifyDataSetChanged();
                    rvChatList.setVisibility(View.VISIBLE);
                }
            }
            @Override public void afterTextChanged(Editable editable) {}
        });

        // SEARCH CHAT: ĐỘC LẬP, ĐÃ SỬA: SEARCH THEO TÊN ĐỐI PHƯƠNG (KHÔNG PHẢI ID)
        etSearchChat.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {
                String key = s.toString().trim();
                if (!key.isEmpty()) {
                    filterChatList(key);
                    rvChatList.setVisibility(View.VISIBLE);
                    rvSearchUserResult.setVisibility(View.GONE);
                } else {
                    filterChatList("");
                    rvChatList.setVisibility(View.VISIBLE);
                    rvSearchUserResult.setVisibility(View.GONE);
                }
            }
            @Override public void afterTextChanged(Editable s) {}
        });

        etSearchUser.setOnEditorActionListener((viewUser, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH
                    || actionId == EditorInfo.IME_ACTION_DONE) {
                viewUser.clearFocus();
                hideKeyboard(viewUser);
                return true;
            }
            return false;
        });

        etSearchChat.setOnEditorActionListener((viewChat, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH
                    || actionId == EditorInfo.IME_ACTION_DONE) {
                viewChat.clearFocus();
                hideKeyboard(viewChat);
                return true;
            }
            return false;
        });

        startListenChats();

        return rootView;
    }

    private void hideKeyboard(View view) {
        InputMethodManager imm = (InputMethodManager)
                requireContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    private void startListenChats() {
        FirebaseFirestore.getInstance()
                .collection("chats")
                .whereArrayContains("participants", currentUserId)
                .addSnapshotListener((value, error) -> {
                    if (error != null) return;
                    allChats.clear();
                    if (value != null) {
                        for (DocumentSnapshot doc : value.getDocuments()) {
                            Chat chat = doc.toObject(Chat.class);
                            if (chat != null) {
                                chat.setChatId(doc.getId());
                                allChats.add(chat);
                            }
                        }
                        Collections.sort(allChats, (o1, o2) -> Long.compare(o2.getLastUpdated(), o1.getLastUpdated()));
                        filterChatList(etSearchChat.getText().toString().trim());
                    }
                });
    }

    // FILTER CHAT ĐANG CÓ THEO TÊN ĐỐI PHƯƠNG
    private void filterChatList(String keyword) {
        chatList.clear();
        if (keyword.isEmpty()) {
            chatList.addAll(allChats);
        } else {
            for (Chat chat : allChats) {
                String opponentId = chat.getParticipants().get(0).equals(currentUserId)
                        ? chat.getParticipants().get(1)
                        : chat.getParticipants().get(0);
                String opponentName = userIdToName.get(opponentId);
                if (opponentName != null && opponentName.toLowerCase().contains(keyword.toLowerCase())) {
                    chatList.add(chat);
                }
            }
        }
        chatAdapter.notifyDataSetChanged();
    }

    private void searchUsersFromFirestore(String keyword) {
        FirebaseFirestore.getInstance().collection("user")
                .whereGreaterThanOrEqualTo("name", keyword)
                .whereLessThanOrEqualTo("name", keyword + "\uf8ff")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    Log.d("SEARCH_USER", "Query returned " + queryDocumentSnapshots.size() + " docs");
                    foundUsers.clear();
                    for (DocumentSnapshot doc : queryDocumentSnapshots.getDocuments()) {
                        String id = doc.getId();
                        Log.d("SEARCH_USER", "Doc id: " + id + ", name=" + doc.getString("name"));
                        if (id.equals(currentUserId)) continue;
                        String name = doc.getString("name");
                        if (name != null && name.toLowerCase().contains(keyword.toLowerCase())) {
                            Map<String, String> userMap = new HashMap<>();
                            userMap.put("id", id);
                            userMap.put("name", name);
                            foundUsers.add(userMap);
                        }
                    }
                    userSearchAdapter.notifyDataSetChanged();
                })
                .addOnFailureListener(e -> {
                    Log.e("SEARCH_USER", "Query failed", e);
                    rvSearchUserResult.setVisibility(View.GONE);
                });
    }

    private void loadAllUsers() {
        FirebaseFirestore.getInstance().collection("user").get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    userIdToName.clear();
                    for (DocumentSnapshot doc : queryDocumentSnapshots) {
                        String id = doc.getId();
                        String name = doc.getString("name");
                        if (name != null) userIdToName.put(id, name);
                    }
                    // Cập nhật lại list chat để show tên sau khi đã nạp map
                    filterChatList(etSearchChat.getText().toString().trim());
                });
    }
}
