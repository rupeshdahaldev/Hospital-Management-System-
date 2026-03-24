package com.hospital.model;

import java.io.Serializable;

/**
 * Abstract base class for all users in the system.
 * Demonstrates: Abstract Class, Encapsulation, Inheritance
 */
public abstract class User implements Serializable {
    private String userId;
    private String username;
    private String password;
    private String fullName;
    private String email;
    private String phone;
    private String role; // ADMIN, DOCTOR, PATIENT
    
    // Default Constructor
    public User() {
    }
    
    // Parameterized Constructor
    public User(String userId, String username, String password, String fullName, 
                String email, String phone, String role) {
        this.userId = userId;
        this.username = username;
        this.password = password;
        this.fullName = fullName;
        this.email = email;
        this.phone = phone;
        this.role = role;
    }
    
    // Getters and Setters (Encapsulation)
    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }
    
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    
    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }
    
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
    
    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }
    
    // Abstract method (to be implemented by subclasses - Polymorphism)
    public abstract String getDisplayInfo();
    
    @Override
    public String toString() {
        return "User{" +
                "userId='" + userId + '\'' +
                ", username='" + username + '\'' +
                ", fullName='" + fullName + '\'' +
                ", role='" + role + '\'' +
                '}';
    }
}
