package com.gf.career.space.controller;

import com.gf.career.space.entity.Certificate;
import com.gf.career.space.entity.Milestone;
import com.gf.career.space.entity.Employee;
import com.gf.career.space.service.CareerInfoService;
import com.gf.career.space.service.EmployeeService;
import com.gf.career.space.util.UserContextHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/career-info")
public class CareerInfoController {

    @Autowired
    private CareerInfoService careerInfoService;
    
    @Autowired
    private EmployeeService employeeService;

    @GetMapping("/milestones/{employeeId}")
    public List<Milestone> getMilestonesByEmployeeId(@PathVariable Long employeeId) {
        // 检查是否是当前用户或有权限访问
        Long currentUserId = getCurrentUserId();
        if (currentUserId == null || !currentUserId.equals(employeeId)) {
            throw new RuntimeException("无权访问该员工的里程碑");
        }
        
        return careerInfoService.getMilestonesByEmployeeId(employeeId);
    }

    @GetMapping("/certificates/{employeeId}")
    public List<Certificate> getCertificatesByEmployeeId(@PathVariable Long employeeId) {
        // 检查是否是当前用户或有权限访问
        Long currentUserId = getCurrentUserId();
        if (currentUserId == null || !currentUserId.equals(employeeId)) {
            throw new RuntimeException("无权访问该员工的证书");
        }
        
        return careerInfoService.getCertificatesByEmployeeId(employeeId);
    }

    @PostMapping("/certificate/annual-assessment")
    public Certificate generateAnnualAssessmentCertificate(@RequestParam Long employeeId, @RequestParam int year) {
        // 检查是否是当前用户或有权限访问
        Long currentUserId = getCurrentUserId();
        if (currentUserId == null || !currentUserId.equals(employeeId)) {
            throw new RuntimeException("无权生成该员工的证书");
        }
        
        return careerInfoService.generateAnnualAssessmentCertificate(employeeId, year);
    }

    @PostMapping("/certificate/employment-contact")
    public Certificate generateEmploymentContactCertificate(@RequestParam Long employeeId) {
        // 检查是否是当前用户或有权限访问
        Long currentUserId = getCurrentUserId();
        if (currentUserId == null || !currentUserId.equals(employeeId)) {
            throw new RuntimeException("无权生成该员工的证书");
        }
        
        return careerInfoService.generateEmploymentContactCertificate(employeeId);
    }

    @PostMapping("/certificate/honor")
    public Certificate generateHonorCertificate(@RequestParam Long employeeId, @RequestParam Long honorId) {
        // 检查是否是当前用户或有权限访问
        Long currentUserId = getCurrentUserId();
        if (currentUserId == null || !currentUserId.equals(employeeId)) {
            throw new RuntimeException("无权生成该员工的证书");
        }
        
        return careerInfoService.generateHonorCertificate(employeeId, honorId);
    }
    
    @GetMapping("/statistics")
    public Map<String, Object> getStatistics() {
        return careerInfoService.getStatistics();
    }
    
    @GetMapping("/statistics/{employeeId}")
    public Map<String, Object> getStatisticsByEmployeeId(@PathVariable Long employeeId) {
        // 检查是否是当前用户或有权限访问
        Long currentUserId = getCurrentUserId();
        if (currentUserId == null || !currentUserId.equals(employeeId)) {
            throw new RuntimeException("无权访问该员工的统计数据");
        }
        
        return careerInfoService.getStatisticsByEmployeeId(employeeId);
    }
    
    @GetMapping("/recent-activities")
    public Map<String, Object> getRecentActivities(
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "10") int size) {
        // 获取最近的里程碑和证书活动
        return careerInfoService.getRecentActivitiesWithPagination(page, size);
    }
    
    /**
     * 获取当前用户ID
     * @return 当前用户ID，如果未登录则返回null
     */
    private Long getCurrentUserId() {
        com.gf.career.space.entity.Employee currentUser = UserContextHolder.getCurrentUser();
        return currentUser != null ? currentUser.getId() : null;
    }
}