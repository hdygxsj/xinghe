package com.gf.career.space.controller;

import com.gf.career.space.entity.EmployeeSkill;
import com.gf.career.space.service.EmployeeSkillService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/employee-skills")
public class EmployeeSkillController {

    @Autowired
    private EmployeeSkillService employeeSkillService;

    @PostMapping
    public boolean createEmployeeSkill(@RequestBody EmployeeSkill employeeSkill) {
        return employeeSkillService.save(employeeSkill);
    }

    @GetMapping("/{id}")
    public EmployeeSkill getEmployeeSkillById(@PathVariable Long id) {
        return employeeSkillService.getById(id);
    }

    @GetMapping
    public List<EmployeeSkill> getAllEmployeeSkills() {
        return employeeSkillService.list();
    }

    @GetMapping("/employee/{employeeId}")
    public List<EmployeeSkill> getEmployeeSkillsByEmployeeId(@PathVariable Long employeeId) {
        return employeeSkillService.lambdaQuery().eq(EmployeeSkill::getEmployeeId, employeeId).list();
    }

    @PutMapping("/{id}")
    public boolean updateEmployeeSkill(@PathVariable Long id, @RequestBody EmployeeSkill employeeSkill) {
        employeeSkill.setId(id);
        return employeeSkillService.updateById(employeeSkill);
    }

    @DeleteMapping("/{id}")
    public boolean deleteEmployeeSkill(@PathVariable Long id) {
        return employeeSkillService.removeById(id);
    }
}