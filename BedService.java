package com.hospital.service;

import com.hospital.data.PatientRepository;
import com.hospital.data.UserRepository;
import com.hospital.exception.*;
import com.hospital.model.Patient;
import com.hospital.util.IdGenerator;
import com.hospital.util.ValidationUtil;
import java.util.List;

public class PatientService {
    private final PatientRepository patientRepository;
    private final UserRepository userRepository;
    
    public PatientService() {
        this.patientRepository = new PatientRepository();
        this.userRepository = new UserRepository();
    }
    
    public Patient addPatient(String username, String password, String fullName,
                             String email, String phone, int age, String gender,
                             String bloodGroup, String address, String emergencyContact) 
            throws DuplicateEntityException, InvalidInputException {
        
        ValidationUtil.validateNotEmpty(username, "Username");
        ValidationUtil.validatePassword(password);
        ValidationUtil.validateNotEmpty(fullName, "Full Name");
        ValidationUtil.validateEmail(email);
        ValidationUtil.validatePhone(phone);
        ValidationUtil.validateAge(age);
        
        if (userRepository.findByUsername(username) != null) {
            throw new DuplicateEntityException("Username already exists");
        }
        
        int sequence = patientRepository.findAll().size() + 1;
        String patientId = IdGenerator.generatePatientId(sequence);
        String userId = "U-" + patientId;
        
        Patient patient = new Patient(userId, username, password, fullName,
                                     email, phone, patientId, age, gender,
                                     bloodGroup, address, emergencyContact);
        
        patientRepository.save(patient);
        userRepository.save(patient);
        
        return patient;
    }
    
    public void updatePatient(Patient patient) throws InvalidInputException, EntityNotFoundException {
        if (!patientRepository.exists(patient.getPatientId())) {
            throw new EntityNotFoundException("Patient not found");
        }
        
        ValidationUtil.validateEmail(patient.getEmail());
        ValidationUtil.validatePhone(patient.getPhone());
        
        patientRepository.update(patient);
        userRepository.update(patient);
    }
    
    public Patient findPatientById(String patientId) throws EntityNotFoundException {
        Patient patient = patientRepository.findById(patientId);
        if (patient == null) {
            throw new EntityNotFoundException("Patient not found: " + patientId);
        }
        return patient;
    }
    
    public List<Patient> getAllPatients() {
        return patientRepository.findAll();
    }
    
    public List<Patient> getPatientsByDoctor(String doctorId) {
        return patientRepository.findByDoctorId(doctorId);
    }
    
    public void deletePatient(String patientId) throws EntityNotFoundException {
        Patient patient = patientRepository.findById(patientId);
        if (patient == null) {
            throw new EntityNotFoundException("Patient not found");
        }
        patientRepository.delete(patientId);
        userRepository.delete(patient.getUserId());
    }
}
