package com.doublefish.langlang.pojo.entity;

import lombok.Data;

import javax.persistence.Id;
import java.util.Date;

@Data
public class Live {
    @Id
    private Integer id;

    private Integer sid;

    private String url;

    private Date create_time;
}
