package com.prm392_ass.ass_prm392.activity.home;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.prm392_ass.ass_prm392.R;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class FilterActivity extends AppCompatActivity {

    private Spinner spinnerLocation, spinnerSalary;
    private Button btnApply;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter);

        spinnerLocation = findViewById(R.id.spinnerLocation);
        spinnerSalary = findViewById(R.id.spinnerSalary);
        btnApply = findViewById(R.id.btnApply);
        db = FirebaseFirestore.getInstance();

        loadFilterData();

        btnApply.setOnClickListener(v -> {
            String location = spinnerLocation.getSelectedItem().toString();
            String salary = spinnerSalary.getSelectedItem().toString();

            Intent resultIntent = new Intent();
            resultIntent.putExtra("locationFilter", location);
            resultIntent.putExtra("salaryFilter", salary);
            setResult(RESULT_OK, resultIntent);
            finish();
        });
    }

    private void loadFilterData() {
        db.collection("internships")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    Set<String> locations = new HashSet<>();
                    Set<String> salaries = new HashSet<>();
                    for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                        String loc = doc.getString("location");
                        String sal = doc.getString("salary");
                        if (loc != null) locations.add(loc);
                        if (sal != null) salaries.add(sal);
                    }
                    ArrayList<String> locationList = new ArrayList<>(locations);
                    ArrayList<String> salaryList = new ArrayList<>(salaries);
                    locationList.add(0, "All");
                    salaryList.add(0, "All");

                    spinnerLocation.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, locationList));
                    spinnerSalary.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, salaryList));
                });
    }
}