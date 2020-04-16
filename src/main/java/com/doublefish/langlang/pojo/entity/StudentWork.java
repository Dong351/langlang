package com.doublefish.langlang.pojo.entity;

import lombok.Data;

import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Data
@Table(name = "student_work")
public class StudentWork {
    @Id
    private Integer id;
    private Integer uid;
    private Integer cwid;
    private Date upload_time;
    private Integer img_number;
    private Float score;
}
