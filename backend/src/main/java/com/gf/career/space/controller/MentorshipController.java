package com.gf.career.space.controller;

import com.gf.career.space.entity.Mentorship;
import com.gf.career.space.service.MentorshipService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/mentorships")
public class MentorshipController {

    @Autowired
    private MentorshipService mentorshipService;

    @PostMapping
    public boolean createMentorship(@RequestBody Mentorship mentorship) {
        return mentorshipService.save(mentorship);
    }

    @GetMapping("/{id}")
    public Mentorship getMentorshipById(@PathVariable Long id) {
        return mentorshipService.getById(id);
    }

    @GetMapping
    public List<Mentorship> getAllMentorships() {
        return mentorshipService.list();
    }

    @GetMapping("/mentor/{mentorId}")
    public List<Mentorship> getMentorshipsByMentorId(@PathVariable Long mentorId) {
        return mentorshipService.lambdaQuery().eq(Mentorship::getMentorId, mentorId).list();
    }

    @GetMapping("/mentee/{menteeId}")
    public List<Mentorship> getMentorshipsByMenteeId(@PathVariable Long menteeId) {
        return mentorshipService.lambdaQuery().eq(Mentorship::getMenteeId, menteeId).list();
    }

    @PutMapping("/{id}")
    public boolean updateMentorship(@PathVariable Long id, @RequestBody Mentorship mentorship) {
        mentorship.setId(id);
        return mentorshipService.updateById(mentorship);
    }

    @DeleteMapping("/{id}")
    public boolean deleteMentorship(@PathVariable Long id) {
        return mentorshipService.removeById(id);
    }
}