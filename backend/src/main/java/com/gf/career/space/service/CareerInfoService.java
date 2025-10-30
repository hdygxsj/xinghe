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
     * @param grade 绩效等级 (A, B, C, D)
     * @return 证明信息
     */
    Certificate generateAnnualAssessmentCertificate(Long employeeId, int year, String grade);

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
    
    /**
     * 获取指定员工的统计信息
     * @param employeeId 员工ID
     * @return 统计信息Map
     */
    Map<String, Object> getStatisticsByEmployeeId(Long employeeId);

    List<Map<String, Object>> getRecentActivities(int page, int size);

    /**
     * 获取最近活动（支持分页）
     * @param page 页码（从0开始）
     * @param size 每页大小
     * @return 包含活动列表和总数量的Map
     */
    Map<String, Object> getRecentActivitiesWithPagination(int page, int size);
}