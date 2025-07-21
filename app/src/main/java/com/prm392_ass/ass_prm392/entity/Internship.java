package com.prm392_ass.ass_prm392.entity;

import com.google.firebase.Timestamp;

import java.io.Serializable;

public class Internship implements Serializable {
    private String id;
    private String companyId;
    private String title;
    private String description;
    private String requirements;
    private String salary;
    private String field;
    private String location;
    private Timestamp datePosted;
    private Timestamp deadline;
    private String companyLogoUrl;
    private Double latitude;
    private Double longitude;
    private String companyName;

    private String jobType;
    private String seniority;

    private String timePosted;




    public Internship() {
    }

    public Internship(String id, String companyId, String title, String description, String requirements, String salary, String field, String location, Timestamp datePosted, Timestamp deadline, String companyLogoUrl, Double latitude, Double longitude , String companyName, String jobType, String seniority, String timePosted) {
        this.id = id;
        this.companyId = companyId;
        this.title = title;
        this.description = description;
        this.requirements = requirements;
        this.salary = salary;
        this.field = field;
        this.location = location;
        this.datePosted = datePosted;
        this.deadline = deadline;
        this.companyLogoUrl = companyLogoUrl;
        this.latitude = latitude;
        this.longitude = longitude;
        this.companyName = companyName;
        this.jobType = jobType;
        this.seniority = seniority;
        this.timePosted = timePosted;


    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCompanyId() {
        return companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getRequirements() {
        return requirements;
    }

    public void setRequirements(String requirements) {
        this.requirements = requirements;
    }

    public String getSalary() {
        return salary;
    }

    public void setSalary(String salary) {
        this.salary = salary;
    }

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Timestamp getDatePosted() {
        return datePosted;
    }


    public void setDatePosted(Timestamp datePosted) {
        this.datePosted = datePosted;
    }

    public Timestamp getDeadline() {
        return deadline;
    }

    public void setDeadline(Timestamp deadline) {
        this.deadline = deadline;
    }

    public String getCompanyLogoUrl() {
        return companyLogoUrl;
    }

    public void setCompanyLogoUrl(String companyLogoUrl) {
        this.companyLogoUrl = companyLogoUrl;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getJobType() {
        return jobType;
    }

    public void setJobType(String jobType) {
        this.jobType = jobType;
    }

    public String getSeniority() {
        return seniority;
    }

    public void setSeniority(String seniority) {
        this.seniority = seniority;
    }

    public String getTimePosted() {
        return timePosted;
    }

    public void setTimePosted(String timePosted) {
        this.timePosted = timePosted;
    }
}