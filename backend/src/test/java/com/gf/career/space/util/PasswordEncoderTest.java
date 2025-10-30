package com.gf.career.space.util;

import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;

public class PasswordEncoderTest {

    @Test
    public void testPasswordEncodingAndMatching() {
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        
        // Test password
        String rawPassword = "password123";
        
        // Encode the password
        String encodedPassword = passwordEncoder.encode(rawPassword);
        System.out.println("Encoded password: " + encodedPassword);
        
        // Check that the encoded password is different from the raw password
        assertNotEquals(rawPassword, encodedPassword);
        
        // Check that the password matches
        assertTrue(passwordEncoder.matches(rawPassword, encodedPassword));
        
        // Check that a wrong password doesn't match
        assertFalse(passwordEncoder.matches("wrongpassword", encodedPassword));
    }
    
    @Test
    public void testKnownHash() {
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        
        // Test with the known hash from our data.sql
        String rawPassword = "password123";
        String knownHash = "$2a$10$jSgB9VADh8Uh8D0JFSMPgOtOReFiNkE9aO2L50yNIzLHyXy8h20Sm";
        
        // Check that the password matches the known hash
        assertTrue(passwordEncoder.matches(rawPassword, knownHash));
        
        // Check that a wrong password doesn't match
        assertFalse(passwordEncoder.matches("wrongpassword", knownHash));
    }
}