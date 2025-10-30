package com.gf.career.space.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gf.career.space.entity.Employee;
import com.gf.career.space.mapper.EmployeeMapper;
import com.gf.career.space.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl extends ServiceImpl<EmployeeMapper, Employee> implements UserService {

    @Autowired
    private EmployeeMapper employeeMapper;

    private PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

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
        
        if (employee == null) {
            throw new Exception("User not found");
        }
        
        // Check password
        if (!passwordEncoder.matches(password, employee.getPassword())) {
            throw new Exception("Invalid password");
        }
        
        // Return employee without password
        employee.setPassword(null);
        return employee;
    }
}