package com.gf.career.space.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gf.career.space.entity.Certificate;
import com.gf.career.space.mapper.CertificateMapper;
import com.gf.career.space.service.CertificateService;
import org.springframework.stereotype.Service;

@Service
public class CertificateServiceImpl extends ServiceImpl<CertificateMapper, Certificate> implements CertificateService {
}