package com.hospital.model;

/**
 * Admin user with full system access.
 * Demonstrates: Inheritance, Method Overriding, Polymorphism
 */
public class Admin extends User {
    private String department;
    private String accessLevel; // SUPER_ADMIN, ADMIN
    
    // Default Constructor
    public Admin() {
        super();
    }
    
    // Parameterized Constructor
    public Admin(String userId, String username, String password, String fullName,
                 String email, String phone, String department, String accessLevel) {
        super(userId, username, password, fullName, email, phone, "ADMIN");
        this.department = department;
        this.accessLevel = accessLevel;
    }
    
    public String getDepartment() { return department; }
    public void setDepartment(String department) { this.department = department; }
    
    public String getAccessLevel() { return accessLevel; }
    public void setAccessLevel(String accessLevel) { this.accessLevel = accessLevel; }
    
    // Method Overriding - Polymorphism
    @Override
    public String getDisplayInfo() {
        return "Admin: " + getFullName() + " | Department: " + department + 
               " | Access: " + accessLevel;
    }
    
    @Override
    public String toString() {
        return "Admin{" +
                "userId='" + getUserId() + '\'' +
                ", fullName='" + getFullName() + '\'' +
                ", department='" + department + '\'' +
                ", accessLevel='" + accessLevel + '\'' +
                '}';
    }
}
