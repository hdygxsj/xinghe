package com.gf.career.space.filter;

import com.gf.career.space.entity.Employee;
import com.gf.career.space.service.EmployeeService;
import com.gf.career.space.util.JwtUtil;
import com.gf.career.space.util.UserContextHolder;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;

@Component
public class JwtFilter implements HandlerInterceptor {

    @Autowired
    private JwtUtil jwtUtil;
    
    @Autowired
    private EmployeeService employeeService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        final String requestTokenHeader = request.getHeader("Authorization");

        System.out.println("JWT Filter - Request URL: " + request.getRequestURL());
        System.out.println("JWT Filter - Authorization Header: " + requestTokenHeader);

        String username = null;
        String jwtToken = null;

        // JWT Token is in the form "Bearer token". Remove Bearer word and get only the Token
        if (requestTokenHeader != null && requestTokenHeader.startsWith("Bearer ")) {
            jwtToken = requestTokenHeader.substring(7);
            try {
                username = jwtUtil.extractUsername(jwtToken);
                System.out.println("JWT Filter - Extracted username: " + username);
            } catch (Exception e) {
                System.out.println("JWT Filter - Unable to extract username from token: " + e.getMessage());
            }
        } else {
            System.out.println("JWT Filter - No valid Authorization header found");
        }

        // Once we get the token validate it.
        if (username != null) {
            try {
                // 从数据库获取用户信息并保存到上下文中
                Employee employee = employeeService.lambdaQuery().eq(Employee::getUsername, username).one();
                if (employee != null) {
                    System.out.println("JWT Filter - Loaded employee details for: " + username);
                    UserContextHolder.setUser(employee);
                } else {
                    System.out.println("JWT Filter - Employee not found for username: " + username);
                }
            } catch (Exception e) {
                return false;
            }
        } else {
            System.out.println("JWT Filter - Username is null");
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        UserContextHolder.clear();
    }
}