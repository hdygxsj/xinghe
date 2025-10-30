package com.gf.career.space.controller;

import com.gf.career.space.entity.Certificate;
import com.gf.career.space.entity.Employee;
import com.gf.career.space.service.CertificateService;
import com.gf.career.space.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.SecureRandom;

@Controller
@RequestMapping("/certificates")
public class CertificateViewController {
    
    @Autowired
    private CertificateService certificateService;
    
    @Autowired
    private EmployeeService employeeService;
    
    @GetMapping("/{id}/view")
    public String viewCertificate(@PathVariable Long id, Model model) {
        Certificate certificate = certificateService.getById(id);
        if (certificate != null) {
            model.addAttribute("certificate", certificate);
            
            // 获取员工信息
            Employee employee = employeeService.getById(certificate.getEmployeeId());
            if (employee != null) {
                model.addAttribute("employeeName", employee.getName());
            } else {
                model.addAttribute("employeeName", "未知员工");
            }
            
            // 生成验证码
            model.addAttribute("verificationCode", generateVerificationCode());
            
            return "certificate";
        }
        return "error/404";
    }
    
    @GetMapping("/{id}/download")
    public String downloadCertificate(@PathVariable Long id, Model model) {
        Certificate certificate = certificateService.getById(id);
        if (certificate != null) {
            model.addAttribute("certificate", certificate);
            
            // 获取员工信息
            Employee employee = employeeService.getById(certificate.getEmployeeId());
            if (employee != null) {
                model.addAttribute("employeeName", employee.getName());
            } else {
                model.addAttribute("employeeName", "未知员工");
            }
            
            // 生成验证码
            model.addAttribute("verificationCode", generateVerificationCode());
            
            return "certificate";
        }
        return "error/404";
    }
    
    private String generateVerificationCode() {
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        SecureRandom random = new SecureRandom();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 6; i++) {
            sb.append(chars.charAt(random.nextInt(chars.length())));
        }
        return sb.toString();
    }
}