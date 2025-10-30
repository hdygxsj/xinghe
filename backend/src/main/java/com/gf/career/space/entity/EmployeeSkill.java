package com.gf.career.space.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("employee_skill")
public class EmployeeSkill {
    @TableId(type = IdType.AUTO)
    private Long id;
    private Long employeeId;
    private Long skillId;
    private Integer proficiencyLevel; // 1-5级熟练度
    private LocalDateTime acquiredDate;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}