package com.gf.career.space.filter;

import com.gf.career.space.entity.Employee;
import com.gf.career.space.service.EmployeeService;
import com.gf.career.space.util.JwtUtil;
import com.gf.career.space.util.UserContextHolder;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component
public class JwtFilter implements HandlerInterceptor {

    private static final Logger logger = LoggerFactory.getLogger(JwtFilter.class);

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private EmployeeService employeeService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        final String requestTokenHeader = request.getHeader("Authorization");

        logger.debug("JWT Filter - Request URL: {}", request.getRequestURL());
        logger.debug("JWT Filter - Authorization Header: {}", requestTokenHeader);

        String username = null;
        String jwtToken = null;

        // JWT Token is in the form "Bearer token". Remove Bearer word and get only the Token
        if (requestTokenHeader != null && requestTokenHeader.startsWith("Bearer ")) {
            jwtToken = requestTokenHeader.substring(7);
            try {
                username = jwtUtil.extractUsername(jwtToken);
                logger.debug("JWT Filter - Extracted username: {}", username);
            } catch (Exception e) {
                logger.warn("JWT Filter - Unable to extract username from token: {}", e.getMessage());
            }
        } else {
            logger.debug("JWT Filter - No valid Authorization header found");
        }

        // Once we get the token validate it.
        if (username != null) {
            try {
                // 从数据库获取用户信息并保存到上下文中
                Employee employee = employeeService.lambdaQuery().eq(Employee::getUsername, username).one();
                if (employee != null) {
                    logger.debug("JWT Filter - Loaded employee details for: {}", username);
                    UserContextHolder.setUser(employee);
                } else {
                    logger.warn("JWT Filter - Employee not found for username: {}", username);
                }
            } catch (Exception e) {
                logger.error("JWT Filter - Error loading employee details", e);
                return false;
            }
        } else {
            logger.debug("JWT Filter - Username is null");
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        UserContextHolder.clear();
    }
}