package com.gf.career.space.controller;

import com.gf.career.space.entity.Milestone;
import com.gf.career.space.service.MilestoneService;
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

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/milestones")
public class MilestoneController {

    @Autowired
    private MilestoneService milestoneService;

    @PostMapping
    public ResponseEntity<?> createMilestone(@RequestBody Milestone milestone) {
        try {
            // 从上下文中获取当前用户ID
            Long currentUserId = getCurrentUserId();
            if (currentUserId == null) {
                return ResponseEntity.badRequest().body(java.util.Map.of("success", false, "message", "用户未登录"));
            }
            
            // 设置员工ID为当前用户ID
            milestone.setEmployeeId(currentUserId);
            
            // 设置创建时间和更新时间
            milestone.setCreateTime(LocalDateTime.now());
            milestone.setUpdateTime(LocalDateTime.now());
            
            boolean result = milestoneService.save(milestone);
            if (result) {
                return ResponseEntity.ok().body(java.util.Map.of("success", true, "message", "创建成功"));
            } else {
                return ResponseEntity.badRequest().body(java.util.Map.of("success", false, "message", "创建失败"));
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body(java.util.Map.of("success", false, "message", "创建失败: " + e.getMessage()));
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getMilestoneById(@PathVariable Long id) {
        try {
            Milestone milestone = milestoneService.getById(id);
            if (milestone != null) {
                // 检查是否是当前用户的里程碑
                Long currentUserId = getCurrentUserId();
                if (currentUserId != null && !milestone.getEmployeeId().equals(currentUserId)) {
                    return ResponseEntity.badRequest().body(java.util.Map.of("success", false, "message", "无权访问该里程碑"));
                }
                
                return ResponseEntity.ok(milestone);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body(java.util.Map.of("success", false, "message", "查询失败: " + e.getMessage()));
        }
    }

    @GetMapping
    public ResponseEntity<?> getAllMilestones() {
        try {
            Long currentUserId = getCurrentUserId();
            if (currentUserId == null) {
                return ResponseEntity.badRequest().body(java.util.Map.of("success", false, "message", "用户未登录"));
            }
            
            List<Milestone> milestones = milestoneService.lambdaQuery().eq(Milestone::getEmployeeId, currentUserId).list();
            return ResponseEntity.ok(milestones);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body(java.util.Map.of("success", false, "message", "查询失败: " + e.getMessage()));
        }
    }

    @GetMapping("/employee/{employeeId}")
    public ResponseEntity<?> getMilestonesByEmployeeId(@PathVariable Long employeeId) {
        try {
            // 检查是否是当前用户或有权限访问
            Long currentUserId = getCurrentUserId();
            if (currentUserId == null) {
                return ResponseEntity.badRequest().body(java.util.Map.of("success", false, "message", "用户未登录"));
            }
            
            if (!currentUserId.equals(employeeId)) {
                return ResponseEntity.badRequest().body(java.util.Map.of("success", false, "message", "无权访问该员工的里程碑"));
            }
            
            List<Milestone> milestones = milestoneService.lambdaQuery().eq(Milestone::getEmployeeId, employeeId).list();
            return ResponseEntity.ok(milestones);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body(java.util.Map.of("success", false, "message", "查询失败: " + e.getMessage()));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateMilestone(@PathVariable Long id, @RequestBody Milestone milestone) {
        try {
            // 检查是否是当前用户的里程碑
            Long currentUserId = getCurrentUserId();
            if (currentUserId == null) {
                return ResponseEntity.badRequest().body(java.util.Map.of("success", false, "message", "用户未登录"));
            }
            
            Milestone existingMilestone = milestoneService.getById(id);
            if (existingMilestone == null) {
                return ResponseEntity.badRequest().body(java.util.Map.of("success", false, "message", "里程碑不存在"));
            }
            
            if (!existingMilestone.getEmployeeId().equals(currentUserId)) {
                return ResponseEntity.badRequest().body(java.util.Map.of("success", false, "message", "无权更新该里程碑"));
            }
            
            milestone.setId(id);
            milestone.setUpdateTime(LocalDateTime.now());
            
            boolean result = milestoneService.updateById(milestone);
            if (result) {
                return ResponseEntity.ok().body(java.util.Map.of("success", true, "message", "更新成功"));
            } else {
                return ResponseEntity.badRequest().body(java.util.Map.of("success", false, "message", "更新失败"));
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body(java.util.Map.of("success", false, "message", "更新失败: " + e.getMessage()));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteMilestone(@PathVariable Long id) {
        try {
            // 检查是否是当前用户的里程碑
            Long currentUserId = getCurrentUserId();
            if (currentUserId == null) {
                return ResponseEntity.badRequest().body(java.util.Map.of("success", false, "message", "用户未登录"));
            }
            
            Milestone existingMilestone = milestoneService.getById(id);
            if (existingMilestone == null) {
                return ResponseEntity.badRequest().body(java.util.Map.of("success", false, "message", "里程碑不存在"));
            }
            
            if (!existingMilestone.getEmployeeId().equals(currentUserId)) {
                return ResponseEntity.badRequest().body(java.util.Map.of("success", false, "message", "无权删除该里程碑"));
            }
            
            boolean result = milestoneService.removeById(id);
            if (result) {
                return ResponseEntity.ok().body(java.util.Map.of("success", true, "message", "删除成功"));
            } else {
                return ResponseEntity.badRequest().body(java.util.Map.of("success", false, "message", "删除失败"));
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body(java.util.Map.of("success", false, "message", "删除失败: " + e.getMessage()));
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