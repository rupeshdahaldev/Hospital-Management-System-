package com.hospital.data;

import com.google.gson.reflect.TypeToken;
import com.hospital.model.Appointment;
import java.lang.reflect.Type;
import java.time.LocalDate;
import java.util.List;

public class AppointmentRepository extends JsonRepository<Appointment> {
    private static final String FILE_PATH = "data/appointments.json";
    private static final Type LIST_TYPE = new TypeToken<List<Appointment>>(){}.getType();
    
    public AppointmentRepository() {
        super(FILE_PATH, LIST_TYPE);
    }
    
    @Override
    protected String getId(Appointment entity) {
        return entity.getAppointmentId();
    }
    
    public List<Appointment> findByPatientId(String patientId) {
        return findAll().stream()
                .filter(apt -> apt.getPatientId().equals(patientId))
                .toList();
    }
    
    public List<Appointment> findByDoctorId(String doctorId) {
        return findAll().stream()
                .filter(apt -> apt.getDoctorId().equals(doctorId))
                .toList();
    }
    
    public boolean isTimeSlotAvailable(String doctorId, LocalDate date, String timeSlot) {
        return findAll().stream()
                .noneMatch(apt -> apt.getDoctorId().equals(doctorId) &&
                                 apt.getAppointmentDate().equals(date) &&
                                 apt.getTimeSlot().equals(timeSlot) &&
                                 apt.getStatus().equals("SCHEDULED"));
    }
}
