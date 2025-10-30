package com.gf.career.space.service;

import com.gf.career.space.entity.Certificate;
import com.gf.career.space.entity.Employee;
import com.itextpdf.html2pdf.HtmlConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.io.ByteArrayOutputStream;
import java.util.Random;

@Service
public class PdfService {
    
    @Autowired
    private TemplateEngine templateEngine;
    
    @Autowired
    private EmployeeService employeeService;
    
    public byte[] generateCertificatePdf(Certificate certificate) throws Exception {
        try {
            // 获取员工信息
            Employee employee = employeeService.getById(certificate.getEmployeeId());
            String employeeName = employee != null ? employee.getName() : "未知员工";
            
            // 生成HTML内容，使用专门的PDF模板
            Context context = new Context();
            context.setVariable("certificate", certificate);
            context.setVariable("employeeName", employeeName);
            context.setVariable("verificationCode", generateVerificationCode());
            
            String htmlContent = templateEngine.process("certificate-pdf", context);
            
            // 将HTML转换为PDF
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            
            // 使用HtmlConverter转换HTML到PDF
            HtmlConverter.convertToPdf(htmlContent, outputStream);
            
            return outputStream.toByteArray();
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception("生成PDF证书失败: " + e.getMessage(), e);
        }
    }
    
    private String generateVerificationCode() {
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        StringBuilder sb = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < 6; i++) {
            sb.append(chars.charAt(random.nextInt(chars.length())));
        }
        return sb.toString();
    }
}