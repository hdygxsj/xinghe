package com.gf.career.space.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gf.career.space.entity.Mentorship;
import com.gf.career.space.mapper.MentorshipMapper;
import com.gf.career.space.service.MentorshipService;
import org.springframework.stereotype.Service;

@Service
public class MentorshipServiceImpl extends ServiceImpl<MentorshipMapper, Mentorship> implements MentorshipService {
}