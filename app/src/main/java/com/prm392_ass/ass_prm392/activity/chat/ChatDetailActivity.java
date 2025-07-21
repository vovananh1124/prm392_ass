package com.prm392_ass.ass_prm392.activity.chat;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.prm392_ass.ass_prm392.R;
import com.prm392_ass.ass_prm392.entity.Message;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ChatDetailActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private MessageAdapter messageAdapter;
    private List<Message> messageList = new ArrayList<>();
    private EditText edtMessage;
    private Button btnSend;

    private String chatId;
    private String currentUserId;
    private String opponentId;
    private String opponentName; // Thêm biến

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_detail);

        // KHÔNG XỬ LÝ NÚT BACK NỮA, XOÁ HẲN
        // ImageButton btnBack = findViewById(R.id.btnBack);
        // btnBack.setOnClickListener(v -> finish());

        recyclerView = findViewById(R.id.rvMessages);
        edtMessage = findViewById(R.id.edtMessageInput);
        btnSend = findViewById(R.id.btnSend);

        // LẤY currentUserId từ FirebaseAuth
        currentUserId = FirebaseAuth.getInstance().getUid();
        if (currentUserId == null) {
            Toast.makeText(this, "Bạn chưa đăng nhập. Vui lòng đăng nhập lại.", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        chatId = getIntent().getStringExtra("chatId");
        List<String> participants = getIntent().getStringArrayListExtra("participants");
        opponentName = getIntent().getStringExtra("opponentName"); // THÊM DÒNG NÀY
        if (participants == null || participants.size() != 2) {
            Toast.makeText(this, "Thiếu dữ liệu participants", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }
        // Xác định opponentId (user còn lại trong participants)
        opponentId = participants.get(0).equals(currentUserId) ? participants.get(1) : participants.get(0);

        // Hiển thị tên đối phương ở Toolbar
        TextView tvOpponentName = findViewById(R.id.tvOpponentName);
        tvOpponentName.setText(opponentName != null ? opponentName : "Unknown");

        messageAdapter = new MessageAdapter(messageList, currentUserId);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(messageAdapter);

        listenMessagesRealtime();

        btnSend.setOnClickListener(v -> {
            String content = edtMessage.getText().toString().trim();
            if (TextUtils.isEmpty(content)) return;
            sendMessage(content);
            edtMessage.setText("");
        });
    }

    // ...Giữ nguyên các phương thức còn lại...
    private void listenMessagesRealtime() {
        FirebaseFirestore.getInstance()
                .collection("chats")
                .document(chatId)
                .collection("messages")
                .orderBy("timestamp", Query.Direction.ASCENDING)
                .addSnapshotListener((value, error) -> {
                    if (error != null) return;
                    messageList.clear();
                    if (value != null) {
                        for (DocumentSnapshot doc : value.getDocuments()) {
                            Message msg = doc.toObject(Message.class);
                            if (msg != null) messageList.add(msg);
                        }
                        messageAdapter.notifyDataSetChanged();
                        recyclerView.scrollToPosition(Math.max(0, messageList.size() - 1));
                        updateSeenMessages();
                    }
                });
    }

    private void sendMessage(String content) {
        long now = System.currentTimeMillis();
        Message message = new Message(currentUserId, content, now, false);

        DocumentReference chatRef = FirebaseFirestore.getInstance()
                .collection("chats")
                .document(chatId);

        chatRef.collection("messages")
                .add(message)
                .addOnSuccessListener(docRef -> {
                    Map<String, Object> updates = new HashMap<>();
                    updates.put("lastMessage", content);
                    updates.put("lastUpdated", now);
                    updates.put("hasUnread", true);
                    chatRef.set(updates, com.google.firebase.firestore.SetOptions.merge());
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Gửi tin nhắn thất bại: " + e.getMessage(), Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                });
    }

    private void updateSeenMessages() {
        FirebaseFirestore.getInstance()
                .collection("chats")
                .document(chatId)
                .collection("messages")
                .whereEqualTo("isSeen", false)
                .whereNotEqualTo("senderId", currentUserId)
                .get()
                .addOnSuccessListener(snapshot -> {
                    for (DocumentSnapshot doc : snapshot) {
                        doc.getReference().update("isSeen", true);
                    }
                    FirebaseFirestore.getInstance()
                            .collection("chats")
                            .document(chatId)
                            .update("hasUnread", false);
                });
    }
}
