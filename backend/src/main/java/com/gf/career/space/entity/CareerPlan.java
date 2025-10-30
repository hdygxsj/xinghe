package com.gf.career.space.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("career_plan")
public class CareerPlan {
    @TableId(type = IdType.AUTO)
    private Long id;
    private Long employeeId;
    private String title;
    private String description;
    private String targetPosition;
    private LocalDateTime targetDate;
    private String status; // DRAFT, ACTIVE, COMPLETED, CANCELLED
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}