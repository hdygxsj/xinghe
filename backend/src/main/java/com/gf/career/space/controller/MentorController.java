package com.gf.career.space.controller;

import com.gf.career.space.entity.Mentor;
import com.gf.career.space.service.MentorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

@RestController
@RequestMapping("/api/mentors")
public class MentorController {

    private static final Logger logger = LoggerFactory.getLogger(MentorController.class);

    @Autowired
    private MentorService mentorService;

    @GetMapping
    public ResponseEntity<?> getAllMentors() {
        try {
            List<Mentor> mentors = mentorService.list();
            return ResponseEntity.ok(mentors);
        } catch (Exception e) {
            logger.error("查询所有导师时发生错误", e);
            return ResponseEntity.badRequest().body(java.util.Map.of("success", false, "message", "查询失败: " + e.getMessage()));
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
            logger.error("查询导师时发生错误，ID: " + id, e);
            return ResponseEntity.badRequest().body(java.util.Map.of("success", false, "message", "查询失败: " + e.getMessage()));
        }
    }
}