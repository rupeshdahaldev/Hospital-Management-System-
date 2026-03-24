package com.hospital.data;

import com.google.gson.reflect.TypeToken;
import com.hospital.model.Bed;
import java.lang.reflect.Type;
import java.util.List;

public class BedRepository extends JsonRepository<Bed> {
    private static final String FILE_PATH = "data/beds.json";
    private static final Type LIST_TYPE = new TypeToken<List<Bed>>(){}.getType();
    
    public BedRepository() {
        super(FILE_PATH, LIST_TYPE);
    }
    
    @Override
    protected String getId(Bed entity) {
        return entity.getBedId();
    }
    
    public List<Bed> findAvailableBedsByWardType(String wardType) {
        return findAll().stream()
                .filter(bed -> bed.getWardType().equals(wardType) && 
                              bed.getStatus().equals("AVAILABLE"))
                .toList();
    }
    
    public List<Bed> findAvailableBeds() {
        return findAll().stream()
                .filter(bed -> bed.getStatus().equals("AVAILABLE"))
                .toList();
    }
}
