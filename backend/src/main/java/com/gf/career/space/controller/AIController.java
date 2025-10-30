package com.gf.career.space.controller;

import com.gf.career.space.entity.Employee;
import com.gf.career.space.entity.Milestone;
import com.gf.career.space.entity.Skill;
import com.gf.career.space.service.EmployeeService;
import com.gf.career.space.service.MilestoneService;
import com.gf.career.space.service.SkillService;
import com.gf.career.space.util.UserContextHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/ai")
public class AIController {

    @Autowired
    private EmployeeService employeeService;
    
    @Autowired
    private MilestoneService milestoneService;
    
    @Autowired
    private SkillService skillService;

    @PostMapping("/chat")
    public Map<String, Object> chatWithAI(@RequestBody Map<String, Object> requestData) {
        Map<String, Object> response = new HashMap<>();
        
        try {
            String message = (String) requestData.get("message");
            // 从上下文中获取当前用户ID
            Long currentUserId = getCurrentUserId();
            
            String reply = generateAIReply(message, currentUserId);
            
            response.put("success", true);
            response.put("reply", reply);
        } catch (Exception e) {
            response.put("success", false);
            response.put("reply", "抱歉，处理您的请求时出现了问题。");
        }
        
        return response;
    }

    @GetMapping("/career-advice")
    public Map<String, Object> getCareerAdvice() {
        Map<String, Object> response = new HashMap<>();
        
        try {
            // 从上下文中获取当前用户ID
            Long currentUserId = getCurrentUserId();
            
            String advice = generateCareerAdvice(currentUserId);
            
            response.put("success", true);
            response.put("advice", advice);
        } catch (Exception e) {
            response.put("success", false);
            response.put("advice", "抱歉，生成职业建议时出现了问题。");
        }
        
        return response;
    }

    @GetMapping("/learning-path")
    public Map<String, Object> getLearningPath() {
        Map<String, Object> response = new HashMap<>();
        
        try {
            // 从上下文中获取当前用户ID
            Long currentUserId = getCurrentUserId();
            
            String learningPath = generateLearningPath(currentUserId);
            
            response.put("success", true);
            response.put("path", learningPath);
        } catch (Exception e) {
            response.put("success", false);
            response.put("path", "抱歉，生成学习路径时出现了问题。");
        }
        
        return response;
    }

    @GetMapping("/resume-optimize")
    public Map<String, Object> optimizeResume() {
        Map<String, Object> response = new HashMap<>();
        
        try {
            // 从上下文中获取当前用户ID
            Long currentUserId = getCurrentUserId();
            
            String optimization = generateResumeOptimization(currentUserId);
            
            response.put("success", true);
            response.put("optimization", optimization);
        } catch (Exception e) {
            response.put("success", false);
            response.put("optimization", "抱歉，生成简历优化建议时出现了问题。");
        }
        
        return response;
    }
    
    private String generateAIReply(String message, Long employeeId) {
        // 这里应该集成实际的AI模型，目前使用模拟回复
        StringBuilder reply = new StringBuilder();
        
        if (employeeId != null) {
            // 获取员工信息
            Employee employee = employeeService.getById(employeeId);
            if (employee != null) {
                reply.append("尊敬的").append(employee.getName()).append("，");
            }
        }
        
        // 根据消息内容生成回复
        if (message.contains("职业") || message.contains("发展")) {
            reply.append("根据您的职业历程，我建议您继续深化专业技能，同时关注跨部门协作能力的提升。");
        } else if (message.contains("技能") || message.contains("学习")) {
            reply.append("基于您的技能背景，我推荐您学习一些新兴技术，如人工智能和大数据分析。");
        } else if (message.contains("晋升") || message.contains("职位")) {
            reply.append("根据您的工作表现，您已经具备了晋升的条件，建议您主动与上级沟通职业发展计划。");
        } else {
            reply.append("感谢您的提问。作为您的职业助手，我可以为您提供职业规划、技能提升和学习路径等方面的建议。");
        }
        
        reply.append("如果您有更具体的问题，欢迎继续咨询。");
        
        return reply.toString();
    }
    
    private String generateCareerAdvice(Long employeeId) {
        StringBuilder advice = new StringBuilder();
        advice.append("职业发展建议：\n\n");
        
        if (employeeId != null) {
            // 获取员工的里程碑和技能
            List<Milestone> milestones = milestoneService.lambdaQuery()
                .eq(Milestone::getEmployeeId, employeeId)
                .list();
            
            // 基于里程碑生成建议
            if (!milestones.isEmpty()) {
                advice.append("1. 您已经积累了丰富的职业经验，建议继续在现有领域深耕。\n");
                advice.append("2. 考虑向管理岗位发展，利用您的专业经验指导团队。\n");
            } else {
                advice.append("1. 建议您积极记录职业历程，这有助于明确发展方向。\n");
                advice.append("2. 主动参与重要项目，积累实战经验。\n");
            }
        } else {
            advice.append("1. 建议制定明确的职业目标和计划。\n");
            advice.append("2. 持续学习新技能，保持竞争力。\n");
            advice.append("3. 建立良好的人际关系网络。\n");
        }
        
        advice.append("4. 定期评估和调整职业规划。\n");
        advice.append("5. 保持工作与生活的平衡。");
        
        return advice.toString();
    }
    
    private String generateLearningPath(Long employeeId) {
        StringBuilder path = new StringBuilder();
        path.append("个性化学习路径：\n\n");
        
        if (employeeId != null) {
            // 获取员工技能
            // 这里简化处理，实际应该查询员工技能表
            path.append("根据您的技能背景，推荐以下学习路径：\n");
            path.append("1. 技术深化：深入学习您所在领域的核心技术\n");
            path.append("2. 软技能提升：加强沟通、领导力等软技能\n");
            path.append("3. 行业前沿：关注行业最新趋势和技术发展\n");
        } else {
            path.append("通用学习路径：\n");
            path.append("1. 基础技能巩固\n");
            path.append("2. 专业技能提升\n");
            path.append("3. 跨领域知识拓展\n");
            path.append("4. 实践项目经验积累\n");
        }
        
        path.append("建议每季度评估学习成果并调整学习计划。");
        
        return path.toString();
    }
    
    private String generateResumeOptimization(Long employeeId) {
        StringBuilder optimization = new StringBuilder();
        optimization.append("简历优化建议：\n\n");
        
        if (employeeId != null) {
            Employee employee = employeeService.getById(employeeId);
            if (employee != null) {
                optimization.append("针对您的背景，建议：\n");
                optimization.append("1. 突出关键成就和量化结果\n");
                optimization.append("2. 使用行业关键词优化技能描述\n");
                optimization.append("3. 简化冗余信息，保持简洁明了\n");
                optimization.append("4. 强调领导力和团队协作经验\n");
            }
        } else {
            optimization.append("通用简历优化建议：\n");
            optimization.append("1. 突出核心技能和专业成就\n");
            optimization.append("2. 使用量化数据展示工作成果\n");
            optimization.append("3. 保持格式统一和易于阅读\n");
            optimization.append("4. 针对不同职位定制简历内容\n");
        }
        
        optimization.append("5. 定期更新简历内容");
        
        return optimization.toString();
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