package com.prm392_ass.ass_prm392.activity.home;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.prm392_ass.ass_prm392.R;
import com.prm392_ass.ass_prm392.activity.fragment.ApplyFragment;
import com.prm392_ass.ass_prm392.activity.fragment.ChatFragment;
import com.prm392_ass.ass_prm392.activity.fragment.RecruiterFragment;
import com.prm392_ass.ass_prm392.activity.fragment.SearchFragment;
import com.prm392_ass.ass_prm392.activity.fragment.StudentFragment;

public class HomeActivity extends AppCompatActivity {

    private ImageView navHome, navSearch, navAdd, navChat, navApply;
    private String role;
    private String name;
    private TextView txtGreeting;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        // Get data from Intent or SharedPreferences
        role = getIntent().getStringExtra("role");
        name = getIntent().getStringExtra("name");
        if (name == null) {
            SharedPreferences prefs = getSharedPreferences("UserPrefs", MODE_PRIVATE);
            name = prefs.getString("name", "User");
        }

        // Set greeting
        txtGreeting = findViewById(R.id.txtGreeting);
        if (txtGreeting != null) {
            txtGreeting.setText("Hello, " + name);
        }

        // Map views
        navHome = findViewById(R.id.nav_home);
        navSearch = findViewById(R.id.nav_search);
        navAdd = findViewById(R.id.nav_add);
        navChat = findViewById(R.id.nav_chat);
        navApply = findViewById(R.id.nav_apply);

        // Hide Add button for students
        if ("student".equalsIgnoreCase(role)) {
            navAdd.setVisibility(View.GONE);
        }

        // Show default fragment
        switchFragment("recruiter".equalsIgnoreCase(role) ? new RecruiterFragment() : new StudentFragment());
        setSelectedIcon(navHome);

        // Click events
        navHome.setOnClickListener(v -> {
            setSelectedIcon(navHome);
            switchFragment("recruiter".equalsIgnoreCase(role) ? new RecruiterFragment() : new StudentFragment());
        });

        navSearch.setOnClickListener(v -> {
            setSelectedIcon(navSearch);
            switchFragment(new SearchFragment());
        });

        navChat.setOnClickListener(v -> {
            setSelectedIcon(navChat);
            switchFragment(new ChatFragment());
        });

        navApply.setOnClickListener(v -> {
            setSelectedIcon(navApply);
            switchFragment(new ApplyFragment());
        });

        navAdd.setOnClickListener(v -> {
            // Optional: show posting screen (Recruiter only)
        });
    }

    private void switchFragment(Fragment fragment) {
        boolean isHome = fragment instanceof StudentFragment || fragment instanceof RecruiterFragment;
        if (isHome) {
            getSupportFragmentManager().popBackStack(null, androidx.fragment.app.FragmentManager.POP_BACK_STACK_INCLUSIVE);
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, fragment)
                    .commit();
        } else {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, fragment)
                    .addToBackStack(null)
                    .commit();
        }
    }

    private void setSelectedIcon(ImageView selectedIcon) {
        navHome.setColorFilter(getResources().getColor(R.color.icon_gray));
        navSearch.setColorFilter(getResources().getColor(R.color.icon_gray));
        navChat.setColorFilter(getResources().getColor(R.color.icon_gray));
        navApply.setColorFilter(getResources().getColor(R.color.icon_gray));
        selectedIcon.setColorFilter(getResources().getColor(R.color.primaryDark));
    }
}