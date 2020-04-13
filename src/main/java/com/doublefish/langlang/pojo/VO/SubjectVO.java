package com.doublefish.langlang.pojo.VO;

import lombok.Data;

import java.util.Date;

@Data
public class SubjectVO {
    private Integer id;

    private String name;

    private Date createTime;

    private Integer uid;

    private Integer cid;

    private String createPeople;
}
