package com.hospital.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Doctor user with medical access.
 * Demonstrates: Inheritance, Method Overriding, Polymorphism
 */
public class Doctor extends User {
    private String doctorId;
    private String specialization;
    private String qualification;
    private List<String> availableTimeSlots;
    private int experienceYears;
    private String department;
    
    // Default Constructor
    public Doctor() {
        super();
        this.availableTimeSlots = new ArrayList<>();
    }
    
    // Parameterized Constructor
    public Doctor(String userId, String username, String password, String fullName,
                  String email, String phone, String doctorId, String specialization,
                  String qualification, int experienceYears, String department) {
        super(userId, username, password, fullName, email, phone, "DOCTOR");
        this.doctorId = doctorId;
        this.specialization = specialization;
        this.qualification = qualification;
        this.experienceYears = experienceYears;
        this.department = department;
        this.availableTimeSlots = new ArrayList<>();
    }
    
    public String getDoctorId() { return doctorId; }
    public void setDoctorId(String doctorId) { this.doctorId = doctorId; }
    
    public String getSpecialization() { return specialization; }
    public void setSpecialization(String specialization) { this.specialization = specialization; }
    
    public String getQualification() { return qualification; }
    public void setQualification(String qualification) { this.qualification = qualification; }
    
    public List<String> getAvailableTimeSlots() { return availableTimeSlots; }
    public void setAvailableTimeSlots(List<String> availableTimeSlots) { 
        this.availableTimeSlots = availableTimeSlots; 
    }
    
    public int getExperienceYears() { return experienceYears; }
    public void setExperienceYears(int experienceYears) { this.experienceYears = experienceYears; }
    
    public String getDepartment() { return department; }
    public void setDepartment(String department) { this.department = department; }
    
    @Override
    public String getDisplayInfo() {
        return "Dr. " + getFullName() + " | " + specialization + 
               " | " + qualification + " | " + experienceYears + " years exp.";
    }
    
    @Override
    public String toString() {
        return "Doctor{" +
                "doctorId='" + doctorId + '\'' +
                ", fullName='" + getFullName() + '\'' +
                ", specialization='" + specialization + '\'' +
                ", qualification='" + qualification + '\'' +
                '}';
    }
}
