package com.gf.career.space.controller;

import com.gf.career.space.entity.Employee;
import com.gf.career.space.service.EmployeeService;
import com.gf.career.space.util.UserContextHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/employees")
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;

    @PostMapping
    public boolean createEmployee(@RequestBody Employee employee) {
        return employeeService.save(employee);
    }

    @GetMapping("/{id}")
    public Employee getEmployeeById(@PathVariable Long id) {
        return employeeService.getById(id);
    }

    @GetMapping
    public List<Employee> getAllEmployees() {
        return employeeService.list();
    }

    @PutMapping("/{id}")
    public boolean updateEmployee(@PathVariable Long id, @RequestBody Employee employee) {
        employee.setId(id);
        return employeeService.updateById(employee);
    }

    @DeleteMapping("/{id}")
    public boolean deleteEmployee(@PathVariable Long id) {
        return employeeService.removeById(id);
    }

    @PatchMapping("/{id}/role")
    public ResponseEntity<Map<String, Object>> updateEmployeeRole(
            @PathVariable Long id,
            @RequestBody Map<String, String> request) {
        Map<String, Object> response = new HashMap<>();
        
        // Check if current user is admin
        Employee currentUser = UserContextHolder.getCurrentUser();
        if (currentUser == null || !"ADMIN".equals(currentUser.getRole())) {
            response.put("success", false);
            response.put("message", "Only administrators can update employee roles");
            return ResponseEntity.status(403).body(response);
        }
        
        try {
            String newRole = request.get("role");
            if (newRole == null || newRole.isEmpty()) {
                response.put("success", false);
                response.put("message", "Role cannot be empty");
                return ResponseEntity.badRequest().body(response);
            }
            
            Employee employee = employeeService.getById(id);
            if (employee == null) {
                response.put("success", false);
                response.put("message", "Employee not found");
                return ResponseEntity.notFound().build();
            }
            
            employee.setRole(newRole);
            boolean updated = employeeService.updateById(employee);
            
            if (updated) {
                response.put("success", true);
                response.put("message", "Role updated successfully");
                response.put("data", employee);
                return ResponseEntity.ok(response);
            } else {
                response.put("success", false);
                response.put("message", "Failed to update role");
                return ResponseEntity.internalServerError().body(response);
            }
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error: " + e.getMessage());
            return ResponseEntity.internalServerError().body(response);
        }
    }
}