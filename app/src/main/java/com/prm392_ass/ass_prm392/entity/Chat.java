// com.prm392_ass.ass_prm392.entity.Chat.java
package com.prm392_ass.ass_prm392.entity;

import java.util.List;

public class Chat {
    private String chatId;
    private List<String> participants; // studentId, recruiterId
    private String lastMessage;
    private long lastUpdated;
    private boolean hasUnread;

    public Chat() {}

    public Chat(String chatId, List<String> participants, String lastMessage, long lastUpdated, boolean hasUnread) {
        this.chatId = chatId;
        this.participants = participants;
        this.lastMessage = lastMessage;
        this.lastUpdated = lastUpdated;
        this.hasUnread = hasUnread;
    }

    public String getChatId() { return chatId; }
    public void setChatId(String chatId) { this.chatId = chatId; }

    public List<String> getParticipants() { return participants; }
    public void setParticipants(List<String> participants) { this.participants = participants; }

    public String getLastMessage() { return lastMessage; }
    public void setLastMessage(String lastMessage) { this.lastMessage = lastMessage; }

    public long getLastUpdated() { return lastUpdated; }
    public void setLastUpdated(long lastUpdated) { this.lastUpdated = lastUpdated; }

    public boolean isHasUnread() { return hasUnread; }
    public void setHasUnread(boolean hasUnread) { this.hasUnread = hasUnread; }
}
