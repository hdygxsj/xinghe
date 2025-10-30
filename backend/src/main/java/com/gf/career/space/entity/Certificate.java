package com.gf.career.space.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("certificate")
public class Certificate {
    @TableId(type = IdType.AUTO)
    private Long id;
    private Long employeeId;
    private String certificateType; // 年度考核证明、在职联系人证明、荣誉证明
    private String title;
    private String description;
    private LocalDateTime issueDate;
    private String issuer;
    private String certificateUrl;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}