package com.gf.career.space.controller;

import com.gf.career.space.entity.CareerPlan;
import com.gf.career.space.service.CareerPlanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/career-plans")
public class CareerPlanController {

    @Autowired
    private CareerPlanService careerPlanService;

    @PostMapping
    public boolean createCareerPlan(@RequestBody CareerPlan careerPlan) {
        return careerPlanService.save(careerPlan);
    }

    @GetMapping("/{id}")
    public CareerPlan getCareerPlanById(@PathVariable Long id) {
        return careerPlanService.getById(id);
    }

    @GetMapping
    public List<CareerPlan> getAllCareerPlans() {
        return careerPlanService.list();
    }

    @GetMapping("/employee/{employeeId}")
    public List<CareerPlan> getCareerPlansByEmployeeId(@PathVariable Long employeeId) {
        return careerPlanService.lambdaQuery().eq(CareerPlan::getEmployeeId, employeeId).list();
    }

    @PutMapping("/{id}")
    public boolean updateCareerPlan(@PathVariable Long id, @RequestBody CareerPlan careerPlan) {
        careerPlan.setId(id);
        return careerPlanService.updateById(careerPlan);
    }

    @DeleteMapping("/{id}")
    public boolean deleteCareerPlan(@PathVariable Long id) {
        return careerPlanService.removeById(id);
    }
}