package com.prm392_ass.ass_prm392.activity.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.prm392_ass.ass_prm392.R;
import com.prm392_ass.ass_prm392.activity.adapter.InternshipAdapter;
import com.prm392_ass.ass_prm392.activity.internship.InternshipDetailActivity;
import com.prm392_ass.ass_prm392.entity.Internship;

import java.util.ArrayList;
import java.util.List;

public class SearchFragment extends Fragment {

    private EditText editTextCompanyName;
    private ImageView btnSearch;
    private RecyclerView recyclerViewSearch;
    private FirebaseFirestore db;
    private List<Internship> internshipList;
    private InternshipAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search, container, false);

        // Ánh xạ view
        editTextCompanyName = view.findViewById(R.id.editTextCompanyName);
        btnSearch          = view.findViewById(R.id.btnSearch);
        recyclerViewSearch = view.findViewById(R.id.recyclerViewSearch);

        // Firestore + list + adapter với callback click
        db = FirebaseFirestore.getInstance();
        internshipList = new ArrayList<>();
        adapter = new InternshipAdapter(internshipList, internship -> {
            // mở detail khi click
            Intent intent = new Intent(requireActivity(), InternshipDetailActivity.class);
            intent.putExtra("internshipId", internship.getId());
            startActivity(intent);
        });

        // RecyclerView
        recyclerViewSearch.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerViewSearch.setAdapter(adapter);

        // Search button
        btnSearch.setOnClickListener(v -> performSearch());

        return view;
    }

    private void performSearch() {
        String companyName = editTextCompanyName.getText().toString().trim();

        if (companyName.isEmpty()) {
            Toast.makeText(getContext(),
                    "Please enter a company name",
                    Toast.LENGTH_SHORT).show();
            return;
        }

        Query query = db.collection("internships")
                .whereEqualTo("companyName", companyName);

        query.get()
                .addOnSuccessListener(querySnapshot -> {
                    internshipList.clear();
                    for (QueryDocumentSnapshot doc : querySnapshot) {
                        internshipList.add(doc.toObject(Internship.class));
                    }
                    adapter.notifyDataSetChanged();
                    if (internshipList.isEmpty()) {
                        Toast.makeText(getContext(),
                                "No results found for " + companyName,
                                Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(e ->
                        Toast.makeText(getContext(),
                                "Search error: " + e.getMessage(),
                                Toast.LENGTH_SHORT).show()
                );
    }

    /** Nếu bạn có nút Back trong layout, bạn có thể giữ lại phương thức này */
    public void onBackClick(View view) {
        requireActivity().getSupportFragmentManager().popBackStack();
    }
}
