package com.doublefish.langlang.pojo.entity;

import lombok.Data;

import javax.persistence.Id;

@Data
public class User {
    @Id
    private Integer uid;
    private String phone;
    private String email;
    private String username;
    private String password;
    private Integer type;       //身份1为学生 0为老师
    private String accesstoken;
}
