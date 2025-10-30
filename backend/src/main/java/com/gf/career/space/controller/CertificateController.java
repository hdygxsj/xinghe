package com.gf.career.space.controller;

import com.gf.career.space.entity.Certificate;
import com.gf.career.space.service.CertificateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/certificates")
public class CertificateController {

    @Autowired
    private CertificateService certificateService;

    @PostMapping
    public boolean createCertificate(@RequestBody Certificate certificate) {
        return certificateService.save(certificate);
    }

    @GetMapping("/{id}")
    public Certificate getCertificateById(@PathVariable Long id) {
        return certificateService.getById(id);
    }

    @GetMapping
    public List<Certificate> getAllCertificates() {
        return certificateService.list();
    }

    @GetMapping("/employee/{employeeId}")
    public List<Certificate> getCertificatesByEmployeeId(@PathVariable Long employeeId) {
        return certificateService.lambdaQuery().eq(Certificate::getEmployeeId, employeeId).list();
    }

    @PutMapping("/{id}")
    public boolean updateCertificate(@PathVariable Long id, @RequestBody Certificate certificate) {
        certificate.setId(id);
        return certificateService.updateById(certificate);
    }

    @DeleteMapping("/{id}")
    public boolean deleteCertificate(@PathVariable Long id) {
        return certificateService.removeById(id);
    }
}