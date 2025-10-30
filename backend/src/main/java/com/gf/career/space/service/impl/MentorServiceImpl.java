package com.gf.career.space.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gf.career.space.entity.Mentor;
import com.gf.career.space.mapper.MentorMapper;
import com.gf.career.space.service.MentorService;
import org.springframework.stereotype.Service;

@Service
public class MentorServiceImpl extends ServiceImpl<MentorMapper, Mentor> implements MentorService {
}