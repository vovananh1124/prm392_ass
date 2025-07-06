package com.fptu.prm391.prm391_project;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.fptu.prm391.prm391_project.adapter.ChatAdapter;
import com.fptu.prm391.prm391_project.model.ChatMessage;
import com.google.firebase.database.*;

import java.util.ArrayList;
import java.util.List;

public class ChatActivity extends AppCompatActivity {
    private RecyclerView recyclerViewChat;
    private EditText editTextMessage;
    private Button buttonSend;
    private Spinner spinnerRole;
    private ChatAdapter chatAdapter;
    private List<ChatMessage> messageList = new ArrayList<>();

    private DatabaseReference messagesRef;
    private String chatRoomId = "room1";
    private String currentUserId = "student1"; // Mặc định là student

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        recyclerViewChat = findViewById(R.id.recyclerViewChat);
        editTextMessage = findViewById(R.id.editTextMessage);
        buttonSend = findViewById(R.id.buttonSend);
        spinnerRole = findViewById(R.id.spinnerRole);

        // Khởi tạo Adapter với phân biệt gửi/nhận
        chatAdapter = new ChatAdapter(messageList, () -> currentUserId);
        recyclerViewChat.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewChat.setAdapter(chatAdapter);

        // Khởi tạo reference đúng best practice
        messagesRef = FirebaseDatabase.getInstance().getReference("messages").child(chatRoomId);

        // Chọn vai trò bằng Spinner
        ArrayAdapter<String> roleAdapter = new ArrayAdapter<>(
                this, android.R.layout.simple_spinner_item, new String[]{"Sinh viên", "Recruiter"});
        roleAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerRole.setAdapter(roleAdapter);

        spinnerRole.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                currentUserId = (position == 0) ? "student1" : "recruiter";
                chatAdapter.notifyDataSetChanged(); // Để cập nhật phân biệt gửi/nhận
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        // Lắng nghe tin nhắn mới
        messagesRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, String previousChildName) {
                ChatMessage msg = snapshot.getValue(ChatMessage.class);
                if (msg != null) {
                    messageList.add(msg);
                    chatAdapter.notifyItemInserted(messageList.size() - 1);
                    recyclerViewChat.scrollToPosition(messageList.size() - 1);
                }
            }
            @Override public void onChildChanged(@NonNull DataSnapshot snapshot, String previousChildName) {}
            @Override public void onChildRemoved(@NonNull DataSnapshot snapshot) {}
            @Override public void onChildMoved(@NonNull DataSnapshot snapshot, String previousChildName) {}
            @Override public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(ChatActivity.this, "Lỗi: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        // Gửi tin nhắn + auto-reply recruiter
        buttonSend.setOnClickListener(v -> {
            String text = editTextMessage.getText().toString().trim();
            if (!text.isEmpty()) {
                ChatMessage msg = new ChatMessage(currentUserId, text, System.currentTimeMillis());
                messagesRef.push().setValue(msg).addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        editTextMessage.setText("");
                        // Nếu là sinh viên gửi thì tự động trả lời recruiter
                        if (currentUserId.equals("student1")) {
                            new Handler().postDelayed(() -> {
                                ChatMessage autoReply = new ChatMessage(
                                        "recruiter",
                                        "Cảm ơn bạn đã liên hệ, chúng tôi sẽ phản hồi sớm!",
                                        System.currentTimeMillis()
                                );
                                messagesRef.push().setValue(autoReply);
                            }, 1500); // 1.5 giây
                        }
                    } else {
                        Toast.makeText(ChatActivity.this, "Gửi thất bại: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }
}
