package com.gf.career.space.controller;

import com.gf.career.space.entity.Milestone;
import com.gf.career.space.service.MilestoneService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/milestones")
public class MilestoneController {

    @Autowired
    private MilestoneService milestoneService;

    @PostMapping
    public boolean createMilestone(@RequestBody Milestone milestone) {
        return milestoneService.save(milestone);
    }

    @GetMapping("/{id}")
    public Milestone getMilestoneById(@PathVariable Long id) {
        return milestoneService.getById(id);
    }

    @GetMapping
    public List<Milestone> getAllMilestones() {
        return milestoneService.list();
    }

    @GetMapping("/employee/{employeeId}")
    public List<Milestone> getMilestonesByEmployeeId(@PathVariable Long employeeId) {
        return milestoneService.lambdaQuery().eq(Milestone::getEmployeeId, employeeId).list();
    }

    @PutMapping("/{id}")
    public boolean updateMilestone(@PathVariable Long id, @RequestBody Milestone milestone) {
        milestone.setId(id);
        return milestoneService.updateById(milestone);
    }

    @DeleteMapping("/{id}")
    public boolean deleteMilestone(@PathVariable Long id) {
        return milestoneService.removeById(id);
    }
}