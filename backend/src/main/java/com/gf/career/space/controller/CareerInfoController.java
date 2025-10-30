package com.gf.career.space.controller;

import com.gf.career.space.entity.Certificate;
import com.gf.career.space.entity.Milestone;
import com.gf.career.space.service.CareerInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/career-info")
public class CareerInfoController {

    @Autowired
    private CareerInfoService careerInfoService;

    @GetMapping("/milestones/{employeeId}")
    public List<Milestone> getMilestonesByEmployeeId(@PathVariable Long employeeId) {
        return careerInfoService.getMilestonesByEmployeeId(employeeId);
    }

    @GetMapping("/certificates/{employeeId}")
    public List<Certificate> getCertificatesByEmployeeId(@PathVariable Long employeeId) {
        return careerInfoService.getCertificatesByEmployeeId(employeeId);
    }

    @PostMapping("/certificate/annual-assessment")
    public Certificate generateAnnualAssessmentCertificate(@RequestParam Long employeeId, @RequestParam int year) {
        return careerInfoService.generateAnnualAssessmentCertificate(employeeId, year);
    }

    @PostMapping("/certificate/employment-contact")
    public Certificate generateEmploymentContactCertificate(@RequestParam Long employeeId) {
        return careerInfoService.generateEmploymentContactCertificate(employeeId);
    }

    @PostMapping("/certificate/honor")
    public Certificate generateHonorCertificate(@RequestParam Long employeeId, @RequestParam Long honorId) {
        return careerInfoService.generateHonorCertificate(employeeId, honorId);
    }
}