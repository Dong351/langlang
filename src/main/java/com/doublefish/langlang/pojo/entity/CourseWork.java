package com.doublefish.langlang.pojo.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Data
@Table(name = "coursework")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CourseWork {
    public static final String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";

    @Id
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
}
