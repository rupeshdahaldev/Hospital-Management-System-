package com.hospital.service;

import com.hospital.data.UserRepository;
import com.hospital.exception.InvalidCredentialsException;
import com.hospital.model.User;

public class AuthService {
    private final UserRepository userRepository;
    private User currentUser;
    
    public AuthService() {
        this.userRepository = new UserRepository();
    }
    
    public User login(String username, String password) throws InvalidCredentialsException {
        User user = userRepository.findByUsername(username);
        
        if (user == null) {
            throw new InvalidCredentialsException("Username not found");
        }
        
        if (!user.getPassword().equals(password)) {
            throw new InvalidCredentialsException("Incorrect password");
        }
        
        this.currentUser = user;
        return user;
    }
    
    public void logout() {
        this.currentUser = null;
    }
    
    public User getCurrentUser() {
        return currentUser;
    }
}
