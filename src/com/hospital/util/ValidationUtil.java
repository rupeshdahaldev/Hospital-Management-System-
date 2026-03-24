package com.hospital.util;

import com.hospital.exception.InvalidInputException;
import java.util.regex.Pattern;

public class ValidationUtil {
    
    private static final Pattern EMAIL_PATTERN = 
        Pattern.compile("^[A-Za-z0-9+_.-]+@(.+)$");
    private static final Pattern PHONE_PATTERN = 
        Pattern.compile("^[0-9]{10}$");
    
    public static void validateNotEmpty(String value, String fieldName) 
            throws InvalidInputException {
        if (value == null || value.trim().isEmpty()) {
            throw new InvalidInputException(fieldName + " cannot be empty");
        }
    }
    
    public static void validateEmail(String email) throws InvalidInputException {
        validateNotEmpty(email, "Email");
        if (!EMAIL_PATTERN.matcher(email).matches()) {
            throw new InvalidInputException("Invalid email format");
        }
    }
    
    public static void validatePhone(String phone) throws InvalidInputException {
        validateNotEmpty(phone, "Phone");
        if (!PHONE_PATTERN.matcher(phone).matches()) {
            throw new InvalidInputException("Phone must be 10 digits");
        }
    }
    
    public static void validateAge(int age) throws InvalidInputException {
        if (age < 0 || age > 150) {
            throw new InvalidInputException("Invalid age: must be between 0 and 150");
        }
    }
    
    public static void validatePassword(String password) throws InvalidInputException {
        validateNotEmpty(password, "Password");
        if (password.length() < 6) {
            throw new InvalidInputException("Password must be at least 6 characters");
        }
    }
}
