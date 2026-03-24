package com.hospital.data;

import com.google.gson.reflect.TypeToken;
import com.hospital.model.Patient;
import java.lang.reflect.Type;
import java.util.List;

public class PatientRepository extends JsonRepository<Patient> {
    private static final String FILE_PATH = "data/patients.json";
    private static final Type LIST_TYPE = new TypeToken<List<Patient>>(){}.getType();
    
    public PatientRepository() {
        super(FILE_PATH, LIST_TYPE);
    }
    
    @Override
    protected String getId(Patient entity) {
        return entity.getPatientId();
    }
    
    public List<Patient> findByDoctorId(String doctorId) {
        return findAll().stream()
                .filter(patient -> doctorId.equals(patient.getAssignedDoctorId()))
                .toList();
    }
}
