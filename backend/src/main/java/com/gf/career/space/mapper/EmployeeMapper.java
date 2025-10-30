package com.gf.career.space.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.gf.career.space.entity.Employee;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface EmployeeMapper extends BaseMapper<Employee> {
}