package com.prm392_ass.ass_prm392.activity.authentication;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.prm392_ass.ass_prm392.R;
import com.prm392_ass.ass_prm392.activity.home.HomeActivity;

public class LoginActivity extends AppCompatActivity {

    EditText editTextEmail, editTextPassword;
    Button btnLogin;
    TextView signUpLink;
    FirebaseAuth mAuth;
    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // NEW: FirebaseAuth instance
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        // Initialize UI components
        editTextEmail = findViewById(R.id.editTextEmail);
        editTextPassword = findViewById(R.id.editTextPassword);
        btnLogin = findViewById(R.id.btnLogin);
        signUpLink = findViewById(R.id.signUpClickable);

        // Login button click
        btnLogin.setOnClickListener(view -> {
            String email = editTextEmail.getText().toString().trim();
            String password = editTextPassword.getText().toString().trim();

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Please enter all fields", Toast.LENGTH_SHORT).show();
                return;
            }

            // NEW: Login via FirebaseAuth
            mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            // Login success - lấy thêm info user từ Firestore
                            FirebaseUser user = mAuth.getCurrentUser();
                            if (user == null) {
                                Toast.makeText(this, "Login failed. Try again!", Toast.LENGTH_SHORT).show();
                                return;
                            }
                            String userId = user.getUid();
                            // Lấy info từ Firestore với docId = uid
                            db.collection("user")
                                    .document(userId)
                                    .get()
                                    .addOnSuccessListener(documentSnapshot -> {
                                        if (documentSnapshot.exists()) {
                                            String role = documentSnapshot.getString("role");
                                            String name = documentSnapshot.getString("name");

                                            Toast.makeText(this, "Welcome " + name, Toast.LENGTH_SHORT).show();
                                            Intent intent = new Intent(this, HomeActivity.class);
                                            intent.putExtra("role", role);
                                            intent.putExtra("name", name);
                                            startActivity(intent);
                                            finish();
                                        } else {
                                            Toast.makeText(this, "User info not found!", Toast.LENGTH_SHORT).show();
                                        }
                                    })
                                    .addOnFailureListener(e -> {
                                        Toast.makeText(this, "Login error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                    });
                        } else {
                            Toast.makeText(this, "Login failed: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
        });

        // Go to sign up screen
        signUpLink.setOnClickListener(view -> {
            startActivity(new Intent(this, SignUpActivity.class));
        });
    }
}
