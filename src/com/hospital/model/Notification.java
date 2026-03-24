package com.hospital.model;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Notification entity for system notifications.
 */
public class Notification implements Serializable {
    private String notificationId;
    private String recipientUserId;
    private String recipientRole;
    private String title;
    private String message;
    private String type;
    private boolean isRead;
    private LocalDateTime createdAt;
    
    public Notification() {
        this.isRead = false;
        this.createdAt = LocalDateTime.now();
    }
    
    public Notification(String notificationId, String recipientUserId, String recipientRole,
                       String title, String message, String type) {
        this();
        this.notificationId = notificationId;
        this.recipientUserId = recipientUserId;
        this.recipientRole = recipientRole;
        this.title = title;
        this.message = message;
        this.type = type;
    }
    
    public String getNotificationId() { return notificationId; }
    public void setNotificationId(String notificationId) { this.notificationId = notificationId; }
    
    public String getRecipientUserId() { return recipientUserId; }
    public void setRecipientUserId(String recipientUserId) { 
        this.recipientUserId = recipientUserId; 
    }
    
    public String getRecipientRole() { return recipientRole; }
    public void setRecipientRole(String recipientRole) { this.recipientRole = recipientRole; }
    
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    
    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
    
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
    
    public boolean isRead() { return isRead; }
    public void setRead(boolean read) { isRead = read; }
    
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
