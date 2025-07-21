package com.prm392_ass.ass_prm392.entity;

import java.security.Timestamp;

public class Interview {
    private String applicationId;
    private String employerId;
    private String studentId;
    private Timestamp schedule;
    private String status;


    public Interview() {
    }

    public Interview(String applicationId, String employerId, String studentId, Timestamp schedule, String status) {
        this.applicationId = applicationId;
        this.employerId = employerId;
        this.studentId = studentId;
        this.schedule = schedule;
        this.status = status;

    }

    public String getApplicationId() {
        return applicationId;
    }

    public void setApplicationId(String applicationId) {
        this.applicationId = applicationId;
    }

    public String getEmployerId() {
        return employerId;
    }

    public void setEmployerId(String employerId) {
        this.employerId = employerId;
    }

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public Timestamp getSchedule() {
        return schedule;
    }

    public void setSchedule(Timestamp schedule) {
        this.schedule = schedule;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
