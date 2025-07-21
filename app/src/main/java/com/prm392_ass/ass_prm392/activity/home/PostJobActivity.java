package com.prm392_ass.ass_prm392.activity.home;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.prm392_ass.ass_prm392.R;
import com.prm392_ass.ass_prm392.entity.Internship;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.UUID;

public class PostJobActivity extends AppCompatActivity {

    private EditText editTextTitle, editTextLocation, editTextSalary;
    private Button btnSave;
    private FirebaseFirestore db;
    private Internship internshipToEdit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_job);

        db = FirebaseFirestore.getInstance();
        editTextTitle    = findViewById(R.id.editTextTitle);
        editTextLocation = findViewById(R.id.editTextLocation);
        editTextSalary   = findViewById(R.id.editTextSalary);
        btnSave          = findViewById(R.id.btnSave);

        // Nếu đang edit, lấy internship đã truyền lên
        internshipToEdit = (Internship) getIntent().getSerializableExtra("internship");
        if (internshipToEdit != null) {
            editTextTitle.setText(internshipToEdit.getTitle());
            editTextLocation.setText(internshipToEdit.getLocation());
            editTextSalary.setText(internshipToEdit.getSalary());
        }

        btnSave.setOnClickListener(v -> saveJob());
    }

    private void saveJob() {
        String title    = editTextTitle.getText().toString().trim();
        String location = editTextLocation.getText().toString().trim();
        String salary   = editTextSalary.getText().toString().trim();

        if (title.isEmpty() || location.isEmpty() || salary.isEmpty()) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        // 1) Tự sinh ID nếu là tạo mới, hoặc giữ ID cũ khi edit
        String id = (internshipToEdit != null)
                ? internshipToEdit.getId()
                : UUID.randomUUID().toString();

        // 2) Tạo đối tượng Internship và gán ID vào field
        Internship internship = new Internship();
        internship.setId(id);
        internship.setTitle(title);
        internship.setCompanyName("Company Name");      // nếu bạn có trường companyName
        internship.setLocation(location);
        internship.setField("Field");                   // tạm hardcode
        internship.setJobType("Job Type");
        internship.setSeniority("Seniority");
        internship.setTimePosted("Just now");
        internship.setSalary(salary);
        internship.setCompanyLogoUrl("https://example.com/logo.png");
        internship.setCompanyId("company_001");         // lấy từ session của bạn
        internship.setDatePosted(new com.google.firebase.Timestamp(new java.util.Date()));
        internship.setDeadline(null);
        internship.setLatitude(null);
        internship.setLongitude(null);

        // 3) Lưu vào Firestore với document key = id
        db.collection("internships")
                .document(id)
                .set(internship)
                .addOnSuccessListener(aVoid -> {
                    String msg = (internshipToEdit != null) ?
                            "Job updated successfully" : "Job added successfully";
                    Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
                    finish();
                })
                .addOnFailureListener(e -> {
                    Log.e("FirestoreJob", "Error saving job", e);
                    Toast.makeText(this, "Error saving job: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });

    }
}
