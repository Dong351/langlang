package com.doublefish.langlang.pojo.entity;

import lombok.Data;

import javax.persistence.Id;
import javax.persistence.Table;

@Data
@Table(name = "courseword")
public class CourseWork {
    @Id
    private Integer id;

    private String name;

//    private Date startTime;

    private String introduction;

    private Integer sid;
}
