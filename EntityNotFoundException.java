package com.hospital.data;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.hospital.model.User;

import java.lang.reflect.Type;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;


public class UserRepository extends JsonRepository<User> {
    private static final String FILE_PATH = "data/users.json";
    private static final Type LIST_TYPE = new TypeToken<List<User>>(){}.getType();

    public UserRepository() {
        super(FILE_PATH, LIST_TYPE);
    }

    @Override
    protected Gson buildGson() {
        return new GsonBuilder()
                .setPrettyPrinting()
                .registerTypeAdapter(LocalDate.class, new LocalDateAdapter())
                .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
                .registerTypeAdapter(User.class, new UserDeserializer())
                .create();
    }

    @Override
    protected String getId(User entity) {
        return entity.getUserId();
    }

    public User findByUsername(String username) {
        return findAll().stream()
                .filter(user -> user.getUsername().equals(username))
                .findFirst()
                .orElse(null);
    }

    public List<User> findByRole(String role) {
        return findAll().stream()
                .filter(user -> user.getRole().equals(role))
                .toList();
    }
}
