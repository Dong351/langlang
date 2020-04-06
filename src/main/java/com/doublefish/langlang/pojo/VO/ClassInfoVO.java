package com.doublefish.langlang.pojo.VO;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.Date;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ClassInfoVO {
    private Integer id;
    private Integer uid;
    private String createPeople;
    private String name;
    private Date createTime;
    private Integer number;
}
