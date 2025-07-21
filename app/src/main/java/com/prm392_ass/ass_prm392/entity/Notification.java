package com.prm392_ass.ass_prm392.entity;

import java.security.Timestamp;

public class Notification {
    private String userId;
    private String message;
    private String type;
    private boolean isRead;
    private Timestamp createdAt;

    public Notification() {
    }

    public Notification(String userId, String message, String type, boolean isRead, Timestamp createdAt) {
        this.userId = userId;
        this.message = message;
        this.type = type;
        this.isRead = isRead;
        this.createdAt = createdAt;
    }


    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public boolean isRead() {
        return isRead;
    }

    public void setRead(boolean read) {
        isRead = read;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }
}
