package com.hospital.model;

import java.io.Serializable;

/**
 * Bed entity for hospital bed management.
 */
public class Bed implements Serializable {
    private String bedId;
    private String wardType;
    private String status;
    private String assignedPatientId;
    private double dailyCharge;
    
    public Bed() {
        this.status = "AVAILABLE";
    }
    
    public Bed(String bedId, String wardType, double dailyCharge) {
        this();
        this.bedId = bedId;
        this.wardType = wardType;
        this.dailyCharge = dailyCharge;
    }
    
    public String getBedId() { return bedId; }
    public void setBedId(String bedId) { this.bedId = bedId; }
    
    public String getWardType() { return wardType; }
    public void setWardType(String wardType) { this.wardType = wardType; }
    
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    
    public String getAssignedPatientId() { return assignedPatientId; }
    public void setAssignedPatientId(String assignedPatientId) { 
        this.assignedPatientId = assignedPatientId; 
    }
    
    public double getDailyCharge() { return dailyCharge; }
    public void setDailyCharge(double dailyCharge) { this.dailyCharge = dailyCharge; }
}
