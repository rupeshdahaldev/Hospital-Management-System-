package com.hospital.service;

import com.hospital.data.NotificationRepository;
import com.hospital.model.Appointment;
import com.hospital.model.Notification;
import com.hospital.util.IdGenerator;
import java.util.List;

public class NotificationService {
    private final NotificationRepository notificationRepository;

    public NotificationService() {
        this.notificationRepository = new NotificationRepository();
    }

    public void sendAppointmentNotification(Appointment appointment) {
        String doctorUserId  = "U-" + appointment.getDoctorId();
        String patientUserId = "U-" + appointment.getPatientId();

        Notification doctorNotif = new Notification(
            IdGenerator.generateNotificationId(),
            doctorUserId,
            "DOCTOR",
            "New Appointment",
            "New appointment scheduled for " + appointment.getAppointmentDate()
                    + " at " + appointment.getTimeSlot(),
            "APPOINTMENT"
        );
        notificationRepository.save(doctorNotif);

        Notification patientNotif = new Notification(
            IdGenerator.generateNotificationId(),
            patientUserId,
            "PATIENT",
            "Appointment Confirmed",
            "Your appointment is confirmed for " + appointment.getAppointmentDate()
                    + " at " + appointment.getTimeSlot(),
            "APPOINTMENT"
        );
        notificationRepository.save(patientNotif);
    }

    public List<Notification> getUnreadNotificationsForUser(String userId) {
        return notificationRepository.findUnreadByRecipientId(userId);
    }
}

