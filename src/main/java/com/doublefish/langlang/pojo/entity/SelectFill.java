package com.doublefish.langlang.pojo.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@Table(name = "select_fill")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SelectFill {
    @Id
    private Integer id;

    private Integer type;

    private String a;
    private String b;
    private String c;
    private String d;
    private String e;
    private String f;

    private String title;

    private String answer;

    private Integer score;

    @Column(name = "cwId")
    private Integer cwId;
}
