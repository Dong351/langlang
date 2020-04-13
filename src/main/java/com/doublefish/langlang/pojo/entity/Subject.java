package com.doublefish.langlang.pojo.entity;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import java.util.Date;

@Data
public class Subject {
    @Id
    private Integer id;

    private String name;

    @Column(name = "createTime")
    private Date createTime;

    private Integer uid;

    private Integer cid;
}
