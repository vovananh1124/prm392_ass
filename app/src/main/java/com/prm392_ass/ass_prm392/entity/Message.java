// com.prm392_ass.ass_prm392.entity.Message.java
package com.prm392_ass.ass_prm392.entity;

public class Message {
    private String senderId;
    private String content;
    private long timestamp;
    private boolean isSeen;

    public Message() {}

    public Message(String senderId, String content, long timestamp, boolean isSeen) {
        this.senderId = senderId;
        this.content = content;
        this.timestamp = timestamp;
        this.isSeen = isSeen;
    }

    public String getSenderId() { return senderId; }
    public void setSenderId(String senderId) { this.senderId = senderId; }

    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }

    public long getTimestamp() { return timestamp; }
    public void setTimestamp(long timestamp) { this.timestamp = timestamp; }

    public boolean isSeen() { return isSeen; }
    public void setSeen(boolean seen) { isSeen = seen; }
}
