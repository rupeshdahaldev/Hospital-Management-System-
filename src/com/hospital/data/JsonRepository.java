package com.hospital.data;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.*;
import java.lang.reflect.Type;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public abstract class JsonRepository<T> implements Repository<T> {
    protected final String filePath;
    protected final Type listType;
    protected final Gson gson;
    private final ReentrantReadWriteLock rwLock = new ReentrantReadWriteLock();
    private final Lock readLock = rwLock.readLock();
    private final Lock writeLock = rwLock.writeLock();

    protected JsonRepository(String filePath, Type listType) {
        this.filePath = filePath;
        this.listType = listType;
        this.gson = buildGson();
        ensureFileExists();
    }


    protected Gson buildGson() {
        return new GsonBuilder()
                .setPrettyPrinting()
                .registerTypeAdapter(LocalDate.class, new LocalDateAdapter())
                .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
                .create();
    }

    private void ensureFileExists() {
        File file = new File(filePath);
        if (!file.exists()) {
            try {
                file.getParentFile().mkdirs();
                file.createNewFile();
                writeToFile(new ArrayList<>());
            } catch (IOException e) {
                System.err.println("Error creating file: " + filePath + " — " + e.getMessage());
            }
        }
    }

    @Override
    public List<T> findAll() {
        readLock.lock();
        try {
            return readFromFile();
        } finally {
            readLock.unlock();
        }
    }

    @Override
    public T findById(String id) {
        readLock.lock();
        try {
            List<T> entities = readFromFile();
            return entities.stream()
                    .filter(entity -> getId(entity).equals(id))
                    .findFirst()
                    .orElse(null);
        } finally {
            readLock.unlock();
        }
    }

    @Override
    public void save(T entity) {
        writeLock.lock();
        try {
            List<T> entities = readFromFile();
            entities.add(entity);
            writeToFile(entities);
        } finally {
            writeLock.unlock();
        }
    }

    @Override
    public void update(T entity) {
        writeLock.lock();
        try {
            List<T> entities = readFromFile();
            String id = getId(entity);
            entities.removeIf(e -> getId(e).equals(id));
            entities.add(entity);
            writeToFile(entities);
        } finally {
            writeLock.unlock();
        }
    }

    @Override
    public void delete(String id) {
        writeLock.lock();
        try {
            List<T> entities = readFromFile();
            entities.removeIf(entity -> getId(entity).equals(id));
            writeToFile(entities);
        } finally {
            writeLock.unlock();
        }
    }

    @Override
    public boolean exists(String id) {
        return findById(id) != null;
    }

    protected List<T> readFromFile() {
        try (Reader reader = new FileReader(filePath)) {
            List<T> entities = gson.fromJson(reader, listType);
            return entities != null ? entities : new ArrayList<>();
        } catch (IOException e) {
            System.err.println("IO error reading " + filePath + ": " + e.getMessage());
            return new ArrayList<>();
        } catch (RuntimeException e) {
            System.err.println("JSON parse error reading " + filePath + ": " + e.getMessage());
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    protected void writeToFile(List<T> entities) {
        try (Writer writer = new FileWriter(filePath)) {
            gson.toJson(entities, writer);
        } catch (IOException e) {
            System.err.println("IO error writing " + filePath + ": " + e.getMessage());
        }
    }

    protected abstract String getId(T entity);
}
