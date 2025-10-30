package com.gf.career.space.controller;

import com.gf.career.space.entity.Employee;
import com.gf.career.space.service.UserService;
import com.gf.career.space.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class UserController {

    @Autowired
    private UserService userService;
    
    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("/register")
    public ResponseEntity<Map<String, Object>> register(@RequestBody Employee employee) {
        Map<String, Object> response = new HashMap<>();
        
        try {
            Employee registeredEmployee = userService.register(employee);
            response.put("success", true);
            response.put("message", "Registration successful");
            response.put("data", registeredEmployee);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> login(@RequestBody Map<String, String> credentials) {
        Map<String, Object> response = new HashMap<>();
        
        try {
            String username = credentials.get("username");
            String password = credentials.get("password");
            
            Employee employee = userService.login(username, password);
            
            // Generate JWT token
            String token = jwtUtil.generateToken(employee.getId(), username);
            
            response.put("success", true);
            response.put("message", "Login successful");
            response.put("data", employee);
            response.put("token", token); // Add token to response
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }
}