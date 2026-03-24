package com.hospital.data;

import com.google.gson.*;
import com.hospital.model.Admin;
import com.hospital.model.Doctor;
import com.hospital.model.Patient;
import com.hospital.model.User;

import java.lang.reflect.Type;

/**
 * Handles deserialisation of the abstract User class.
 * Reads the "role" field and delegates to Admin, Doctor, or Patient accordingly.
 */
public class UserDeserializer implements JsonDeserializer<User> {

    @Override
    public User deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
            throws JsonParseException {

        if (!json.isJsonObject()) {
            throw new JsonParseException("Expected a JSON object for User, got: " + json.getClass().getSimpleName());
        }

        JsonObject jsonObject = json.getAsJsonObject();

        JsonElement roleElement = jsonObject.get("role");
        if (roleElement == null || roleElement.isJsonNull()) {
            throw new JsonParseException("Missing 'role' field in User JSON: " + jsonObject);
        }

        String role = roleElement.getAsString();

        switch (role) {
            case "ADMIN":
                return context.deserialize(json, Admin.class);
            case "DOCTOR":
                return context.deserialize(json, Doctor.class);
            case "PATIENT":
                return context.deserialize(json, Patient.class);
            default:
                throw new JsonParseException("Unknown user role '" + role + "' — cannot deserialize User.");
        }
    }
}
