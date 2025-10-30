package com.gf.career.space.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("skill")
public class Skill {
    @TableId(type = IdType.AUTO)
    private Long id;
    private String name;
    private String category; // 技能类型：技术技能、软技能、专业技能、语言技能、管理技能
    private String description;
    private Integer level; // 1-5级技能等级
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}