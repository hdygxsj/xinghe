package com.gf.career.space.service;

import com.gf.career.space.entity.Certificate;
import com.gf.career.space.entity.Employee;
import com.lowagie.text.pdf.BaseFont;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.xhtmlrenderer.pdf.ITextFontResolver;
import org.xhtmlrenderer.pdf.ITextRenderer;

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
            
            // 生成HTML内容
            Context context = new Context();
            context.setVariable("certificate", certificate);
            context.setVariable("employeeName", employeeName);
            context.setVariable("verificationCode", generateVerificationCode());
            
            String htmlContent = templateEngine.process("certificate", context);
            
            // 使用Flying Saucer转换HTML到PDF
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            
            ITextRenderer renderer = new ITextRenderer();
            
            // 添加中文字体支持
            ITextFontResolver fontResolver = renderer.getFontResolver();
            boolean fontAdded = false;
            
            // 尝试多个字体路径
            String[] fontPaths = {
                // macOS系统字体
                "/System/Library/Fonts/PingFang.ttc",
                "/System/Library/Fonts/Supplemental/Songti.ttc",
                "/Library/Fonts/Songti.ttc",
                // Windows系统字体
                "C:/Windows/Fonts/simsun.ttc",
                "C:/Windows/Fonts/msyh.ttc",
                // Linux系统字体
                "/usr/share/fonts/truetype/wqy/wqy-microhei.ttc",
                "/usr/share/fonts/opentype/noto/NotoSansCJK-Regular.ttc"
            };
            
            for (String fontPath : fontPaths) {
                try {
                    fontResolver.addFont(fontPath, BaseFont.IDENTITY_H, BaseFont.NOT_EMBEDDED);
                    System.out.println("成功加载字体: " + fontPath);
                    fontAdded = true;
                    break;
                } catch (Exception e) {
                    // 继续尝试下一个字体
                }
            }
            
            if (!fontAdded) {
                System.err.println("警告: 未找到中文字体，PDF中的中文可能无法正常显示");
            }
            
            renderer.setDocumentFromString(htmlContent);
            renderer.layout();
            renderer.createPDF(outputStream);
            
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