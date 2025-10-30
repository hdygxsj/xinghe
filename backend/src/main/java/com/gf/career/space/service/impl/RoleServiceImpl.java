package com.gf.career.space.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gf.career.space.entity.Role;
import com.gf.career.space.mapper.RoleMapper;
import com.gf.career.space.service.RoleService;
import org.springframework.stereotype.Service;

@Service
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role> implements RoleService {
}
