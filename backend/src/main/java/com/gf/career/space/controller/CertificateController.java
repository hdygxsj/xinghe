package com.gf.career.space.controller;

import com.gf.career.space.entity.Certificate;
import com.gf.career.space.service.CertificateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
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
    
    @GetMapping("/{id}/download")
    public ResponseEntity<byte[]> downloadCertificate(@PathVariable Long id) {
        // 这里应该实现证书文件的下载逻辑
        // 目前返回一个模拟的证书内容
        Certificate certificate = certificateService.getById(id);
        if (certificate != null) {
            String content = "证书标题: " + certificate.getTitle() + "\n" +
                           "证书类型: " + certificate.getCertificateType() + "\n" +
                           "颁发日期: " + certificate.getIssueDate() + "\n" +
                           "颁发机构: " + certificate.getIssuer() + "\n" +
                           "描述: " + certificate.getDescription();
            
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
            headers.setContentDispositionFormData("attachment", certificate.getTitle() + ".txt");
            
            return new ResponseEntity<>(content.getBytes(), headers, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}