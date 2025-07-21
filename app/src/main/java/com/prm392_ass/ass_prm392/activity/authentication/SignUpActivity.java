package com.prm392_ass.ass_prm392.activity.authentication;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.prm392_ass.ass_prm392.R;
import com.prm392_ass.ass_prm392.activity.authentication.LoginActivity;

import java.util.HashMap;
import java.util.Map;

public class SignUpActivity extends AppCompatActivity {

    private EditText editTextEmail, editTextPassword, editTextName, editTextUniversity;
    private RadioGroup roleGroup;
    private Button btnSignUp;
    private TextView loginLink;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        mAuth = FirebaseAuth.getInstance(); // NEW
        db = FirebaseFirestore.getInstance();

        editTextEmail = findViewById(R.id.editTextEmail);
        editTextPassword = findViewById(R.id.editTextPassword);
        editTextName = findViewById(R.id.editTextName);
        editTextUniversity = findViewById(R.id.editTextUniversity);
        roleGroup = findViewById(R.id.roleGroup);
        btnSignUp = findViewById(R.id.btnSignUp);
        loginLink = findViewById(R.id.loginLink);

        roleGroup.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId == R.id.radioStudent) {
                editTextUniversity.setVisibility(EditText.VISIBLE);
                editTextUniversity.setHint("University");
                editTextName.setHint("Full Name");
            } else {
                editTextUniversity.setVisibility(EditText.GONE);
                editTextName.setHint("Company Name");
            }
        });

        btnSignUp.setOnClickListener(view -> registerUser());

        loginLink.setOnClickListener(view -> {
            startActivity(new Intent(this, LoginActivity.class));
        });
    }

    private void registerUser() {
        String email = editTextEmail.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();
        String name = editTextName.getText().toString().trim();
        String university = editTextUniversity.getText().toString().trim();
        int selectedId = roleGroup.getCheckedRadioButtonId();
        String role = selectedId == R.id.radioRecruiter ? "recruiter" : "student";

        if (email.isEmpty() || password.isEmpty() || name.isEmpty()
                || (role.equals("student") && university.isEmpty())) {
            Toast.makeText(this, "Please fill in all required fields", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Toast.makeText(this, "Invalid email format", Toast.LENGTH_SHORT).show();
            return;
        }

        if (password.length() < 6) {
            Toast.makeText(this, "Password must be at least 6 characters", Toast.LENGTH_SHORT).show();
            return;
        }

        // NEW: Đăng ký với FirebaseAuth trước
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        // Sau khi tạo user thành công trên Auth, lưu info vào Firestore
                        String uid = mAuth.getCurrentUser().getUid();
                        Map<String, Object> user = new HashMap<>();
                        user.put("email", email);
                        user.put("name", name);
                        user.put("role", role);
                        if (role.equals("student")) {
                            user.put("university", university);
                        }
                        // docId = uid!
                        db.collection("user")
                                .document(uid)
                                .set(user)
                                .addOnSuccessListener(unused -> {
                                    Toast.makeText(this, "Account created successfully", Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(this, LoginActivity.class));
                                    finish();
                                })
                                .addOnFailureListener(e -> {
                                    Toast.makeText(this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                });
                    } else {
                        Toast.makeText(this, "Sign up failed: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
