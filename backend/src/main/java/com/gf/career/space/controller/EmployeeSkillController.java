package com.gf.career.space.controller;

import com.gf.career.space.entity.EmployeeSkill;
import com.gf.career.space.service.EmployeeSkillService;
import com.gf.career.space.util.UserContextHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

@RestController
@RequestMapping("/api/employee-skills")
public class EmployeeSkillController {

    private static final Logger logger = LoggerFactory.getLogger(EmployeeSkillController.class);

    @Autowired
    private EmployeeSkillService employeeSkillService;

    @PostMapping
    public ResponseEntity<?> createEmployeeSkill(@RequestBody EmployeeSkill employeeSkill) {
        try {
            // 从上下文中获取当前用户ID
            Long currentUserId = getCurrentUserId();
            if (currentUserId == null) {
                return ResponseEntity.badRequest().body(java.util.Map.of("success", false, "message", "用户未登录"));
            }
            
            // 设置员工ID为当前用户ID
            employeeSkill.setEmployeeId(currentUserId);
            
            boolean result = employeeSkillService.save(employeeSkill);
            if (result) {
                return ResponseEntity.ok().body(java.util.Map.of("success", true, "message", "创建成功"));
            } else {
                return ResponseEntity.badRequest().body(java.util.Map.of("success", false, "message", "创建失败"));
            }
        } catch (Exception e) {
            logger.error("创建员工技能时发生错误", e);
            return ResponseEntity.badRequest().body(java.util.Map.of("success", false, "message", "创建失败: " + e.getMessage()));
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getEmployeeSkillById(@PathVariable Long id) {
        try {
            EmployeeSkill employeeSkill = employeeSkillService.getById(id);
            if (employeeSkill != null) {
                // 检查是否是当前用户的技能
                Long currentUserId = getCurrentUserId();
                if (currentUserId != null && !employeeSkill.getEmployeeId().equals(currentUserId)) {
                    return ResponseEntity.badRequest().body(java.util.Map.of("success", false, "message", "无权访问该技能"));
                }
                
                return ResponseEntity.ok(employeeSkill);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            logger.error("查询员工技能时发生错误，ID: " + id, e);
            return ResponseEntity.badRequest().body(java.util.Map.of("success", false, "message", "查询失败: " + e.getMessage()));
        }
    }

    @GetMapping
    public ResponseEntity<?> getAllEmployeeSkills() {
        try {
            Long currentUserId = getCurrentUserId();
            if (currentUserId == null) {
                return ResponseEntity.badRequest().body(java.util.Map.of("success", false, "message", "用户未登录"));
            }
            
            List<EmployeeSkill> employeeSkills = employeeSkillService.lambdaQuery().eq(EmployeeSkill::getEmployeeId, currentUserId).list();
            return ResponseEntity.ok(employeeSkills);
        } catch (Exception e) {
            logger.error("查询所有员工技能时发生错误", e);
            return ResponseEntity.badRequest().body(java.util.Map.of("success", false, "message", "查询失败: " + e.getMessage()));
        }
    }

    @GetMapping("/employee/{employeeId}")
    public ResponseEntity<?> getEmployeeSkillsByEmployeeId(@PathVariable Long employeeId) {
        try {
            // 检查是否是当前用户或有权限访问
            Long currentUserId = getCurrentUserId();
            if (currentUserId == null) {
                return ResponseEntity.badRequest().body(java.util.Map.of("success", false, "message", "用户未登录"));
            }
            
            if (!currentUserId.equals(employeeId)) {
                return ResponseEntity.badRequest().body(java.util.Map.of("success", false, "message", "无权访问该员工的技能"));
            }
            
            List<EmployeeSkill> employeeSkills = employeeSkillService.lambdaQuery().eq(EmployeeSkill::getEmployeeId, employeeId).list();
            return ResponseEntity.ok(employeeSkills);
        } catch (Exception e) {
            logger.error("根据员工ID查询员工技能时发生错误，员工ID: " + employeeId, e);
            return ResponseEntity.badRequest().body(java.util.Map.of("success", false, "message", "查询失败: " + e.getMessage()));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateEmployeeSkill(@PathVariable Long id, @RequestBody EmployeeSkill employeeSkill) {
        try {
            // 检查是否是当前用户的技能
            Long currentUserId = getCurrentUserId();
            if (currentUserId == null) {
                return ResponseEntity.badRequest().body(java.util.Map.of("success", false, "message", "用户未登录"));
            }
            
            EmployeeSkill existingEmployeeSkill = employeeSkillService.getById(id);
            if (existingEmployeeSkill == null) {
                return ResponseEntity.badRequest().body(java.util.Map.of("success", false, "message", "技能不存在"));
            }
            
            if (!existingEmployeeSkill.getEmployeeId().equals(currentUserId)) {
                return ResponseEntity.badRequest().body(java.util.Map.of("success", false, "message", "无权更新该技能"));
            }
            
            employeeSkill.setId(id);
            boolean result = employeeSkillService.updateById(employeeSkill);
            if (result) {
                return ResponseEntity.ok().body(java.util.Map.of("success", true, "message", "更新成功"));
            } else {
                return ResponseEntity.badRequest().body(java.util.Map.of("success", false, "message", "更新失败"));
            }
        } catch (Exception e) {
            logger.error("更新员工技能时发生错误，ID: " + id, e);
            return ResponseEntity.badRequest().body(java.util.Map.of("success", false, "message", "更新失败: " + e.getMessage()));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteEmployeeSkill(@PathVariable Long id) {
        try {
            // 检查是否是当前用户的技能
            Long currentUserId = getCurrentUserId();
            if (currentUserId == null) {
                return ResponseEntity.badRequest().body(java.util.Map.of("success", false, "message", "用户未登录"));
            }
            
            EmployeeSkill existingEmployeeSkill = employeeSkillService.getById(id);
            if (existingEmployeeSkill == null) {
                return ResponseEntity.badRequest().body(java.util.Map.of("success", false, "message", "技能不存在"));
            }
            
            if (!existingEmployeeSkill.getEmployeeId().equals(currentUserId)) {
                return ResponseEntity.badRequest().body(java.util.Map.of("success", false, "message", "无权删除该技能"));
            }
            
            boolean result = employeeSkillService.removeById(id);
            if (result) {
                return ResponseEntity.ok().body(java.util.Map.of("success", true, "message", "删除成功"));
            } else {
                return ResponseEntity.badRequest().body(java.util.Map.of("success", false, "message", "删除失败"));
            }
        } catch (Exception e) {
            logger.error("删除员工技能时发生错误，ID: " + id, e);
            return ResponseEntity.badRequest().body(java.util.Map.of("success", false, "message", "删除失败: " + e.getMessage()));
        }
    }
    
    @GetMapping("/statistics/{employeeId}")
    public ResponseEntity<?> getEmployeeSkillStatistics(@PathVariable Long employeeId) {
        try {
            // 检查是否是当前用户或有权限访问
            Long currentUserId = getCurrentUserId();
            if (currentUserId == null) {
                return ResponseEntity.badRequest().body(java.util.Map.of("success", false, "message", "用户未登录"));
            }
            
            if (!currentUserId.equals(employeeId)) {
                return ResponseEntity.badRequest().body(java.util.Map.of("success", false, "message", "无权访问该员工的技能统计"));
            }
            
            // 获取员工技能统计信息
            java.util.Map<String, Object> statistics = employeeSkillService.getEmployeeSkillStatistics(employeeId);
            return ResponseEntity.ok(statistics);
        } catch (Exception e) {
            logger.error("获取员工技能统计时发生错误，员工ID: " + employeeId, e);
            return ResponseEntity.badRequest().body(java.util.Map.of("success", false, "message", "查询失败: " + e.getMessage()));
        }
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