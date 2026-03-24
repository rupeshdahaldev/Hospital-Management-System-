package com.hospital.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class IdGenerator {
    
    public static String generateId(String prefix) {
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
        return prefix + "-" + timestamp + "-" + getRandomString(4);
    }
    
    public static String generateId(String prefix, int sequenceNumber) {
        return prefix + "-" + String.format("%06d", sequenceNumber);
    }
    
    private static String getRandomString(int length) {
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < length; i++) {
            result.append(chars.charAt((int) (Math.random() * chars.length())));
        }
        return result.toString();
    }
    
    public static String generatePatientId(int sequence) {
        return "PAT-" + String.format("%06d", sequence);
    }
    
    public static String generateDoctorId(int sequence) {
        return "DOC-" + String.format("%06d", sequence);
    }
    
    public static String generateAppointmentId() {
        return generateId("APT");
    }
    
    public static String generateAdmissionId() {
        return generateId("ADM");
    }
    
    public static String generateNotificationId() {
        return generateId("NOT");
    }
}
