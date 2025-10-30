package com.gf.career.space.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.gf.career.space.entity.Employee;
import com.gf.career.space.mapper.EmployeeMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private EmployeeMapper employeeMapper;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // Find employee by username
        QueryWrapper<Employee> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username", username);
        Employee employee = employeeMapper.selectOne(queryWrapper);
        
        if (employee == null) {
            throw new UsernameNotFoundException("User not found with username: " + username);
        }
        
        return new User(employee.getUsername(), employee.getPassword(), new ArrayList<>());
    }
}