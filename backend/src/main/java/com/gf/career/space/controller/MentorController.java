package com.gf.career.space.controller;

import com.gf.career.space.entity.Mentor;
import com.gf.career.space.service.MentorService;
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
@RequestMapping("/api/mentors")
public class MentorController {

    @Autowired
    private MentorService mentorService;

    @PostMapping
    public ResponseEntity<?> createMentor(@RequestBody Mentor mentor) {
        try {
            boolean result = mentorService.save(mentor);
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
    public ResponseEntity<?> getMentorById(@PathVariable Long id) {
        try {
            Mentor mentor = mentorService.getById(id);
            if (mentor != null) {
                return ResponseEntity.ok(mentor);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body(java.util.Map.of("success", false, "message", "查询失败: " + e.getMessage()));
        }
    }

    @GetMapping
    public ResponseEntity<?> getAllMentors() {
        try {
            List<Mentor> mentors = mentorService.list();
            return ResponseEntity.ok(mentors);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body(java.util.Map.of("success", false, "message", "查询失败: " + e.getMessage()));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateMentor(@PathVariable Long id, @RequestBody Mentor mentor) {
        try {
            mentor.setId(id);
            boolean result = mentorService.updateById(mentor);
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
    public ResponseEntity<?> deleteMentor(@PathVariable Long id) {
        try {
            boolean result = mentorService.removeById(id);
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