package com.doublefish.langlang.pojo.entity;

import lombok.Data;

import javax.persistence.Id;

@Data
public class Student {
    @Id
    private Integer uid;
    private String school;
    private Integer grade;
}
