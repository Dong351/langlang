package com.doublefish.langlang.pojo.entity;

import lombok.Data;

import javax.persistence.Table;

@Data
@Table(name = "user_class")
public class UserClass {
    private Integer uid;
    private Integer cid;
}
