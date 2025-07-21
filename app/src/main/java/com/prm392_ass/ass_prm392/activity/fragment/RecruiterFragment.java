package com.prm392_ass.ass_prm392.activity.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.prm392_ass.ass_prm392.R;
import com.prm392_ass.ass_prm392.activity.adapter.RecruiterJobAdapter;
import com.prm392_ass.ass_prm392.entity.Internship;
import com.prm392_ass.ass_prm392.activity.home.PostJobActivity; // Tạo sau

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class RecruiterFragment extends Fragment {

    private RecyclerView recyclerViewJobs;
    private FirebaseFirestore db;
    private List<Internship> internshipList;
    private RecruiterJobAdapter adapter;
    private String companyId; // Giả sử lấy từ session hoặc user data

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_recruiter, container, false);

        db = FirebaseFirestore.getInstance();
        internshipList = new ArrayList<>();
        adapter = new RecruiterJobAdapter(internshipList, new RecruiterJobAdapter.OnJobActionListener() {
            @Override
            public void onEditClick(Internship internship) {
                editJob(internship);
            }

            @Override
            public void onDeleteClick(Internship internship) {
                deleteJob(internship.getId());
            }
        });

        Button btnAddJob = view.findViewById(R.id.btnAddJob);
        recyclerViewJobs = view.findViewById(R.id.recyclerViewJobs);
        recyclerViewJobs.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerViewJobs.setAdapter(adapter);

        btnAddJob.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), PostJobActivity.class);
            startActivity(intent);
        });

        loadJobs();

        return view;
    }

    private void loadJobs() {
        // Lấy companyId từ session (giả sử)
        companyId = "company_001"; // Thay bằng logic lấy từ user
        db.collection("internships")
                .whereEqualTo("companyId", companyId)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    internshipList.clear();
                    for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                        Internship internship = document.toObject(Internship.class);
                        internshipList.add(internship);
                    }
                    adapter.notifyDataSetChanged();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(getContext(), "Error loading jobs: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

    private void addJob(Internship internship) {
        db.collection("internships")
                .add(internship)
                .addOnSuccessListener(documentReference -> {
                    Toast.makeText(getContext(), "Job added successfully", Toast.LENGTH_SHORT).show();
                    loadJobs(); // Tải lại danh sách
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(getContext(), "Error adding job: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

    private void editJob(Internship internship) {
        // Chuyển đến PostJobActivity với dữ liệu để chỉnh sửa
        Intent intent = new Intent(getActivity(), PostJobActivity.class);
        intent.putExtra("internship", internship);
        startActivity(intent); // Cập nhật sau khi lưu
    }

    private void deleteJob(String jobId) {
        db.collection("internships")
                .document(jobId)
                .delete()
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(getContext(), "Job deleted successfully", Toast.LENGTH_SHORT).show();
                    loadJobs(); // Tải lại danh sách
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(getContext(), "Error deleting job: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }
}