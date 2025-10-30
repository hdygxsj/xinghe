package com.gf.career.space.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gf.career.space.entity.CareerPlan;
import com.gf.career.space.mapper.CareerPlanMapper;
import com.gf.career.space.service.CareerPlanService;
import org.springframework.stereotype.Service;

@Service
public class CareerPlanServiceImpl extends ServiceImpl<CareerPlanMapper, CareerPlan> implements CareerPlanService {
}