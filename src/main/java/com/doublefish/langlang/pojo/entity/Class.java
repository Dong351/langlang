package com.doublefish.langlang.pojo.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import java.util.Date;

@Data
public class Class {
    public static final String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";

    @Id
    private Integer id;
    private Integer uid;
    private String name;

    @Column(name = "createTime")
    @JsonFormat(pattern = DATE_FORMAT)
    private Date createTime;

    private Integer number;

    private String code;
}
