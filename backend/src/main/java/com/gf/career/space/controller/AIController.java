package com.gf.career.space.controller;

import com.gf.career.space.service.AIService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/ai")
public class AIController {

    @Autowired
    private AIService aiService;

    @GetMapping("/career-advice")
    public String getCareerAdvice(@RequestParam Long employeeId) {
        return aiService.generateCareerAdvice(employeeId);
    }

    @GetMapping("/learning-path")
    public String getLearningPath(@RequestParam Long employeeId) {
        return aiService.recommendLearningPath(employeeId);
    }

    @GetMapping("/resume-optimize")
    public String optimizeResume(@RequestParam Long employeeId) {
        return aiService.optimizeResume(employeeId);
    }
}