package com.gf.career.space.service;

import com.gf.career.space.entity.Certificate;
import com.lowagie.text.pdf.BaseFont;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;
import org.xhtmlrenderer.pdf.ITextRenderer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayOutputStream;
import java.util.Random;

@Service
public class PdfService {

    private static final Logger logger = LoggerFactory.getLogger(PdfService.class);

    private final TemplateEngine templateEngine;

    public PdfService() {
        ClassLoaderTemplateResolver templateResolver = new ClassLoaderTemplateResolver();
        templateResolver.setTemplateMode("HTML");
        templateResolver.setCharacterEncoding("UTF-8");
        templateResolver.setPrefix("/templates/");
        templateResolver.setSuffix(".html");
        
        templateEngine = new TemplateEngine();
        templateEngine.setTemplateResolver(templateResolver);
    }

    public byte[] generateCertificatePdf(Certificate certificate) throws Exception {
        try {
            Context context = new Context();
            context.setVariable("certificate", certificate);
            context.setVariable("verificationCode", generateVerificationCode());
            
            String htmlContent = templateEngine.process("certificate", context);
            
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            ITextRenderer renderer = new ITextRenderer();
            
            // 配置中文字体支持
            renderer.getFontResolver().addFont("/fonts/simsun.ttc", BaseFont.IDENTITY_H, BaseFont.NOT_EMBEDDED);
            
            // 尝试加载系统字体以支持中文
            boolean fontAdded = false;
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
                    renderer.getFontResolver().addFont(fontPath, BaseFont.IDENTITY_H, BaseFont.NOT_EMBEDDED);
                    logger.debug("成功加载字体: {}", fontPath);
                    fontAdded = true;
                    break;
                } catch (Exception e) {
                    // 继续尝试下一个字体
                }
            }
            
            if (!fontAdded) {
                logger.warn("警告: 未找到中文字体，PDF中的中文可能无法正常显示");
            }
            
            renderer.setDocumentFromString(htmlContent);
            renderer.layout();
            renderer.createPDF(outputStream);
            
            return outputStream.toByteArray();
        } catch (Exception e) {
            logger.error("生成PDF证书失败", e);
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