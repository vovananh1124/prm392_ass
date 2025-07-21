package com.prm392_ass.ass_prm392.activity.home;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.prm392_ass.ass_prm392.R;
import com.prm392_ass.ass_prm392.entity.Internship;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PostJobActivity extends AppCompatActivity {

    private EditText editTextTitle, editTextLocation, editTextSalary, editTextCompanyName;
    private EditText editTextDescription, editTextRequirement, editTextDeadline;
    private EditText editTextField, editTextJobType, editTextSeniority, editTextCompanyLogoUrl;
    private EditText editTextLatitude, editTextLongitude;
    private Button btnSave;
    private FirebaseFirestore db;
    private Internship internshipToEdit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_job);

        db = FirebaseFirestore.getInstance();
        editTextTitle = findViewById(R.id.editTextTitle);
        editTextLocation = findViewById(R.id.editTextLocation);
        editTextSalary = findViewById(R.id.editTextSalary);
        editTextCompanyName = findViewById(R.id.editTextCompanyName);
        editTextDescription = findViewById(R.id.editTextDescription);
        editTextRequirement = findViewById(R.id.editTextRequirement);
        editTextDeadline = findViewById(R.id.editTextDeadline);
        editTextField = findViewById(R.id.editTextField);
        editTextJobType = findViewById(R.id.editTextJobType);
        editTextSeniority = findViewById(R.id.editTextSeniority);
        editTextCompanyLogoUrl = findViewById(R.id.editTextCompanyLogoUrl);
        editTextLatitude = findViewById(R.id.editTextLatitude);
        editTextLongitude = findViewById(R.id.editTextLongitude);
        btnSave = findViewById(R.id.btnSave);

        internshipToEdit = (Internship) getIntent().getSerializableExtra("internship");
        if (internshipToEdit != null) {
            editTextTitle.setText(internshipToEdit.getTitle());
            editTextLocation.setText(internshipToEdit.getLocation());
            editTextSalary.setText(internshipToEdit.getSalary());
            editTextCompanyName.setText(internshipToEdit.getCompanyName());
            editTextDescription.setText(internshipToEdit.getDescription());
            editTextRequirement.setText(internshipToEdit.getRequirements());
            if (internshipToEdit.getDeadline() != null)
                editTextDeadline.setText(internshipToEdit.getDeadline().toString());
            editTextField.setText(internshipToEdit.getField());
            editTextJobType.setText(internshipToEdit.getJobType());
            editTextSeniority.setText(internshipToEdit.getSeniority());
            editTextCompanyLogoUrl.setText(internshipToEdit.getCompanyLogoUrl());
            if (internshipToEdit.getLatitude() != null)
                editTextLatitude.setText(String.valueOf(internshipToEdit.getLatitude()));
            if (internshipToEdit.getLongitude() != null)
                editTextLongitude.setText(String.valueOf(internshipToEdit.getLongitude()));
        }

        btnSave.setOnClickListener(v -> saveJob());
    }

    private void saveJob() {
        String title = editTextTitle.getText().toString().trim();
        String location = editTextLocation.getText().toString().trim();
        String salary = editTextSalary.getText().toString().trim();
        String companyName = editTextCompanyName.getText().toString().trim();
        String description = editTextDescription.getText().toString().trim();
        String requirement = editTextRequirement.getText().toString().trim();
        String deadlineStr = editTextDeadline.getText().toString().trim();
        String field = editTextField.getText().toString().trim();
        String jobType = editTextJobType.getText().toString().trim();
        String seniority = editTextSeniority.getText().toString().trim();
        String companyLogoUrl = editTextCompanyLogoUrl.getText().toString().trim();
        String latitudeStr = editTextLatitude.getText().toString().trim();
        String longitudeStr = editTextLongitude.getText().toString().trim();

        // Validate tất cả trường bắt buộc
        if (title.isEmpty() || location.isEmpty() || salary.isEmpty() ||
                companyName.isEmpty() || description.isEmpty() || requirement.isEmpty() ||
                deadlineStr.isEmpty() || field.isEmpty() || jobType.isEmpty() ||
                seniority.isEmpty() || companyLogoUrl.isEmpty() ||
                latitudeStr.isEmpty() || longitudeStr.isEmpty()) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        // Convert deadline (yyyy-MM-dd) to Timestamp
        com.google.firebase.Timestamp deadline;
        try {
            java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd");
            Date date = sdf.parse(deadlineStr);
            deadline = new com.google.firebase.Timestamp(date);
        } catch (Exception e) {
            Toast.makeText(this, "Invalid deadline format (yyyy-MM-dd)!", Toast.LENGTH_SHORT).show();
            return;
        }

        // Convert latitude, longitude to Double
        Double latitude, longitude;
        try {
            latitude = Double.parseDouble(latitudeStr);
            longitude = Double.parseDouble(longitudeStr);
        } catch (Exception e) {
            Toast.makeText(this, "Invalid latitude or longitude!", Toast.LENGTH_SHORT).show();
            return;
        }

        String id = (internshipToEdit != null)
                ? internshipToEdit.getId()
                : UUID.randomUUID().toString();

        Internship internship = new Internship();
        internship.setId(id);
        internship.setTitle(title);
        internship.setCompanyName(companyName);
        internship.setLocation(location);
        internship.setField(field);
        internship.setJobType(jobType);
        internship.setSeniority(seniority);
        internship.setTimePosted("Just now"); // tự động gán
        internship.setSalary(salary);
        internship.setCompanyLogoUrl(companyLogoUrl);
        internship.setDatePosted(new com.google.firebase.Timestamp(new Date()));
        internship.setDeadline(deadline);
        internship.setLatitude(latitude);
        internship.setLongitude(longitude);
        internship.setDescription(description);
        internship.setRequirements(requirement);

        // Xử lý Company Firestore
        CollectionReference companyRef = db.collection("companies");
        companyRef.whereEqualTo("name", companyName)
                .get()
                .addOnSuccessListener(querySnapshot -> {
                    if (querySnapshot.isEmpty()) {
                        Map<String, Object> company = new HashMap<>();
                        company.put("name", companyName);
                        company.put("logoUrl", companyLogoUrl);
                        companyRef.add(company)
                                .addOnSuccessListener(docRef -> {
                                    internship.setCompanyId(docRef.getId());
                                    saveInternship(internship);
                                })
                                .addOnFailureListener(e -> {
                                    Toast.makeText(this, "Error creating company: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                });
                    } else {
                        DocumentSnapshot doc = querySnapshot.getDocuments().get(0);
                        internship.setCompanyId(doc.getId());
                        saveInternship(internship);
                    }
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Error checking company: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

    private void saveInternship(Internship internship) {
        db.collection("internships")
                .document(internship.getId())
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
