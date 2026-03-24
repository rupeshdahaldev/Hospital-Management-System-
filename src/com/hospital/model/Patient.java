package com.hospital.model;

/**
 * Patient user with limited access.
 * Demonstrates: Inheritance, Method Overriding, Polymorphism
 */
public class Patient extends User {
    private String patientId;
    private int age;
    private String gender;
    private String bloodGroup;
    private String address;
    private String emergencyContact;
    private String assignedDoctorId;
    private String medicalHistory;
    
    // Default Constructor
    public Patient() {
        super();
    }
    
    // Parameterized Constructor
    public Patient(String userId, String username, String password, String fullName,
                   String email, String phone, String patientId, int age, String gender,
                   String bloodGroup, String address, String emergencyContact) {
        super(userId, username, password, fullName, email, phone, "PATIENT");
        this.patientId = patientId;
        this.age = age;
        this.gender = gender;
        this.bloodGroup = bloodGroup;
        this.address = address;
        this.emergencyContact = emergencyContact;
        this.medicalHistory = "";
    }
    
    public String getPatientId() { return patientId; }
    public void setPatientId(String patientId) { this.patientId = patientId; }
    
    public int getAge() { return age; }
    public void setAge(int age) { this.age = age; }
    
    public String getGender() { return gender; }
    public void setGender(String gender) { this.gender = gender; }
    
    public String getBloodGroup() { return bloodGroup; }
    public void setBloodGroup(String bloodGroup) { this.bloodGroup = bloodGroup; }
    
    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }
    
    public String getEmergencyContact() { return emergencyContact; }
    public void setEmergencyContact(String emergencyContact) { 
        this.emergencyContact = emergencyContact; 
    }
    
    public String getAssignedDoctorId() { return assignedDoctorId; }
    public void setAssignedDoctorId(String assignedDoctorId) { 
        this.assignedDoctorId = assignedDoctorId; 
    }
    
    public String getMedicalHistory() { return medicalHistory; }
    public void setMedicalHistory(String medicalHistory) { 
        this.medicalHistory = medicalHistory; 
    }
    
    @Override
    public String getDisplayInfo() {
        return "Patient: " + getFullName() + " | Age: " + age + 
               " | Blood Group: " + bloodGroup + " | Gender: " + gender;
    }
    
    @Override
    public String toString() {
        return "Patient{" +
                "patientId='" + patientId + '\'' +
                ", fullName='" + getFullName() + '\'' +
                ", age=" + age +
                ", gender='" + gender + '\'' +
                ", bloodGroup='" + bloodGroup + '\'' +
                '}';
    }
}
