package com.gf.career.space.service.impl;

import com.gf.career.space.entity.Certificate;
import com.gf.career.space.entity.Milestone;
import com.gf.career.space.entity.Employee;
import com.gf.career.space.entity.EmployeeSkill;
import com.gf.career.space.service.CareerInfoService;
import com.gf.career.space.service.CertificateService;
import com.gf.career.space.service.MilestoneService;
import com.gf.career.space.service.EmployeeService;
import com.gf.career.space.service.EmployeeSkillService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class CareerInfoServiceImpl implements CareerInfoService {

    @Autowired
    private MilestoneService milestoneService;

    @Autowired
    private CertificateService certificateService;
    
    @Autowired
    private EmployeeService employeeService;
    
    @Autowired
    private EmployeeSkillService employeeSkillService;

    @Override
    public List<Milestone> getMilestonesByEmployeeId(Long employeeId) {
        return milestoneService.lambdaQuery().eq(Milestone::getEmployeeId, employeeId).list();
    }

    @Override
    public List<Certificate> getCertificatesByEmployeeId(Long employeeId) {
        return certificateService.lambdaQuery().eq(Certificate::getEmployeeId, employeeId).list();
    }

    @Override
    public Certificate generateAnnualAssessmentCertificate(Long employeeId, int year) {
        Certificate certificate = new Certificate();
        certificate.setEmployeeId(employeeId);
        certificate.setCertificateType("年度考核证明");
        certificate.setTitle(year + "年度考核证明");
        certificate.setDescription("兹证明该员工在" + year + "年度表现优秀，考核结果为合格。");
        certificate.setIssueDate(LocalDateTime.now());
        certificate.setIssuer("人力资源部");
        certificate.setCreateTime(LocalDateTime.now());
        certificate.setUpdateTime(LocalDateTime.now());
        certificateService.save(certificate);
        return certificate;
    }

    @Override
    public Certificate generateEmploymentContactCertificate(Long employeeId) {
        Certificate certificate = new Certificate();
        certificate.setEmployeeId(employeeId);
        certificate.setCertificateType("在职联系人证明");
        certificate.setTitle("在职联系人证明");
        certificate.setDescription("兹证明该员工为我公司在职员工，联系方式有效。");
        certificate.setIssueDate(LocalDateTime.now());
        certificate.setIssuer("人力资源部");
        certificate.setCreateTime(LocalDateTime.now());
        certificate.setUpdateTime(LocalDateTime.now());
        certificateService.save(certificate);
        return certificate;
    }

    @Override
    public Certificate generateHonorCertificate(Long employeeId, Long honorId) {
        // 查找荣誉里程碑
        Milestone honorMilestone = milestoneService.getById(honorId);
        
        Certificate certificate = new Certificate();
        certificate.setEmployeeId(employeeId);
        certificate.setCertificateType("荣誉证明");
        certificate.setTitle(honorMilestone.getTitle() + "荣誉证明");
        certificate.setDescription("兹证明该员工获得" + honorMilestone.getTitle() + "荣誉。");
        certificate.setIssueDate(LocalDateTime.now());
        certificate.setIssuer("人力资源部");
        certificate.setCreateTime(LocalDateTime.now());
        certificate.setUpdateTime(LocalDateTime.now());
        certificateService.save(certificate);
        return certificate;
    }
    
    @Override
    public Map<String, Object> getStatistics() {
        Map<String, Object> stats = new HashMap<>();
        
        // 获取总用户数（这里简化处理，实际应该有用户表）
        // 使用正确的查询方法获取不同员工数
        Long userCount = (long) milestoneService.lambdaQuery()
            .select(Milestone::getEmployeeId)
            .groupBy(Milestone::getEmployeeId)
            .list()
            .size();
        stats.put("userCount", userCount);
        
        // 获取总里程碑数
        Long milestoneCount = milestoneService.count();
        stats.put("milestoneCount", milestoneCount);
        
        // 获取总证书数
        Long certificateCount = certificateService.count();
        stats.put("certificateCount", certificateCount);
        
        // 获取总技能数
        Long skillCount = employeeSkillService.count();
        stats.put("skillCount", skillCount);
        
        // 用户满意度（模拟数据）
        stats.put("satisfactionRate", 95);
        
        return stats;
    }
    
    @Override
    public Map<String, Object> getStatisticsByEmployeeId(Long employeeId) {
        Map<String, Object> stats = new HashMap<>();
        
        // 获取指定员工的里程碑数
        Long milestoneCount = milestoneService.lambdaQuery()
            .eq(Milestone::getEmployeeId, employeeId)
            .count();
        stats.put("milestoneCount", milestoneCount);
        
        // 获取指定员工的证书数
        Long certificateCount = certificateService.lambdaQuery()
            .eq(Certificate::getEmployeeId, employeeId)
            .count();
        stats.put("certificateCount", certificateCount);
        
        // 获取指定员工的技能数
        Long skillCount = employeeSkillService.lambdaQuery()
            .eq(EmployeeSkill::getEmployeeId, employeeId)
            .count();
        stats.put("skillCount", skillCount);
        
        // 用户满意度（模拟数据）
        stats.put("satisfactionRate", 95);
        
        return stats;
    }
    
    @Override
    public List<Map<String, Object>> getRecentActivities(int page, int size) {
        // 限制每页大小不超过50
        size = Math.min(size, 50);
        
        // 获取最近的里程碑活动（最多page*size+size条）
        List<Milestone> recentMilestones = milestoneService.lambdaQuery()
            .orderByDesc(Milestone::getCreateTime)
            .last("LIMIT " + (page * size + size))
            .list();
            
        // 获取最近的证书活动（最多page*size+size条）
        List<Certificate> recentCertificates = certificateService.lambdaQuery()
            .orderByDesc(Certificate::getCreateTime)
            .last("LIMIT " + (page * size + size))
            .list();
            
        // 合并并排序所有活动
        List<Map<String, Object>> allActivities = new ArrayList<>();
        
        // 处理里程碑活动
        for (Milestone milestone : recentMilestones) {
            Map<String, Object> activity = new HashMap<>();
            activity.put("id", milestone.getId());
            activity.put("title", milestone.getTitle());
            activity.put("description", milestone.getDescription());
            activity.put("type", "里程碑");
            activity.put("createTime", milestone.getEventDate());
            
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
            activity.put("createTime", certificate.getIssueDate());
            
            // 获取员工信息
            Employee employee = employeeService.getById(certificate.getEmployeeId());
            if (employee != null) {
                activity.put("employeeName", employee.getName());
                activity.put("employeeId", employee.getId());
            }
            
            allActivities.add(activity);
        }
        
        // 按创建时间排序
        List<Map<String, Object>> sortedActivities = allActivities.stream()
            .sorted((a, b) -> {
                LocalDateTime timeA = (LocalDateTime) a.get("createTime");
                LocalDateTime timeB = (LocalDateTime) b.get("createTime");
                return timeB.compareTo(timeA); // 降序排列
            })
            .collect(Collectors.toList());
        
        // 分页处理
        int fromIndex = page * size;
        int toIndex = Math.min(fromIndex + size, sortedActivities.size());
        
        // 确保索引有效
        if (fromIndex >= sortedActivities.size()) {
            return new ArrayList<>(); // 返回空列表
        }
        
        return sortedActivities.subList(fromIndex, toIndex);
    }
    
    @Override
    public Map<String, Object> getRecentActivitiesWithPagination(int page, int size) {
        // 限制每页大小不超过50
        size = Math.min(size, 50);
        
        // 获取所有里程碑和证书活动
        List<Milestone> allMilestones = milestoneService.lambdaQuery()
            .orderByDesc(Milestone::getCreateTime)
            .list();
            
        List<Certificate> allCertificates = certificateService.lambdaQuery()
            .orderByDesc(Certificate::getCreateTime)
            .list();
            
        // 合并并排序所有活动
        List<Map<String, Object>> allActivities = new ArrayList<>();
        
        // 处理里程碑活动
        for (Milestone milestone : allMilestones) {
            Map<String, Object> activity = new HashMap<>();
            activity.put("id", milestone.getId());
            activity.put("title", milestone.getTitle());
            activity.put("description", milestone.getDescription());
            activity.put("type", "里程碑");
            activity.put("createTime", milestone.getEventDate());
            
            // 获取员工信息
            Employee employee = employeeService.getById(milestone.getEmployeeId());
            if (employee != null) {
                activity.put("employeeName", employee.getName());
                activity.put("employeeId", employee.getId());
            }
            
            allActivities.add(activity);
        }
        
        // 处理证书活动
        for (Certificate certificate : allCertificates) {
            Map<String, Object> activity = new HashMap<>();
            activity.put("id", certificate.getId());
            activity.put("title", certificate.getTitle());
            activity.put("description", certificate.getDescription());
            activity.put("type", "证书");
            activity.put("createTime", certificate.getIssueDate()); // 转换为字符串
            
            // 获取员工信息
            Employee employee = employeeService.getById(certificate.getEmployeeId());
            if (employee != null) {
                activity.put("employeeName", employee.getName());
                activity.put("employeeId", employee.getId());
            }
            
            allActivities.add(activity);
        }
        
        // 按创建时间排序
        List<Map<String, Object>> sortedActivities = allActivities.stream()
            .sorted((a, b) -> {
                LocalDateTime timeA = (LocalDateTime) a.get("createTime");
                LocalDateTime timeB = (LocalDateTime) b.get("createTime");
                return timeB.compareTo(timeA); // 降序排列
            })
            .collect(Collectors.toList());
        
        // 获取总数量
        int total = sortedActivities.size();
        
        // 分页处理
        int fromIndex = page * size;
        int toIndex = Math.min(fromIndex + size, sortedActivities.size());
        
        // 确保索引有效
        List<Map<String, Object>> paginatedActivities = new ArrayList<>();
        if (fromIndex < sortedActivities.size()) {
            paginatedActivities = sortedActivities.subList(fromIndex, toIndex);
        }
        
        // 构造返回结果
        Map<String, Object> result = new HashMap<>();
        result.put("activities", paginatedActivities);
        result.put("total", total);
        result.put("page", page);
        result.put("size", size);
        
        return result;
    }
}