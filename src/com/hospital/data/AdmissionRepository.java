package com.hospital.data;

import com.google.gson.reflect.TypeToken;
import com.hospital.model.Admission;
import java.lang.reflect.Type;
import java.util.List;

public class AdmissionRepository extends JsonRepository<Admission> {
    private static final String FILE_PATH = "data/admissions.json";
    private static final Type LIST_TYPE = new TypeToken<List<Admission>>(){}.getType();
    
    public AdmissionRepository() {
        super(FILE_PATH, LIST_TYPE);
    }
    
    @Override
    protected String getId(Admission entity) {
        return entity.getAdmissionId();
    }
    
    public List<Admission> findByPatientId(String patientId) {
        return findAll().stream()
                .filter(adm -> adm.getPatientId().equals(patientId))
                .toList();
    }
}
