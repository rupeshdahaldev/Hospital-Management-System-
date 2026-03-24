package com.hospital.service;

import com.hospital.data.UserRepository;
import com.hospital.exception.*;
import com.hospital.model.Doctor;
import com.hospital.util.IdGenerator;
import com.hospital.util.ValidationUtil;
import java.util.List;

public class DoctorService {
    private final UserRepository userRepository;
    
    public DoctorService() {
        this.userRepository = new UserRepository();
    }
    
    public Doctor addDoctor(String username, String password, String fullName,
                           String email, String phone, String specialization,
                           String qualification, int experienceYears, String department)
            throws DuplicateEntityException, InvalidInputException {
        
        ValidationUtil.validateNotEmpty(username, "Username");
        ValidationUtil.validatePassword(password);
        ValidationUtil.validateNotEmpty(fullName, "Full Name");
        ValidationUtil.validateEmail(email);
        ValidationUtil.validatePhone(phone);
        
        if (userRepository.findByUsername(username) != null) {
            throw new DuplicateEntityException("Username already exists");
        }
        
        List<Doctor> doctors = getDoctors();
        int sequence = doctors.size() + 1;
        String doctorId = IdGenerator.generateDoctorId(sequence);
        String userId = "U-" + doctorId;
        
        Doctor doctor = new Doctor(userId, username, password, fullName,
                                  email, phone, doctorId, specialization,
                                  qualification, experienceYears, department);
        
        userRepository.save(doctor);
        return doctor;
    }
    
    public void updateDoctor(Doctor doctor) throws InvalidInputException, EntityNotFoundException {
        if (!userRepository.exists(doctor.getUserId())) {
            throw new EntityNotFoundException("Doctor not found");
        }
        
        ValidationUtil.validateEmail(doctor.getEmail());
        ValidationUtil.validatePhone(doctor.getPhone());
        
        userRepository.update(doctor);
    }
    
    public Doctor findDoctorById(String doctorId) throws EntityNotFoundException {
        List<Doctor> doctors = getDoctors();
        return doctors.stream()
                .filter(d -> d.getDoctorId().equals(doctorId))
                .findFirst()
                .orElseThrow(() -> new EntityNotFoundException("Doctor not found: " + doctorId));
    }
    
    public List<Doctor> getDoctors() {
        return userRepository.findByRole("DOCTOR").stream()
                .filter(user -> user instanceof Doctor)
                .map(user -> (Doctor) user)
                .toList();
    }
    
    public void deleteDoctor(String doctorId) throws EntityNotFoundException {
        Doctor doctor = findDoctorById(doctorId);
        userRepository.delete(doctor.getUserId());
    }
}
