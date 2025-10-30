package com.gf.career.space.service;

public interface AIService {
    /**
     * 根据员工的职业历程生成职业发展建议
     * @param employeeId 员工ID
     * @return 职业发展建议
     */
    String generateCareerAdvice(Long employeeId);

    /**
     * 分析员工技能并推荐学习路径
     * @param employeeId 员工ID
     * @return 学习路径建议
     */
    String recommendLearningPath(Long employeeId);

    /**
     * 生成简历优化建议
     * @param employeeId 员工ID
     * @return 简历优化建议
     */
    String optimizeResume(Long employeeId);
}