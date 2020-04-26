package com.doublefish.langlang.pojo.entity;

import lombok.Data;

import javax.persistence.Id;
import java.util.Date;

@Data
public class Discussion {
    @Id
    private Integer id;

    private String title;

    private String content;

    private Date create_time;

    private Integer sid;

    private Integer uid;
}
