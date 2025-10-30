package com.gf.career.space.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("milestone")
public class Milestone {
    @TableId(type = IdType.AUTO)
    private Long id;
    private Long employeeId;
    private String title;
    private String description;
    private String type; // 入职培训、转正、部门调整、职位晋升、参赛荣誉、重大项目
    private LocalDateTime eventDate;
    private String attachmentUrl;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}