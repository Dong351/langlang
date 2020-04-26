package com.doublefish.langlang.pojo.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import javax.persistence.Id;
import java.util.Date;

@Data
public class Unit {

    @Id
    private Integer id;

    private String name;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date create_time;

    private Integer sid;
}
