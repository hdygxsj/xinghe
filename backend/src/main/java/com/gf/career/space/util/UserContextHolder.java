package com.gf.career.space.util;

import com.gf.career.space.entity.Employee;

/**
 * 用户上下文持有者，使用ThreadLocal存储当前用户信息
 */
public class UserContextHolder {
    
    private static final ThreadLocal<Employee> userContext = new ThreadLocal<>();
    
    /**
     * 设置当前用户信息
     * @param employee 当前用户信息
     */
    public static void setUser(Employee employee) {
        userContext.set(employee);
    }
    
    /**
     * 获取当前用户信息
     * @return 当前用户信息
     */
    public static Employee getCurrentUser() {
        return userContext.get();
    }
    
    /**
     * 清除当前用户信息
     */
    public static void clear() {
        userContext.remove();
    }
}