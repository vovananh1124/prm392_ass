package com.prm392_ass.ass_prm392.entity;

public class User {
    private String name;
    private String email;
    private String role;
    private String university;
    private String companyName;

    public User() {}

    public User(String name, String email, String role, String university, String companyName) {
        this.name = name;
        this.email = email;
        this.role = role;
        this.university = university;
        this.companyName = companyName;
    }

    // Getters và Setters
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }

    public String getUniversity() { return university; }
    public void setUniversity(String university) { this.university = university; }

    public String getCompanyName() { return companyName; }
    public void setCompanyName(String companyName) { this.companyName = companyName; }
}