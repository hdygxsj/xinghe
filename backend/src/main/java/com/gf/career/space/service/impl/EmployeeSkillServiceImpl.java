package com.gf.career.space.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gf.career.space.entity.EmployeeSkill;
import com.gf.career.space.entity.Skill;
import com.gf.career.space.mapper.EmployeeSkillMapper;
import com.gf.career.space.service.EmployeeSkillService;
import com.gf.career.space.service.SkillService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class EmployeeSkillServiceImpl extends ServiceImpl<EmployeeSkillMapper, EmployeeSkill> implements EmployeeSkillService {
    
    @Autowired
    private SkillService skillService;
    
    @Override
    public Map<String, Object> getEmployeeSkillStatistics(Long employeeId) {
        // 获取员工的所有技能
        List<EmployeeSkill> employeeSkills = this.lambdaQuery().eq(EmployeeSkill::getEmployeeId, employeeId).list();
        
        // 初始化各类技能计数
        Map<String, Integer> skillTypeCount = new HashMap<>();
        skillTypeCount.put("技术技能", 0);
        skillTypeCount.put("软技能", 0);
        skillTypeCount.put("专业技能", 0);
        skillTypeCount.put("语言技能", 0);
        skillTypeCount.put("管理技能", 0);
        
        // 统计各类技能
        for (EmployeeSkill employeeSkill : employeeSkills) {
            Skill skill = skillService.getById(employeeSkill.getSkillId());
            if (skill != null && skill.getCategory() != null) {
                String category = skill.getCategory();
                skillTypeCount.put(category, skillTypeCount.getOrDefault(category, 0) + 1);
            }
        }
        
        // 构造返回结果
        Map<String, Object> result = new HashMap<>();
        result.put("skillTypeCount", skillTypeCount);
        result.put("totalSkills", employeeSkills.size());
        
        return result;
    }
}