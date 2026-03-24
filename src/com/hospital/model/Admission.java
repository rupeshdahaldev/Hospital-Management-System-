package com.hospital.model;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * Admission entity to track patient admissions.
 */
public class Admission implements Serializable {
    private String admissionId;
    private String patientId;
    private String bedId;
    private LocalDate admitDate;
    private LocalDate dischargeDate;
    private String admittingDoctorId;
    private String reason;
    private double totalCharges;
    private String status;
    
    public Admission() {
        this.status = "ACTIVE";
    }
    
    public Admission(String admissionId, String patientId, String bedId,
                    LocalDate admitDate, String admittingDoctorId, String reason) {
        this();
        this.admissionId = admissionId;
        this.patientId = patientId;
        this.bedId = bedId;
        this.admitDate = admitDate;
        this.admittingDoctorId = admittingDoctorId;
        this.reason = reason;
    }
    
    public String getAdmissionId() { return admissionId; }
    public void setAdmissionId(String admissionId) { this.admissionId = admissionId; }
    
    public String getPatientId() { return patientId; }
    public void setPatientId(String patientId) { this.patientId = patientId; }
    
    public String getBedId() { return bedId; }
    public void setBedId(String bedId) { this.bedId = bedId; }
    
    public LocalDate getAdmitDate() { return admitDate; }
    public void setAdmitDate(LocalDate admitDate) { this.admitDate = admitDate; }
    
    public LocalDate getDischargeDate() { return dischargeDate; }
    public void setDischargeDate(LocalDate dischargeDate) { this.dischargeDate = dischargeDate; }
    
    public String getAdmittingDoctorId() { return admittingDoctorId; }
    public void setAdmittingDoctorId(String admittingDoctorId) { 
        this.admittingDoctorId = admittingDoctorId; 
    }
    
    public String getReason() { return reason; }
    public void setReason(String reason) { this.reason = reason; }
    
    public double getTotalCharges() { return totalCharges; }
    public void setTotalCharges(double totalCharges) { this.totalCharges = totalCharges; }
    
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}
