package com.gf.career.space.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("mentorship")
public class Mentorship {
    @TableId(type = IdType.AUTO)
    private Long id;
    private Long mentorId;
    private Long menteeId;
    private String status; // REQUESTED, ACCEPTED, REJECTED, COMPLETED
    private String goals;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}