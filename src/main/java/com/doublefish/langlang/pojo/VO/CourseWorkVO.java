package com.doublefish.langlang.pojo.VO;

import com.doublefish.langlang.pojo.entity.SelectFill;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CourseWorkVO {
    public static final String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";

    private Integer id;

    private String name;

//    private Date startTime;

    private String introduction;

    private Integer img_number;

    @JsonFormat(pattern = DATE_FORMAT)
    private Date start_time;

    @JsonFormat(pattern = DATE_FORMAT)
    private Date end_time;

    private Integer sid;

    private Integer state;

    private String score;

    private List<SelectFill> selectFillList;
}
