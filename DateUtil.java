package com.hospital.util;

import com.hospital.data.UserRepository;
import com.hospital.model.Admin;
import com.hospital.service.*;

public class DataInitializer {

    public static void initializeSampleData() {

        try {
            UserRepository userRepo = new UserRepository();
            Admin admin = new Admin("U-A001", "admin", "admin123", "System Admin",
                    "admin@hospital.com", "1234567890", "IT", "SUPER_ADMIN");
            if (userRepo.findByUsername("admin") == null) {
                userRepo.save(admin);
                System.out.println("Admin account ready.");
            }
        } catch (Exception e) {
            System.err.println("Failed to initialize admin account: " + e.getMessage());
            e.printStackTrace();
        }

        // --- Doctors ---
        try {
            DoctorService doctorService = new DoctorService();
            if (doctorService.getDoctors().isEmpty()) {
                doctorService.addDoctor("doctor1", "doc123", "Dr. Sarah Johnson",
                        "sarah.j@hospital.com", "9876543210", "Cardiology",
                        "MD, FACC", 10, "Cardiology");

                doctorService.addDoctor("doctor2", "doc123", "Dr. Michael Chen",
                        "michael.c@hospital.com", "9876543211", "Neurology",
                        "MD, PhD", 8, "Neurology");

                doctorService.addDoctor("doctor3", "doc123", "Dr. Emily Davis",
                        "emily.d@hospital.com", "9876543212", "Pediatrics",
                        "MD", 5, "Pediatrics");

                System.out.println("Sample doctors created.");
            }
        } catch (Exception e) {
            System.err.println("Failed to initialize sample doctors: " + e.getMessage());
            e.printStackTrace();
        }

        // --- Patients ---
        try {
            PatientService patientService = new PatientService();
            if (patientService.getAllPatients().isEmpty()) {
                patientService.addPatient("patient1", "pat123", "John Doe",
                        "john.doe@email.com", "5551234567", 45, "Male",
                        "O+", "123 Main St", "5559876543");

                patientService.addPatient("patient2", "pat123", "Jane Smith",
                        "jane.smith@email.com", "5551234568", 32, "Female",
                        "A+", "456 Oak Ave", "5559876544");

                System.out.println("Sample patients created.");
            }
        } catch (Exception e) {
            System.err.println("Failed to initialize sample patients: " + e.getMessage());
            e.printStackTrace();
        }

        // --- Beds ---
        try {
            BedService bedService = new BedService();
            if (bedService.getAllBeds().isEmpty()) {
                bedService.addBed("ICU-01", "ICU", 500.0);
                bedService.addBed("ICU-02", "ICU", 500.0);
                bedService.addBed("ICU-03", "ICU", 500.0);
                bedService.addBed("GEN-01", "GENERAL", 200.0);
                bedService.addBed("GEN-02", "GENERAL", 200.0);
                bedService.addBed("GEN-03", "GENERAL", 200.0);
                bedService.addBed("GEN-04", "GENERAL", 200.0);
                bedService.addBed("GEN-05", "GENERAL", 200.0);
                bedService.addBed("EMG-01", "EMERGENCY", 300.0);
                bedService.addBed("EMG-02", "EMERGENCY", 300.0);
                System.out.println("Sample beds created.");
            }
        } catch (Exception e) {
            System.err.println("Failed to initialize beds: " + e.getMessage());
            e.printStackTrace();
        }

        System.out.println("Data initialization complete.");
    }
}
