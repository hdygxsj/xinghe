package com.gf.career.space.service;

import com.gf.career.space.entity.Employee;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
public class UserServiceTest {

    @Autowired
    private UserService userService;

    @Test
    public void testRegisterAndLogin() throws Exception {
        // Create a new employee for testing
        Employee employee = new Employee();
        employee.setEmployeeId("TEST001");
        employee.setName("测试用户");
        employee.setUsername("testuser");
        employee.setPassword("testpassword");
        employee.setDepartment("测试部");
        employee.setPosition("测试工程师");
        employee.setEmail("test@example.com");
        employee.setPhone("13800138000");

        // Test registration
        Employee registeredEmployee = userService.register(employee);
        assertNotNull(registeredEmployee.getId());
        assertEquals("testuser", registeredEmployee.getUsername());
        // Password should be encrypted, so it should not be the same as the original
        assertNotEquals("testpassword", registeredEmployee.getPassword());

        // Test login with correct credentials
        Employee loggedInEmployee = userService.login("testuser", "testpassword");
        assertNotNull(loggedInEmployee);
        assertEquals("testuser", loggedInEmployee.getUsername());
        // Password should be null in the returned employee
        assertNull(loggedInEmployee.getPassword());

        // Test login with incorrect password
        assertThrows(Exception.class, () -> {
            userService.login("testuser", "wrongpassword");
        });
    }
}