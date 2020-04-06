package com.doublefish.langlang.pojo.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Setter
@Getter
@ToString
@Table(name = "reset_mail_code")
public class ResetMailCode {

    @Id
    @Column(name = "id")
    private Integer id;

    @Column(name = "uid")
    private Integer uid;

    @Column(name = "time")
    private Date date;

    @Column(name = "code")
    private String code;

    @Column(name = "email")
    private String email;
}
