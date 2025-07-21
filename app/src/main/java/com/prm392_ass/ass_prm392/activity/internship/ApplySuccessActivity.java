package com.prm392_ass.ass_prm392.activity.internship;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.prm392_ass.ass_prm392.R;
import com.prm392_ass.ass_prm392.activity.home.HomeActivity;

public class ApplySuccessActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_apply_success);

        // Nhận data từ Intent
        String jobTitle = getIntent().getStringExtra("jobTitle");
        String company = getIntent().getStringExtra("company");
        String location = getIntent().getStringExtra("location");
        String date = getIntent().getStringExtra("date");
        String fileName = getIntent().getStringExtra("fileName");

        // Gán vào UI
        TextView tvJobTitle = findViewById(R.id.tvJobTitle);
        TextView tvCompanyAndLocation = findViewById(R.id.tvCompanyAndLocation);
        TextView tvPdfFileName = findViewById(R.id.tvPdfFileName);

        tvJobTitle.setText(jobTitle != null ? jobTitle : "");
        tvCompanyAndLocation.setText(
                (company != null ? company : "") +
                        (location != null ? "  •  " + location : "") +
                        (date != null ? "  •  " + date : "")
        );
        tvPdfFileName.setText(fileName != null ? fileName : "");

        // Nút "Back to Home"
        Button btnBackHome = findViewById(R.id.btnBackHome);
        btnBackHome.setOnClickListener(v -> {
            Intent intent = new Intent(ApplySuccessActivity.this, HomeActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        });
    }
}
