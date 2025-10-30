package com.gf.career.space.controller;

import com.gf.career.space.entity.Certificate;
import com.gf.career.space.entity.Milestone;
import com.gf.career.space.entity.Employee;
import com.gf.career.space.service.CertificateService;
import com.gf.career.space.service.MilestoneService;
import com.gf.career.space.service.EmployeeService;
import com.gf.career.space.service.EmployeeSkillService;
import com.gf.career.space.util.UserContextHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/hr-dashboard")
public class HRDashboardController {

    @Autowired
    private EmployeeService employeeService;
    
    @Autowired
    private MilestoneService milestoneService;
    
    @Autowired
    private CertificateService certificateService;
    
    @Autowired
    private EmployeeSkillService employeeSkillService;

    /**
     * 获取HR驾驶舱统计数据
     * @return 统计数据Map
     */
    @GetMapping("/statistics")
    public Map<String, Object> getHRStatistics() {
        // 检查是否为管理员
        if (!isAdmin()) {
            throw new RuntimeException("无权访问HR驾驶舱数据");
        }
        
        Map<String, Object> stats = new HashMap<>();
        
        // 获取总员工数
        Long totalEmployees = employeeService.count();
        stats.put("totalEmployees", totalEmployees);
        
        // 获取总里程碑数
        Long totalMilestones = milestoneService.count();
        stats.put("totalMilestones", totalMilestones);
        
        // 获取总证书数
        Long totalCertificates = certificateService.count();
        stats.put("totalCertificates", totalCertificates);
        
        // 获取总技能数
        Long totalSkills = employeeSkillService.count();
        stats.put("totalSkills", totalSkills);
        
        return stats;
    }
    
    /**
     * 获取部门员工分布数据
     * @return 部门分布数据
     */
    @GetMapping("/department-distribution")
    public List<Map<String, Object>> getDepartmentDistribution() {
        // 检查是否为管理员
        if (!isAdmin()) {
            throw new RuntimeException("无权访问HR驾驶舱数据");
        }
        
        // 获取所有员工
        List<Employee> employees = employeeService.list();
        
        // 按部门统计
        Map<String, Integer> departmentCount = new HashMap<>();
        for (Employee employee : employees) {
            String department = employee.getDepartment() != null ? employee.getDepartment() : "未分配";
            departmentCount.put(department, departmentCount.getOrDefault(department, 0) + 1);
        }
        
        // 转换为列表格式
        List<Map<String, Object>> result = new ArrayList<>();
        for (Map.Entry<String, Integer> entry : departmentCount.entrySet()) {
            Map<String, Object> item = new HashMap<>();
            item.put("department", entry.getKey());
            item.put("count", entry.getValue());
            result.add(item);
        }
        
        return result;
    }
    
    /**
     * 获取企业最近活动
     * @return 最近活动列表
     */
    @GetMapping("/recent-activities")
    public List<Map<String, Object>> getRecentCompanyActivities() {
        // 检查是否为管理员
        if (!isAdmin()) {
            throw new RuntimeException("无权访问HR驾驶舱数据");
        }
        
        // 获取最近的里程碑活动（最多10条）
        List<Milestone> recentMilestones = milestoneService.lambdaQuery()
            .orderByDesc(Milestone::getCreateTime)
            .last("LIMIT 10")
            .list();
            
        // 获取最近的证书活动（最多10条）
        List<Certificate> recentCertificates = certificateService.lambdaQuery()
            .orderByDesc(Certificate::getCreateTime)
            .last("LIMIT 10")
            .list();
            
        // 合并并排序所有活动
        List<Map<String, Object>> allActivities = new ArrayList<>();
        
        // 处理里程碑活动
        for (Milestone milestone : recentMilestones) {
            Map<String, Object> activity = new HashMap<>();
            activity.put("id", milestone.getId());
            activity.put("title", milestone.getTitle());
            activity.put("description", milestone.getDescription());
            activity.put("type", "历程");
            activity.put("createTime", milestone.getCreateTime());
            
            // 获取员工信息
            Employee employee = employeeService.getById(milestone.getEmployeeId());
            if (employee != null) {
                activity.put("employeeName", employee.getName());
                activity.put("employeeId", employee.getId());
            }
            
            allActivities.add(activity);
        }
        
        // 处理证书活动
        for (Certificate certificate : recentCertificates) {
            Map<String, Object> activity = new HashMap<>();
            activity.put("id", certificate.getId());
            activity.put("title", certificate.getTitle());
            activity.put("description", certificate.getDescription());
            activity.put("type", "证书");
            activity.put("createTime", certificate.getCreateTime());
            
            // 获取员工信息
            Employee employee = employeeService.getById(certificate.getEmployeeId());
            if (employee != null) {
                activity.put("employeeName", employee.getName());
                activity.put("employeeId", employee.getId());
            }
            
            allActivities.add(activity);
        }
        
        // 按创建时间排序，取最新的10条记录
        return allActivities.stream()
            .sorted((a, b) -> {
                java.time.LocalDateTime timeA = (java.time.LocalDateTime) a.get("createTime");
                java.time.LocalDateTime timeB = (java.time.LocalDateTime) b.get("createTime");
                return timeB.compareTo(timeA); // 降序排列
            })
            .limit(10)
            .collect(java.util.stream.Collectors.toList());
    }
    
    /**
     * 获取员工技能统计
     * @return 技能统计数据
     */
    @GetMapping("/skill-statistics")
    public Map<String, Object> getSkillStatistics() {
        // 检查是否为管理员
        if (!isAdmin()) {
            throw new RuntimeException("无权访问HR驾驶舱数据");
        }
        
        Map<String, Object> result = new HashMap<>();
        
        // 这里可以实现更复杂的技能统计逻辑
        // 暂时返回模拟数据
        Map<String, Integer> skillStats = new HashMap<>();
        skillStats.put("技术技能", 35);
        skillStats.put("软技能", 42);
        skillStats.put("专业技能", 28);
        skillStats.put("语言技能", 15);
        skillStats.put("管理技能", 8);
        
        result.put("skillDistribution", skillStats);
        
        return result;
    }
    
    /**
     * 检查当前用户是否为管理员
     * @return 是否为管理员
     */
    private boolean isAdmin() {
        Employee currentUser = UserContextHolder.getCurrentUser();
        return currentUser != null && "ADMIN".equals(currentUser.getRole());
    }
}