package com.prm392_ass.ass_prm392.activity.application;

import android.os.Bundle;


import androidx.appcompat.app.AppCompatActivity;

import com.prm392_ass.ass_prm392.R;

import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.prm392_ass.ass_prm392.activity.adapter.ApplicationAdapter;
import com.prm392_ass.ass_prm392.entity.ApplicationItem;

import java.util.ArrayList;
import java.util.List;

public class ApplicationHistoryActivity extends AppCompatActivity {
    private RecyclerView rvApplications;
    private ApplicationAdapter adapter;
    private List<ApplicationItem> applicationItemList = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_application_history);

        rvApplications = findViewById(R.id.rvApplications);
        adapter = new ApplicationAdapter(this, applicationItemList);
        rvApplications.setAdapter(adapter);
        rvApplications.setLayoutManager(new LinearLayoutManager(this));

        loadApplicationHistory();
    }

    private void loadApplicationHistory() {
        String currentUserId = FirebaseAuth.getInstance().getUid();
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection("applications")
                .whereEqualTo("studentId", currentUserId)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    applicationItemList.clear();
                    for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                        // Giả sử bạn có hàm chuyển doc sang ApplicationItem
                        ApplicationItem item = doc.toObject(ApplicationItem.class);
                        applicationItemList.add(item);
                    }
                    adapter.notifyDataSetChanged();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Lỗi khi tải lịch sử ứng tuyển: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }
}
