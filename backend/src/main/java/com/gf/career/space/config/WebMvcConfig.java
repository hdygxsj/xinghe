package com.gf.career.space.config;

import com.gf.career.space.filter.JwtFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Autowired
    private JwtFilter jwtFilter;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 注册JWT拦截器，拦截所有API请求
        registry.addInterceptor(jwtFilter)
                .addPathPatterns("/api/**")
                .excludePathPatterns("/api/auth/register", "/api/auth/login");
    }
    
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOriginPatterns("*")
                .allowedMethods("*")
                .allowedHeaders("*")
                .allowCredentials(true)
                .maxAge(3600);
    }
}