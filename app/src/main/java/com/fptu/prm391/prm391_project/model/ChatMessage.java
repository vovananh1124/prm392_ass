package com.fptu.prm391.prm391_project.model;

public class ChatMessage {
    private String senderId;
    private String message;
    private long timestamp;

    public ChatMessage() {} // Bắt buộc cho Firebase

    public ChatMessage(String senderId, String message, long timestamp) {
        this.senderId = senderId;
        this.message = message;
        this.timestamp = timestamp;
    }

    public String getSenderId() { return senderId; }
    public void setSenderId(String senderId) { this.senderId = senderId; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public long getTimestamp() { return timestamp; }
    public void setTimestamp(long timestamp) { this.timestamp = timestamp; }
}
