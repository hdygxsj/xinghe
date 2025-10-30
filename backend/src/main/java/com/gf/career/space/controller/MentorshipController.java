package com.gf.career.space.controller;

import com.gf.career.space.entity.Mentorship;
import com.gf.career.space.service.MentorshipService;
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

import java.util.List;

@RestController
@RequestMapping("/api/mentorships")
public class MentorshipController {

    @Autowired
    private MentorshipService mentorshipService;

    @PostMapping
    public ResponseEntity<?> createMentorship(@RequestBody Mentorship mentorship) {
        try {
            // 从上下文中获取当前用户ID
            Long currentUserId = getCurrentUserId();
            if (currentUserId == null) {
                return ResponseEntity.badRequest().body(java.util.Map.of("success", false, "message", "用户未登录"));
            }
            
            // 设置学员ID为当前用户ID
            mentorship.setMenteeId(currentUserId);
            
            boolean result = mentorshipService.save(mentorship);
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
    public ResponseEntity<?> getMentorshipById(@PathVariable Long id) {
        try {
            Mentorship mentorship = mentorshipService.getById(id);
            if (mentorship != null) {
                // 检查是否是当前用户相关的导师关系
                Long currentUserId = getCurrentUserId();
                if (currentUserId != null && !mentorship.getMenteeId().equals(currentUserId) && !mentorship.getMentorId().equals(currentUserId)) {
                    return ResponseEntity.badRequest().body(java.util.Map.of("success", false, "message", "无权访问该导师关系"));
                }
                
                return ResponseEntity.ok(mentorship);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body(java.util.Map.of("success", false, "message", "查询失败: " + e.getMessage()));
        }
    }

    @GetMapping
    public ResponseEntity<?> getAllMentorships() {
        try {
            Long currentUserId = getCurrentUserId();
            if (currentUserId == null) {
                return ResponseEntity.badRequest().body(java.util.Map.of("success", false, "message", "用户未登录"));
            }
            
            // 获取当前用户作为学员或导师的所有导师关系
            List<Mentorship> mentorships = mentorshipService.lambdaQuery()
                .eq(Mentorship::getMenteeId, currentUserId)
                .or()
                .eq(Mentorship::getMentorId, currentUserId)
                .list();
                
            return ResponseEntity.ok(mentorships);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body(java.util.Map.of("success", false, "message", "查询失败: " + e.getMessage()));
        }
    }

    @GetMapping("/mentor/{mentorId}")
    public ResponseEntity<?> getMentorshipsByMentorId(@PathVariable Long mentorId) {
        try {
            // 检查是否是当前用户或有权限访问
            Long currentUserId = getCurrentUserId();
            if (currentUserId == null) {
                return ResponseEntity.badRequest().body(java.util.Map.of("success", false, "message", "用户未登录"));
            }
            
            if (!currentUserId.equals(mentorId)) {
                return ResponseEntity.badRequest().body(java.util.Map.of("success", false, "message", "无权访问该导师的导师关系"));
            }
            
            List<Mentorship> mentorships = mentorshipService.lambdaQuery().eq(Mentorship::getMentorId, mentorId).list();
            return ResponseEntity.ok(mentorships);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body(java.util.Map.of("success", false, "message", "查询失败: " + e.getMessage()));
        }
    }

    @GetMapping("/mentee/{menteeId}")
    public ResponseEntity<?> getMentorshipsByMenteeId(@PathVariable Long menteeId) {
        try {
            // 检查是否是当前用户或有权限访问
            Long currentUserId = getCurrentUserId();
            if (currentUserId == null) {
                return ResponseEntity.badRequest().body(java.util.Map.of("success", false, "message", "用户未登录"));
            }
            
            if (!currentUserId.equals(menteeId)) {
                return ResponseEntity.badRequest().body(java.util.Map.of("success", false, "message", "无权访问该学员的导师关系"));
            }
            
            List<Mentorship> mentorships = mentorshipService.lambdaQuery().eq(Mentorship::getMenteeId, menteeId).list();
            return ResponseEntity.ok(mentorships);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body(java.util.Map.of("success", false, "message", "查询失败: " + e.getMessage()));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateMentorship(@PathVariable Long id, @RequestBody Mentorship mentorship) {
        try {
            // 检查是否是当前用户相关的导师关系
            Long currentUserId = getCurrentUserId();
            if (currentUserId == null) {
                return ResponseEntity.badRequest().body(java.util.Map.of("success", false, "message", "用户未登录"));
            }
            
            Mentorship existingMentorship = mentorshipService.getById(id);
            if (existingMentorship == null) {
                return ResponseEntity.badRequest().body(java.util.Map.of("success", false, "message", "导师关系不存在"));
            }
            
            if (!existingMentorship.getMenteeId().equals(currentUserId) && !existingMentorship.getMentorId().equals(currentUserId)) {
                return ResponseEntity.badRequest().body(java.util.Map.of("success", false, "message", "无权更新该导师关系"));
            }
            
            mentorship.setId(id);
            boolean result = mentorshipService.updateById(mentorship);
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
    public ResponseEntity<?> deleteMentorship(@PathVariable Long id) {
        try {
            // 检查是否是当前用户相关的导师关系
            Long currentUserId = getCurrentUserId();
            if (currentUserId == null) {
                return ResponseEntity.badRequest().body(java.util.Map.of("success", false, "message", "用户未登录"));
            }
            
            Mentorship existingMentorship = mentorshipService.getById(id);
            if (existingMentorship == null) {
                return ResponseEntity.badRequest().body(java.util.Map.of("success", false, "message", "导师关系不存在"));
            }
            
            if (!existingMentorship.getMenteeId().equals(currentUserId) && !existingMentorship.getMentorId().equals(currentUserId)) {
                return ResponseEntity.badRequest().body(java.util.Map.of("success", false, "message", "无权删除该导师关系"));
            }
            
            boolean result = mentorshipService.removeById(id);
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