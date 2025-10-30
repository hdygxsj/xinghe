package com.gf.career.space.controller;

import com.gf.career.space.entity.Skill;
import com.gf.career.space.service.SkillService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/skills")
public class SkillController {

    @Autowired
    private SkillService skillService;

    @PostMapping
    public boolean createSkill(@RequestBody Skill skill) {
        return skillService.save(skill);
    }

    @GetMapping("/{id}")
    public Skill getSkillById(@PathVariable Long id) {
        return skillService.getById(id);
    }

    @GetMapping
    public List<Skill> getAllSkills() {
        return skillService.list();
    }

    @PutMapping("/{id}")
    public boolean updateSkill(@PathVariable Long id, @RequestBody Skill skill) {
        skill.setId(id);
        return skillService.updateById(skill);
    }

    @DeleteMapping("/{id}")
    public boolean deleteSkill(@PathVariable Long id) {
        return skillService.removeById(id);
    }
}