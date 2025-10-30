package com.gf.career.space.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gf.career.space.entity.Employee;
import com.gf.career.space.mapper.EmployeeMapper;
import com.gf.career.space.service.UserService;
import com.gf.career.space.util.SM3PasswordEncoder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class UserServiceImpl extends ServiceImpl<EmployeeMapper, Employee> implements UserService {

    private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    @Autowired
    private EmployeeMapper employeeMapper;

    private SM3PasswordEncoder passwordEncoder = new SM3PasswordEncoder();

    @Override
    public Employee register(Employee employee) throws Exception {
        // Check if username already exists
        QueryWrapper<Employee> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username", employee.getUsername());
        if (employeeMapper.selectCount(queryWrapper) > 0) {
            throw new Exception("Username already exists");
        }

        // Encode password
        employee.setPassword(passwordEncoder.encode(employee.getPassword()));
        
        // Save employee
        employeeMapper.insert(employee);
        return employee;
    }

    @Override
    public Employee login(String username, String password) throws Exception {
        // Find employee by username
        QueryWrapper<Employee> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username", username);
        Employee employee = employeeMapper.selectOne(queryWrapper);
        
        logger.debug("Login attempt - Username: {}", username);
        logger.debug("Employee found: {}", (employee != null));
        
        if (employee == null) {
            throw new Exception("User not found");
        }
        
        logger.debug("Stored password hash: {}", employee.getPassword());
        logger.debug("Provided password: {}", password);
        
        // Check password
        boolean passwordMatches = passwordEncoder.matches(password, employee.getPassword());
        logger.debug("Password matches: {}", passwordMatches);
        
        if (!passwordMatches) {
            throw new Exception("Invalid password");
        }
        
        // Create a copy of employee without password for return
        Employee employeeWithoutPassword = new Employee();
        employeeWithoutPassword.setId(employee.getId());
        employeeWithoutPassword.setUsername(employee.getUsername());
        employeeWithoutPassword.setName(employee.getName());
        employeeWithoutPassword.setDepartment(employee.getDepartment());
        employeeWithoutPassword.setPosition(employee.getPosition());
        employeeWithoutPassword.setEmail(employee.getEmail());
        employeeWithoutPassword.setPhone(employee.getPhone());
        employeeWithoutPassword.setHireDate(employee.getHireDate());
        employeeWithoutPassword.setRole(employee.getRole()); // Include role field
        
        return employeeWithoutPassword;
    }
}