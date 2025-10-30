package com.gf.career.space.controller;

import com.gf.career.space.entity.CareerPlan;
import com.gf.career.space.service.CareerPlanService;
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
@RequestMapping("/api/career-plans")
public class CareerPlanController {

    private static final Logger logger = LoggerFactory.getLogger(CareerPlanController.class);

    @Autowired
    private CareerPlanService careerPlanService;

    @PostMapping
    public ResponseEntity<?> createCareerPlan(@RequestBody CareerPlan careerPlan) {
        try {
            // 从上下文中获取当前用户ID
            Long currentUserId = getCurrentUserId();
            if (currentUserId == null) {
                return ResponseEntity.badRequest().body(java.util.Map.of("success", false, "message", "用户未登录"));
            }
            
            // 设置员工ID为当前用户ID
            careerPlan.setEmployeeId(currentUserId);
            
            boolean result = careerPlanService.save(careerPlan);
            if (result) {
                return ResponseEntity.ok().body(java.util.Map.of("success", true, "message", "创建成功"));
            } else {
                return ResponseEntity.badRequest().body(java.util.Map.of("success", false, "message", "创建失败"));
            }
        } catch (Exception e) {
            logger.error("创建职业规划时发生错误", e);
            return ResponseEntity.badRequest().body(java.util.Map.of("success", false, "message", "创建失败: " + e.getMessage()));
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getCareerPlanById(@PathVariable Long id) {
        try {
            CareerPlan careerPlan = careerPlanService.getById(id);
            if (careerPlan != null) {
                // 检查是否是当前用户的职业规划
                Long currentUserId = getCurrentUserId();
                if (currentUserId != null && !careerPlan.getEmployeeId().equals(currentUserId)) {
                    return ResponseEntity.badRequest().body(java.util.Map.of("success", false, "message", "无权访问该职业规划"));
                }
                
                return ResponseEntity.ok(careerPlan);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            logger.error("查询职业规划时发生错误，ID: {}" , id, e);
            return ResponseEntity.badRequest().body(java.util.Map.of("success", false, "message", "查询失败: " + e.getMessage()));
        }
    }

    @GetMapping
    public ResponseEntity<?> getAllCareerPlans() {
        try {
            Long currentUserId = getCurrentUserId();
            if (currentUserId == null) {
                return ResponseEntity.badRequest().body(java.util.Map.of("success", false, "message", "用户未登录"));
            }
            
            List<CareerPlan> careerPlans = careerPlanService.lambdaQuery().eq(CareerPlan::getEmployeeId, currentUserId).list();
            return ResponseEntity.ok(careerPlans);
        } catch (Exception e) {
            logger.error("查询所有职业规划时发生错误", e);
            return ResponseEntity.badRequest().body(java.util.Map.of("success", false, "message", "查询失败: " + e.getMessage()));
        }
    }

    @GetMapping("/employee/{employeeId}")
    public ResponseEntity<?> getCareerPlansByEmployeeId(@PathVariable Long employeeId) {
        try {
            // 检查是否是当前用户或有权限访问
            Long currentUserId = getCurrentUserId();
            if (currentUserId == null) {
                return ResponseEntity.badRequest().body(java.util.Map.of("success", false, "message", "用户未登录"));
            }
            
            if (!currentUserId.equals(employeeId)) {
                return ResponseEntity.badRequest().body(java.util.Map.of("success", false, "message", "无权访问该员工的职业规划"));
            }
            
            List<CareerPlan> careerPlans = careerPlanService.lambdaQuery().eq(CareerPlan::getEmployeeId, employeeId).list();
            return ResponseEntity.ok(careerPlans);
        } catch (Exception e) {
            logger.error("根据员工ID查询职业规划时发生错误，员工ID: {}" , employeeId, e);
            return ResponseEntity.badRequest().body(java.util.Map.of("success", false, "message", "查询失败: " + e.getMessage()));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateCareerPlan(@PathVariable Long id, @RequestBody CareerPlan careerPlan) {
        try {
            // 检查是否是当前用户的职业规划
            Long currentUserId = getCurrentUserId();
            if (currentUserId == null) {
                return ResponseEntity.badRequest().body(java.util.Map.of("success", false, "message", "用户未登录"));
            }
            
            CareerPlan existingCareerPlan = careerPlanService.getById(id);
            if (existingCareerPlan == null) {
                return ResponseEntity.badRequest().body(java.util.Map.of("success", false, "message", "职业规划不存在"));
            }
            
            if (!existingCareerPlan.getEmployeeId().equals(currentUserId)) {
                return ResponseEntity.badRequest().body(java.util.Map.of("success", false, "message", "无权更新该职业规划"));
            }
            
            careerPlan.setId(id);
            boolean result = careerPlanService.updateById(careerPlan);
            if (result) {
                return ResponseEntity.ok().body(java.util.Map.of("success", true, "message", "更新成功"));
            } else {
                return ResponseEntity.badRequest().body(java.util.Map.of("success", false, "message", "更新失败"));
            }
        } catch (Exception e) {
            logger.error("更新职业规划时发生错误，ID: {}" , id, e);
            return ResponseEntity.badRequest().body(java.util.Map.of("success", false, "message", "更新失败: " + e.getMessage()));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCareerPlan(@PathVariable Long id) {
        try {
            // 检查是否是当前用户的职业规划
            Long currentUserId = getCurrentUserId();
            if (currentUserId == null) {
                return ResponseEntity.badRequest().body(java.util.Map.of("success", false, "message", "用户未登录"));
            }
            
            CareerPlan existingCareerPlan = careerPlanService.getById(id);
            if (existingCareerPlan == null) {
                return ResponseEntity.badRequest().body(java.util.Map.of("success", false, "message", "职业规划不存在"));
            }
            
            if (!existingCareerPlan.getEmployeeId().equals(currentUserId)) {
                return ResponseEntity.badRequest().body(java.util.Map.of("success", false, "message", "无权删除该职业规划"));
            }
            
            boolean result = careerPlanService.removeById(id);
            if (result) {
                return ResponseEntity.ok().body(java.util.Map.of("success", true, "message", "删除成功"));
            } else {
                return ResponseEntity.badRequest().body(java.util.Map.of("success", false, "message", "删除失败"));
            }
        } catch (Exception e) {
            logger.error("删除职业规划时发生错误，ID: {}", id, e);
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