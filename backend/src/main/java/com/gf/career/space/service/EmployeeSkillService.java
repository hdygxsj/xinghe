package com.gf.career.space.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.gf.career.space.entity.EmployeeSkill;

import java.util.Map;

public interface EmployeeSkillService extends IService<EmployeeSkill> {
    /**
     * 获取员工技能统计信息
     * @param employeeId 员工ID
     * @return 技能统计信息
     */
    Map<String, Object> getEmployeeSkillStatistics(Long employeeId);
}