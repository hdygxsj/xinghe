package com.gf.career.space.controller;

import com.gf.career.space.entity.Certificate;
import com.gf.career.space.service.CertificateService;
import com.gf.career.space.service.PdfService;
import com.gf.career.space.util.UserContextHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/certificates")
public class CertificateController {

    @Autowired
    private CertificateService certificateService;
    
    @Autowired
    private PdfService pdfService;

    @PostMapping
    public ResponseEntity<?> createCertificate(@RequestBody Certificate certificate) {
        try {
            // 从上下文中获取当前用户ID
            Long currentUserId = getCurrentUserId();
            if (currentUserId == null) {
                return ResponseEntity.badRequest().body(java.util.Map.of("success", false, "message", "用户未登录"));
            }
            
            // 设置员工ID为当前用户ID
            certificate.setEmployeeId(currentUserId);
            
            boolean result = certificateService.save(certificate);
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
    public ResponseEntity<?> getCertificateById(@PathVariable Long id) {
        try {
            Certificate certificate = certificateService.getById(id);
            if (certificate != null) {
                // 检查是否是当前用户的证书
                Long currentUserId = getCurrentUserId();
                if (currentUserId != null && !certificate.getEmployeeId().equals(currentUserId)) {
                    return ResponseEntity.badRequest().body(java.util.Map.of("success", false, "message", "无权访问该证书"));
                }
                
                return ResponseEntity.ok(certificate);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body(java.util.Map.of("success", false, "message", "查询失败: " + e.getMessage()));
        }
    }

    @GetMapping
    public ResponseEntity<?> getAllCertificates() {
        try {
            Long currentUserId = getCurrentUserId();
            if (currentUserId == null) {
                return ResponseEntity.badRequest().body(java.util.Map.of("success", false, "message", "用户未登录"));
            }
            
            List<Certificate> certificates = certificateService.lambdaQuery().eq(Certificate::getEmployeeId, currentUserId).list();
            return ResponseEntity.ok(certificates);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body(java.util.Map.of("success", false, "message", "查询失败: " + e.getMessage()));
        }
    }

    @GetMapping("/employee/{employeeId}")
    public ResponseEntity<?> getCertificatesByEmployeeId(@PathVariable Long employeeId) {
        try {
            // 检查是否是当前用户或有权限访问
            Long currentUserId = getCurrentUserId();
            if (currentUserId == null) {
                return ResponseEntity.badRequest().body(java.util.Map.of("success", false, "message", "用户未登录"));
            }
            
            if (!currentUserId.equals(employeeId)) {
                return ResponseEntity.badRequest().body(java.util.Map.of("success", false, "message", "无权访问该员工的证书"));
            }
            
            List<Certificate> certificates = certificateService.lambdaQuery().eq(Certificate::getEmployeeId, employeeId).list();
            return ResponseEntity.ok(certificates);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body(java.util.Map.of("success", false, "message", "查询失败: " + e.getMessage()));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateCertificate(@PathVariable Long id, @RequestBody Certificate certificate) {
        try {
            // 检查是否是当前用户的证书
            Long currentUserId = getCurrentUserId();
            if (currentUserId == null) {
                return ResponseEntity.badRequest().body(java.util.Map.of("success", false, "message", "用户未登录"));
            }
            
            Certificate existingCertificate = certificateService.getById(id);
            if (existingCertificate == null) {
                return ResponseEntity.badRequest().body(java.util.Map.of("success", false, "message", "证书不存在"));
            }
            
            if (!existingCertificate.getEmployeeId().equals(currentUserId)) {
                return ResponseEntity.badRequest().body(java.util.Map.of("success", false, "message", "无权更新该证书"));
            }
            
            certificate.setId(id);
            boolean result = certificateService.updateById(certificate);
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
    public ResponseEntity<?> deleteCertificate(@PathVariable Long id) {
        try {
            // 检查是否是当前用户的证书
            Long currentUserId = getCurrentUserId();
            if (currentUserId == null) {
                return ResponseEntity.badRequest().body(java.util.Map.of("success", false, "message", "用户未登录"));
            }
            
            Certificate existingCertificate = certificateService.getById(id);
            if (existingCertificate == null) {
                return ResponseEntity.badRequest().body(java.util.Map.of("success", false, "message", "证书不存在"));
            }
            
            if (!existingCertificate.getEmployeeId().equals(currentUserId)) {
                return ResponseEntity.badRequest().body(java.util.Map.of("success", false, "message", "无权删除该证书"));
            }
            
            boolean result = certificateService.removeById(id);
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
    
    @GetMapping("/{id}/download")
    public ResponseEntity<byte[]> downloadCertificate(@PathVariable Long id) {
        try {
            Certificate certificate = certificateService.getById(id);
            if (certificate != null) {
                // 检查是否是当前用户的证书
                Long currentUserId = getCurrentUserId();
                if (currentUserId != null && !certificate.getEmployeeId().equals(currentUserId)) {
                    return ResponseEntity.badRequest().body(new byte[0]);
                }
                
                // 生成PDF证书
                byte[] pdfContent = pdfService.generateCertificatePdf(certificate);
                
                // 使用URL编码处理中文文件名
                String fileName = certificate.getTitle() + ".pdf";
                String encodedFileName = java.net.URLEncoder.encode(fileName, "UTF-8").replaceAll("\\+", "%20");
                
                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.APPLICATION_PDF);
                // 使用RFC 5987的filename*格式支持中文文件名
                headers.set(HttpHeaders.CONTENT_DISPOSITION, 
                    "attachment; filename=\"" + encodedFileName + "\"; filename*=UTF-8''" + encodedFileName);
                headers.setContentLength(pdfContent.length);
                
                return ResponseEntity.ok()
                        .headers(headers)
                        .body(pdfContent);
            }
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
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