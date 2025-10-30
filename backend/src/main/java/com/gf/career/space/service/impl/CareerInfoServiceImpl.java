package com.gf.career.space.service.impl;

import com.gf.career.space.entity.Certificate;
import com.gf.career.space.entity.Milestone;
import com.gf.career.space.service.CareerInfoService;
import com.gf.career.space.service.CertificateService;
import com.gf.career.space.service.MilestoneService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class CareerInfoServiceImpl implements CareerInfoService {

    @Autowired
    private MilestoneService milestoneService;

    @Autowired
    private CertificateService certificateService;

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
        
        // 用户满意度（模拟数据）
        stats.put("satisfactionRate", 95);
        
        return stats;
    }
}