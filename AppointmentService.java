package com.hospital.service;

import com.hospital.data.AdmissionRepository;
import com.hospital.exception.*;
import com.hospital.model.Admission;
import com.hospital.util.IdGenerator;
import java.time.LocalDate;
import java.util.List;

public class AdmissionService {
    private final AdmissionRepository admissionRepository;
    
    public AdmissionService() {
        this.admissionRepository = new AdmissionRepository();
    }
    
    public List<Admission> getAdmissionsByPatient(String patientId) {
        return admissionRepository.findByPatientId(patientId);
    }
}
