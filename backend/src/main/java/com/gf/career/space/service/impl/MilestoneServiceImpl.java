package com.gf.career.space.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gf.career.space.entity.Milestone;
import com.gf.career.space.mapper.MilestoneMapper;
import com.gf.career.space.service.MilestoneService;
import org.springframework.stereotype.Service;

@Service
public class MilestoneServiceImpl extends ServiceImpl<MilestoneMapper, Milestone> implements MilestoneService {
}