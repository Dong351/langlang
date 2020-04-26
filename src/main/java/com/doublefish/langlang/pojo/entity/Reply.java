package com.doublefish.langlang.pojo.entity;

import lombok.Data;

import javax.persistence.Id;
import java.util.Date;

@Data
public class Reply {
    @Id
    private Integer id;

    private String answer;

    private Date create_time;

    private Integer did;

    private Integer uid;

    private Integer reply_id;
}
