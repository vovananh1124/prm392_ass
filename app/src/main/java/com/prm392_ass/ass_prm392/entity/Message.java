package com.prm392_ass.ass_prm392.entity;

import java.security.Timestamp;

public class Message {
    private String fromId;
    private String toId;
    private String message;
    private Timestamp timestamp;


    public Message() {
    }

    public Message(String fromId, String toId, String message, Timestamp timestamp) {
        this.fromId = fromId;
        this.toId = toId;
        this.message = message;
        this.timestamp = timestamp;
    }


    public String getFromId() {
        return fromId;
    }

    public void setFromId(String fromId) {
        this.fromId = fromId;
    }

    public String getToId() {
        return toId;
    }

    public void setToId(String toId) {
        this.toId = toId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }
}
