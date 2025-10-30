package com.gf.career.space.controller;

import com.gf.career.space.entity.Skill;
import com.gf.career.space.service.SkillService;
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
@RequestMapping("/api/skills")
public class SkillController {

    @Autowired
    private SkillService skillService;

    @PostMapping
    public ResponseEntity<?> createSkill(@RequestBody Skill skill) {
        try {
            boolean result = skillService.save(skill);
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
    public ResponseEntity<?> getSkillById(@PathVariable Long id) {
        try {
            Skill skill = skillService.getById(id);
            if (skill != null) {
                return ResponseEntity.ok(skill);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body(java.util.Map.of("success", false, "message", "查询失败: " + e.getMessage()));
        }
    }

    @GetMapping
    public ResponseEntity<?> getAllSkills() {
        try {
            List<Skill> skills = skillService.list();
            return ResponseEntity.ok(skills);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body(java.util.Map.of("success", false, "message", "查询失败: " + e.getMessage()));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateSkill(@PathVariable Long id, @RequestBody Skill skill) {
        try {
            skill.setId(id);
            boolean result = skillService.updateById(skill);
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
    public ResponseEntity<?> deleteSkill(@PathVariable Long id) {
        try {
            boolean result = skillService.removeById(id);
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