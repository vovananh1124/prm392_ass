package com.prm392_ass.ass_prm392.entity;

import com.google.firebase.Timestamp;

public class Application {
    private String studentId;
    private String internshipId;
    private String resumeUrl;
    private String status;
    private Timestamp appliedAt;

    public Application() {
    }

    public Application(String studentId, String internshipId, String resumeUrl, String status, Timestamp appliedAt) {
        this.studentId = studentId;
        this.internshipId = internshipId;
        this.resumeUrl = resumeUrl;
        this.status = status;
        this.appliedAt = appliedAt;
    }

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public String getInternshipId() {
        return internshipId;
    }

    public void setInternshipId(String internshipId) {
        this.internshipId = internshipId;
    }

    public String getResumeUrl() {
        return resumeUrl;
    }

    public void setResumeUrl(String resumeUrl) {
        this.resumeUrl = resumeUrl;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Timestamp getAppliedAt() {
        return appliedAt;
    }

    public void setAppliedAt(Timestamp appliedAt) {
        this.appliedAt = appliedAt;
    }
}
