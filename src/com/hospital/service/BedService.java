package com.hospital.service;

import com.hospital.data.BedRepository;
import com.hospital.exception.*;
import com.hospital.model.Bed;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class BedService {
    private final BedRepository bedRepository;
    
    public BedService() {
        this.bedRepository = new BedRepository();
    }
    
    public Bed addBed(String bedId, String wardType, double dailyCharge)
            throws InvalidInputException {
        
        if (!List.of("ICU", "GENERAL", "EMERGENCY").contains(wardType)) {
            throw new InvalidInputException("Invalid ward type");
        }
        
        Bed bed = new Bed(bedId, wardType, dailyCharge);
        bedRepository.save(bed);
        return bed;
    }
    
    public Bed findBedById(String bedId) throws EntityNotFoundException {
        Bed bed = bedRepository.findById(bedId);
        if (bed == null) {
            throw new EntityNotFoundException("Bed not found");
        }
        return bed;
    }
    
    public List<Bed> getAllBeds() {
        return bedRepository.findAll();
    }
    
    public List<Bed> getAvailableBeds() {
        return bedRepository.findAvailableBeds();
    }
    
    public List<Bed> getAvailableBedsByWardType(String wardType) {
        return bedRepository.findAvailableBedsByWardType(wardType);
    }
    
    public Map<String, Long> getBedAvailabilityStats() {
        List<Bed> allBeds = bedRepository.findAll();
        return allBeds.stream()
                .filter(bed -> bed.getStatus().equals("AVAILABLE"))
                .collect(Collectors.groupingBy(Bed::getWardType, Collectors.counting()));
    }
    
    public void allocateBed(String bedId, String patientId) 
            throws EntityNotFoundException, InvalidInputException {
        
        Bed bed = findBedById(bedId);
        
        if (!bed.getStatus().equals("AVAILABLE")) {
            throw new InvalidInputException("Bed is not available");
        }
        
        bed.setStatus("OCCUPIED");
        bed.setAssignedPatientId(patientId);
        bedRepository.update(bed);
    }
    
    public void releaseBed(String bedId) throws EntityNotFoundException {
        Bed bed = findBedById(bedId);
        bed.setStatus("AVAILABLE");
        bed.setAssignedPatientId(null);
        bedRepository.update(bed);
    }
    
    public void deleteBed(String bedId) throws EntityNotFoundException {
        if (!bedRepository.exists(bedId)) {
            throw new EntityNotFoundException("Bed not found");
        }
        bedRepository.delete(bedId);
    }
}
