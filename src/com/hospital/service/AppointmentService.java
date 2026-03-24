package com.hospital.service;

import com.hospital.data.AppointmentRepository;
import com.hospital.exception.*;
import com.hospital.model.Appointment;
import com.hospital.util.IdGenerator;
import java.time.LocalDate;
import java.util.List;

public class AppointmentService {
    private final AppointmentRepository appointmentRepository;
    private final NotificationService notificationService;
    
    public AppointmentService() {
        this.appointmentRepository = new AppointmentRepository();
        this.notificationService = new NotificationService();
    }
    
    public Appointment scheduleAppointment(String patientId, String doctorId,
                                          LocalDate appointmentDate, String timeSlot)
            throws InvalidInputException {
        
        if (patientId == null || doctorId == null) {
            throw new InvalidInputException("Patient ID and Doctor ID are required");
        }
        
        if (appointmentDate == null || appointmentDate.isBefore(LocalDate.now())) {
            throw new InvalidInputException("Appointment date must be today or in the future");
        }
        
        if (!appointmentRepository.isTimeSlotAvailable(doctorId, appointmentDate, timeSlot)) {
            throw new InvalidInputException("Time slot already booked");
        }
        
        String appointmentId = IdGenerator.generateAppointmentId();
        Appointment appointment = new Appointment(appointmentId, patientId, doctorId,
                                                  appointmentDate, timeSlot);
        appointment.setStatus("SCHEDULED");
        
        appointmentRepository.save(appointment);
        notificationService.sendAppointmentNotification(appointment);
        
        return appointment;
    }
    
    public void updateAppointmentStatus(String appointmentId, String status)
            throws EntityNotFoundException, InvalidInputException {
        
        Appointment appointment = findAppointmentById(appointmentId);
        
        if (!List.of("SCHEDULED", "COMPLETED", "CANCELLED").contains(status)) {
            throw new InvalidInputException("Invalid status");
        }
        
        appointment.setStatus(status);
        appointmentRepository.update(appointment);
    }
    
    public void addDiagnosisAndPrescription(String appointmentId, String diagnosis,
                                           String prescription, String notes)
            throws EntityNotFoundException {
        
        Appointment appointment = findAppointmentById(appointmentId);
        appointment.setDiagnosis(diagnosis);
        appointment.setPrescription(prescription);
        appointment.setNotes(notes);
        appointment.setStatus("COMPLETED");
        
        appointmentRepository.update(appointment);
    }
    
    public Appointment findAppointmentById(String appointmentId) throws EntityNotFoundException {
        Appointment appointment = appointmentRepository.findById(appointmentId);
        if (appointment == null) {
            throw new EntityNotFoundException("Appointment not found");
        }
        return appointment;
    }
    
    public List<Appointment> getAllAppointments() {
        return appointmentRepository.findAll();
    }
    
    public List<Appointment> getAppointmentsByPatient(String patientId) {
        return appointmentRepository.findByPatientId(patientId);
    }
    
    public List<Appointment> getAppointmentsByDoctor(String doctorId) {
        return appointmentRepository.findByDoctorId(doctorId);
    }
    
    public void cancelAppointment(String appointmentId) throws EntityNotFoundException, InvalidInputException {
        updateAppointmentStatus(appointmentId, "CANCELLED");
    }
}
