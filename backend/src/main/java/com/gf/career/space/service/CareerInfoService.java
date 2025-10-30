package com.gf.career.space.service;

import com.gf.career.space.entity.Certificate;
import com.gf.career.space.entity.Milestone;

import java.util.List;
import java.util.Map;

public interface CareerInfoService {
    /**
     * 获取员工的所有里程碑事件
     * @param employeeId 员工ID
     * @return 里程碑事件列表
     */
    List<Milestone> getMilestonesByEmployeeId(Long employeeId);

    /**
     * 获取员工的所有证书和证明
     * @param employeeId 员工ID
     * @return 证书和证明列表
     */
    List<Certificate> getCertificatesByEmployeeId(Long employeeId);

    /**
     * 生成年度考核证明
     * @param employeeId 员工ID
     * @param year 年份
     * @return 证明信息
     */
    Certificate generateAnnualAssessmentCertificate(Long employeeId, int year);

    /**
     * 生成在职联系人证明
     * @param employeeId 员工ID
     * @return 证明信息
     */
    Certificate generateEmploymentContactCertificate(Long employeeId);

    /**
     * 生成荣誉证明
     * @param employeeId 员工ID
     * @param honorId 荣誉ID
     * @return 证明信息
     */
    Certificate generateHonorCertificate(Long employeeId, Long honorId);
    
    /**
     * 获取系统统计信息
     * @return 统计信息Map
     */
    Map<String, Object> getStatistics();
}