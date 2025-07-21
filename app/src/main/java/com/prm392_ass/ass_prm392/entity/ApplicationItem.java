package com.prm392_ass.ass_prm392.entity;

public class ApplicationItem {
    private String jobTitle;
    private String companyName;
    private String companyLogoUrl;
    private String status;
    private String internshipId;


    public ApplicationItem() {
    }

    public ApplicationItem(String jobTitle, String companyName, String companyLogoUrl, String status, String internshipId) {
        this.jobTitle = jobTitle;
        this.companyName = companyName;
        this.companyLogoUrl = companyLogoUrl;
        this.status = status;
        this.internshipId = internshipId;
    }

    public String getJobTitle() {
        return jobTitle;
    }

    public void setJobTitle(String jobTitle) {
        this.jobTitle = jobTitle;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getCompanyLogoUrl() {
        return companyLogoUrl;
    }

    public void setCompanyLogoUrl(String companyLogoUrl) {
        this.companyLogoUrl = companyLogoUrl;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getInternshipId() {
        return internshipId;
    }

    public void setInternshipId(String internshipId) {
        this.internshipId = internshipId;
    }
}
