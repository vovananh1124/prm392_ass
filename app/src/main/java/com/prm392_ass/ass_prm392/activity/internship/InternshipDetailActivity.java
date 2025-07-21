package com.prm392_ass.ass_prm392.activity.internship;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.Source;
import com.prm392_ass.ass_prm392.R;
import com.prm392_ass.ass_prm392.activity.map.MapActivity;
import com.prm392_ass.ass_prm392.entity.Internship;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class InternshipDetailActivity extends AppCompatActivity {

    private ProgressBar progressBar;
    private LinearLayout contentLayout;
    private TextView tvJobTitle, tvCompany, tvLocation, tvDate,
            tvDescription, tvRequirements, tvSalary;
    private ImageView imgCompanyLogo;
    private Button btnUploadCV, btnApply, btnViewMap;

    private Internship currentInternship;
    private Uri selectedCvUri;
    private String selectedCvFileName = "";
    private String currentApplicationId = null; // Lưu ID application nếu đã nộp

    // Launcher để chọn CV từ Drive
    private final ActivityResultLauncher<String[]> pickCvLauncher =
            registerForActivityResult(
                    new ActivityResultContracts.OpenDocument(),
                    uri -> {
                        if (uri != null) {
                            // Persist permission
                            try {
                                getContentResolver().takePersistableUriPermission(
                                        uri,
                                        Intent.FLAG_GRANT_READ_URI_PERMISSION
                                );
                            } catch (Exception ignored) { }

                            selectedCvUri = uri;
                            selectedCvFileName = queryFileName(uri);
                            btnUploadCV.setText("ĐÃ CHỌN: " + selectedCvFileName);
                            btnUploadCV.setBackgroundColor(
                                    ContextCompat.getColor(this, android.R.color.holo_green_light)
                            );
                        }
                    }
            );

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_internship_detail);

        // Ánh xạ view
        progressBar     = findViewById(R.id.progressBar);
        contentLayout   = findViewById(R.id.contentLayout); // Layout chứa toàn bộ nội dung, ban đầu bị ẩn
        tvJobTitle      = findViewById(R.id.tvJobTitle);
        tvCompany       = findViewById(R.id.tvCompany);
        tvLocation      = findViewById(R.id.tvLocation);
        tvDate          = findViewById(R.id.tvDate);
        tvDescription   = findViewById(R.id.tvDescription);
        tvRequirements  = findViewById(R.id.tvRequirements);
        tvSalary        = findViewById(R.id.tvSalary);
        imgCompanyLogo  = findViewById(R.id.imgCompanyLogo);
        btnUploadCV     = findViewById(R.id.btnUploadCV);
        btnApply        = findViewById(R.id.btnApply);
        btnViewMap      = findViewById(R.id.btnViewMap);

        // Mặc định ẩn nội dung, hiện loading
        contentLayout.setVisibility(LinearLayout.GONE);
        progressBar.setVisibility(ProgressBar.VISIBLE);

        // Nút Upload CV
        btnUploadCV.setOnClickListener(v ->
                pickCvLauncher.launch(new String[]{"application/pdf"})
        );
        // Nút Apply
        btnApply.setOnClickListener(v -> submitApplication());

        // Nút Xem bản đồ (nếu cần)
        btnViewMap.setOnClickListener(v -> {
            if (currentInternship != null
                    && currentInternship.getLatitude() != null
                    && currentInternship.getLongitude() != null) {
                Intent mapIntent = new Intent(this, MapActivity.class);
                mapIntent.putExtra("lat", currentInternship.getLatitude());
                mapIntent.putExtra("lng", currentInternship.getLongitude());
                mapIntent.putExtra("company", currentInternship.getCompanyName());
                startActivity(mapIntent);
            } else {
                Toast.makeText(this,
                        "Chưa có vị trí công ty!", Toast.LENGTH_SHORT).show();
            }
        });



        // Lấy internshipId từ Intent và validate
        String internshipId = getIntent().getStringExtra("internshipId");
        if (internshipId == null || internshipId.isEmpty()) {
            Toast.makeText(this,
                    "Không có internshipId", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        loadInternshipDetail(internshipId);
    }

    private void loadInternshipDetail(@NonNull String internshipId) {
        progressBar.setVisibility(ProgressBar.VISIBLE);
        contentLayout.setVisibility(LinearLayout.GONE);

        FirebaseFirestore.getInstance()
                .collection("internships")
                .document(internshipId)
                .get(Source.SERVER)
                .addOnSuccessListener(doc -> {
                    Internship it = doc.toObject(Internship.class);
                    if (it == null) {
                        Toast.makeText(this,
                                "Không tìm thấy thông tin", Toast.LENGTH_SHORT).show();
                        finish();
                        return;
                    }
                    currentInternship = it;
                    // Hiển thị dữ liệu
                    tvJobTitle.setText(it.getTitle());
                    tvCompany .setText(it.getCompanyName());
                    tvLocation.setText(it.getLocation());
                    tvDescription.setText(it.getDescription());
                    tvRequirements.setText(it.getRequirements());
                    tvSalary.setText(it.getSalary());

                    // Logo công ty
                    String logo = it.getCompanyLogoUrl();
                    if (logo != null && !logo.isEmpty()) {
                        Glide.with(this)
                                .load(logo)
                                .placeholder(R.drawable.a)
                                .error(R.drawable.a)
                                .into(imgCompanyLogo);
                    } else {
                        imgCompanyLogo.setImageResource(R.drawable.a);
                    }

                    // Xử lý ngày đăng & deadline
                    Timestamp posted = it.getDatePosted();
                    Timestamp deadline = it.getDeadline();
                    if (deadline != null
                            && deadline.toDate().before(new Date())) {
                        tvDate.setText("Đã hết hạn");
                        btnUploadCV.setEnabled(false);
                        btnUploadCV.setText("ĐÃ HẾT HẠN");
                        btnUploadCV.setBackgroundColor(
                                ContextCompat.getColor(this,
                                        android.R.color.holo_red_light));
                        btnUploadCV.setTextColor(
                                ContextCompat.getColor(this,
                                        android.R.color.white));
                        btnApply.setEnabled(false);
                    } else {
                        String ago = "N/A";
                        if (posted != null) {
                            long diff = System.currentTimeMillis()
                                    - posted.toDate().getTime();
                            long days = diff / (1000L*60*60*24);
                            ago = days == 0
                                    ? "Today"
                                    : days + " ngày trước";
                        }
                        tvDate.setText(ago);
                        // Sau khi đã hiện dữ liệu, kiểm tra đã ứng tuyển chưa!
                        checkApplication();
                    }
                    // SHOW nội dung sau khi load xong!
                    contentLayout.setVisibility(LinearLayout.VISIBLE);
                    progressBar.setVisibility(ProgressBar.GONE);
                })
                .addOnFailureListener(e -> {
                    progressBar.setVisibility(ProgressBar.GONE);
                    Toast.makeText(this,
                            "Lỗi tải dữ liệu: " + e.getMessage(),
                            Toast.LENGTH_SHORT).show();
                });
    }

    private void checkApplication() {
        String uid = FirebaseAuth.getInstance().getUid();
        if (uid == null || currentInternship == null) return;

        FirebaseFirestore.getInstance().collection("applications")
                .whereEqualTo("studentId", uid)
                .whereEqualTo("internshipId", currentInternship.getId())
                .get()
                .addOnSuccessListener(query -> {
                    if (!query.isEmpty()) {
                        // Đã ứng tuyển, lấy id của doc đó
                        DocumentSnapshot doc = query.getDocuments().get(0);
                        currentApplicationId = doc.getId();
                        btnUploadCV.setText("UPDATE CV");
                        btnApply.setText("CẬP NHẬT CV");
                    } else {
                        currentApplicationId = null;
                        btnUploadCV.setText("UPLOAD CV/RESUME");
                        btnApply.setText("APPLY NOW");
                    }
                });
    }

    private void submitApplication() {
        if (currentInternship == null) return;
        if (selectedCvUri == null) {
            Toast.makeText(this,
                    "Vui lòng chọn CV trước khi nộp.",
                    Toast.LENGTH_SHORT).show();
            return;
        }

        btnApply.setEnabled(false);
        String uid = FirebaseAuth.getInstance().getUid();
        Map<String,Object> app = new HashMap<>();
        app.put("studentId", uid);
        app.put("internshipId", currentInternship.getId());
        app.put("resumeUrl", selectedCvUri.toString());
        app.put("status", "pending");
        app.put("appliedAt", new Timestamp(new Date()));

        if (currentApplicationId == null) {
            // CHƯA ỨNG TUYỂN: Thêm mới
            FirebaseFirestore.getInstance()
                    .collection("applications")
                    .add(app)
                    .addOnSuccessListener(ref -> {
                        onSuccessApply();
                    })
                    .addOnFailureListener(e -> {
                        btnApply.setEnabled(true);
                        Toast.makeText(this, "Lỗi nộp: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    });
        } else {
            // ĐÃ ỨNG TUYỂN: Update
            FirebaseFirestore.getInstance()
                    .collection("applications")
                    .document(currentApplicationId)
                    .update(app)
                    .addOnSuccessListener(ref -> {
                        onSuccessApply();
                    })
                    .addOnFailureListener(e -> {
                        btnApply.setEnabled(true);
                        Toast.makeText(this, "Lỗi cập nhật: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    });
        }
    }

    private void onSuccessApply() {
        Toast.makeText(this, "Ứng tuyển thành công!", Toast.LENGTH_SHORT).show();
        Intent i = new Intent(this, ApplySuccessActivity.class);
        i.putExtra("jobTitle", currentInternship.getTitle());
        i.putExtra("company", currentInternship.getCompanyName());
        i.putExtra("location", currentInternship.getLocation());
        i.putExtra("date", tvDate.getText().toString());
        i.putExtra("fileName", selectedCvFileName);
        startActivity(i);
        finish();
    }

    /** Lấy tên file từ URI */
    private String queryFileName(@NonNull Uri uri) {
        String name = "";
        try (android.database.Cursor c = getContentResolver()
                .query(uri, null, null, null, null)) {
            if (c != null && c.moveToFirst()) {
                int idx = c.getColumnIndex(
                        android.provider.OpenableColumns.DISPLAY_NAME);
                if (idx >= 0) name = c.getString(idx);
            }
        }
        if (name.isEmpty()) {
            String path = uri.getPath();
            int cut = path != null ? path.lastIndexOf('/') : -1;
            name = (cut != -1)
                    ? path.substring(cut + 1)
                    : uri.toString();
        }
        return name;
    }
}
