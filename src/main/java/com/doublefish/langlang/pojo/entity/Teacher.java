package com.doublefish.langlang.pojo.entity;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;

@Data
public class Teacher {
    @Id
    private Integer uid;
    private String school;
    @Column(name = "teachId")
    private String teachId;
}
