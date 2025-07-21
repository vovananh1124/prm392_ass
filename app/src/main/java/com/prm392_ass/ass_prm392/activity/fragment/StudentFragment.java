package com.prm392_ass.ass_prm392.activity.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.prm392_ass.ass_prm392.R;
import com.prm392_ass.ass_prm392.activity.adapter.InternshipAdapter;
import com.prm392_ass.ass_prm392.activity.home.FilterActivity;
import com.prm392_ass.ass_prm392.activity.internship.InternshipDetailActivity;
import com.prm392_ass.ass_prm392.entity.Internship;

import java.util.ArrayList;
import java.util.List;

public class StudentFragment extends Fragment {

    private RecyclerView recyclerViewJobs;
    private FirebaseFirestore db;
    private List<Internship> internshipList;
    private InternshipAdapter adapter;

    private final ActivityResultLauncher<Intent> filterActivityResultLauncher =
            registerForActivityResult(
                    new ActivityResultContracts.StartActivityForResult(),
                    result -> {
                        if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                            String locationFilter = result.getData().getStringExtra("locationFilter");
                            String salaryFilter   = result.getData().getStringExtra("salaryFilter");
                            applyFilter(locationFilter, salaryFilter);
                        }
                    }
            );

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_student, container, false);

        // 1) Khởi tạo Firestore và danh sách
        db = FirebaseFirestore.getInstance();
        internshipList = new ArrayList<>();

        // 2) Tạo adapter với callback click
        adapter = new InternshipAdapter(internshipList, internship -> {
            // Khi click, mở detail và truyền internshipId
            Intent intent = new Intent(requireActivity(), InternshipDetailActivity.class);
            intent.putExtra("internshipId", internship.getId());
            startActivity(intent);
        });

        // 3) Setup RecyclerView
        recyclerViewJobs = view.findViewById(R.id.recyclerViewJobs);
        recyclerViewJobs.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerViewJobs.setAdapter(adapter);

        // 4) Gán click cho nút filter
        ImageView imgFilter = view.findViewById(R.id.imgFilter);
        imgFilter.setOnClickListener(v -> {
            Intent intent = new Intent(requireActivity(), FilterActivity.class);
            filterActivityResultLauncher.launch(intent);
        });

        // 5) Load dữ liệu
        loadInternships();

        return view;
    }

    private void loadInternships() {
        db.collection("internships")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    internshipList.clear();
                    for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                        Internship internship = document.toObject(Internship.class);
                        // Bắt buộc gán id từ document để không bị null
                        internship.setId(document.getId());
                        internshipList.add(internship);
                    }
                    adapter.notifyDataSetChanged();
                })
                .addOnFailureListener(e ->
                        Toast.makeText(getContext(),
                                        "Error loading data: " + e.getMessage(),
                                        Toast.LENGTH_SHORT)
                                .show()
                );
    }

    private void applyFilter(String location, String salary) {
        com.google.firebase.firestore.Query query = db.collection("internships");
        if (location != null && !"All".equals(location)) {
            query = query.whereEqualTo("location", location);
        }
        if (salary != null && !"All".equals(salary)) {
            query = query.whereEqualTo("salary", salary);
        }
        query.get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    internshipList.clear();
                    for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                        Internship internship = document.toObject(Internship.class);
                        // Cũng phải gán ID ở đây
                        internship.setId(document.getId());
                        internshipList.add(internship);
                    }
                    adapter.notifyDataSetChanged();
                })
                .addOnFailureListener(e ->
                        Toast.makeText(getContext(),
                                        "Filter error: " + e.getMessage(),
                                        Toast.LENGTH_SHORT)
                                .show()
                );
    }
}
