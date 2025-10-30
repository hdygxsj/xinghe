package com.gf.career.space.service;

import com.gf.career.space.entity.Employee;

public interface UserService {
    Employee register(Employee employee) throws Exception;
    Employee login(String username, String password) throws Exception;
}