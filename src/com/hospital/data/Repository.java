package com.hospital.data;

import java.util.List;

public interface Repository<T> {
    void save(T entity);
    void update(T entity);
    void delete(String id);
    T findById(String id);
    List<T> findAll();
    boolean exists(String id);
}
