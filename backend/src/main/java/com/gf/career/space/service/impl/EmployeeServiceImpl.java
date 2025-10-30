package com.gf.career.space.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gf.career.space.entity.Employee;
import com.gf.career.space.mapper.EmployeeMapper;
import com.gf.career.space.service.EmployeeService;
import org.springframework.stereotype.Service;

@Service
public class EmployeeServiceImpl extends ServiceImpl<EmployeeMapper, Employee> implements EmployeeService {
}