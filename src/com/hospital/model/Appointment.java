package com.hospital.model;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Appointment entity.
 * Demonstrates: Classes, Objects, Encapsulation, Constructor Overloading
 */
public class Appointment implements Serializable {
    private String appointmentId;
    private String patientId;
    private String doctorId;
    private LocalDate appointmentDate;
    private String timeSlot;
    private String status;
    private String diagnosis;
    private String prescription;
    private String notes;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    
    // Default Constructor
    public Appointment() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
        this.status = "SCHEDULED";
    }
    
    // Parameterized Constructor (Constructor Overloading)
    public Appointment(String appointmentId, String patientId, String doctorId,
                      LocalDate appointmentDate, String timeSlot) {
        this();
        this.appointmentId = appointmentId;
        this.patientId = patientId;
        this.doctorId = doctorId;
        this.appointmentDate = appointmentDate;
        this.timeSlot = timeSlot;
    }
    
    // Full Constructor (Constructor Overloading)
    public Appointment(String appointmentId, String patientId, String doctorId,
                      LocalDate appointmentDate, String timeSlot, String status,
                      String diagnosis, String prescription, String notes) {
        this(appointmentId, patientId, doctorId, appointmentDate, timeSlot);
        this.status = status;
        this.diagnosis = diagnosis;
        this.prescription = prescription;
        this.notes = notes;
    }
    
    public String getAppointmentId() { return appointmentId; }
    public void setAppointmentId(String appointmentId) { this.appointmentId = appointmentId; }
    
    public String getPatientId() { return patientId; }
    public void setPatientId(String patientId) { this.patientId = patientId; }
    
    public String getDoctorId() { return doctorId; }
    public void setDoctorId(String doctorId) { this.doctorId = doctorId; }
    
    public LocalDate getAppointmentDate() { return appointmentDate; }
    public void setAppointmentDate(LocalDate appointmentDate) { 
        this.appointmentDate = appointmentDate; 
    }
    
    public String getTimeSlot() { return timeSlot; }
    public void setTimeSlot(String timeSlot) { this.timeSlot = timeSlot; }
    
    public String getStatus() { return status; }
    public void setStatus(String status) { 
        this.status = status;
        this.updatedAt = LocalDateTime.now();
    }
    
    public String getDiagnosis() { return diagnosis; }
    public void setDiagnosis(String diagnosis) { 
        this.diagnosis = diagnosis;
        this.updatedAt = LocalDateTime.now();
    }
    
    public String getPrescription() { return prescription; }
    public void setPrescription(String prescription) { 
        this.prescription = prescription;
        this.updatedAt = LocalDateTime.now();
    }
    
    public String getNotes() { return notes; }
    public void setNotes(String notes) { 
        this.notes = notes;
        this.updatedAt = LocalDateTime.now();
    }
    
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
    
    @Override
    public String toString() {
        return "Appointment{" +
                "appointmentId='" + appointmentId + '\'' +
                ", patientId='" + patientId + '\'' +
                ", doctorId='" + doctorId + '\'' +
                ", appointmentDate=" + appointmentDate +
                ", timeSlot='" + timeSlot + '\'' +
                ", status='" + status + '\'' +
                '}';
    }
}
