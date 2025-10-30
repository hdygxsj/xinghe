package com.gf.career.space.controller;

import com.gf.career.space.entity.Role;
import com.gf.career.space.service.RoleService;
import com.gf.career.space.util.UserContextHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/roles")
public class RoleController {

    @Autowired
    private RoleService roleService;

    @PostMapping
    public ResponseEntity<?> createRole(@RequestBody Role role) {
        try {
            // 检查是否是管理员
            if (!isAdmin()) {
                return ResponseEntity.badRequest().body(java.util.Map.of("success", false, "message", "只有管理员才能创建角色"));
            }
            
            boolean result = roleService.save(role);
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
    public ResponseEntity<?> getRoleById(@PathVariable Long id) {
        try {
            // 检查是否是管理员
            if (!isAdmin()) {
                return ResponseEntity.badRequest().body(java.util.Map.of("success", false, "message", "只有管理员才能查看角色"));
            }
            
            Role role = roleService.getById(id);
            if (role != null) {
                return ResponseEntity.ok(role);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body(java.util.Map.of("success", false, "message", "查询失败: " + e.getMessage()));
        }
    }

    @GetMapping
    public ResponseEntity<?> getAllRoles() {
        try {
            // 检查是否是管理员
            if (!isAdmin()) {
                return ResponseEntity.badRequest().body(java.util.Map.of("success", false, "message", "只有管理员才能查看角色列表"));
            }
            
            List<Role> roles = roleService.list();
            return ResponseEntity.ok(roles);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body(java.util.Map.of("success", false, "message", "查询失败: " + e.getMessage()));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateRole(@PathVariable Long id, @RequestBody Role role) {
        try {
            // 检查是否是管理员
            if (!isAdmin()) {
                return ResponseEntity.badRequest().body(java.util.Map.of("success", false, "message", "只有管理员才能更新角色"));
            }
            
            role.setId(id);
            boolean result = roleService.updateById(role);
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
    public ResponseEntity<?> deleteRole(@PathVariable Long id) {
        try {
            // 检查是否是管理员
            if (!isAdmin()) {
                return ResponseEntity.badRequest().body(java.util.Map.of("success", false, "message", "只有管理员才能删除角色"));
            }
            
            boolean result = roleService.removeById(id);
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
     * 检查当前用户是否是管理员
     */
    private boolean isAdmin() {
        com.gf.career.space.entity.Employee currentUser = UserContextHolder.getCurrentUser();
        return currentUser != null && "ADMIN".equals(currentUser.getRole());
    }
}
