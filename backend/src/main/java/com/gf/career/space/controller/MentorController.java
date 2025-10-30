package com.gf.career.space.controller;

import com.gf.career.space.entity.Mentor;
import com.gf.career.space.service.MentorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/mentors")
public class MentorController {

    @Autowired
    private MentorService mentorService;

    @PostMapping
    public boolean createMentor(@RequestBody Mentor mentor) {
        return mentorService.save(mentor);
    }

    @GetMapping("/{id}")
    public Mentor getMentorById(@PathVariable Long id) {
        return mentorService.getById(id);
    }

    @GetMapping
    public List<Mentor> getAllMentors() {
        return mentorService.list();
    }

    @PutMapping("/{id}")
    public boolean updateMentor(@PathVariable Long id, @RequestBody Mentor mentor) {
        mentor.setId(id);
        return mentorService.updateById(mentor);
    }

    @DeleteMapping("/{id}")
    public boolean deleteMentor(@PathVariable Long id) {
        return mentorService.removeById(id);
    }
}