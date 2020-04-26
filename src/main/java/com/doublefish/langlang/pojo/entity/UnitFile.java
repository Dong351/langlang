package com.doublefish.langlang.pojo.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Data
@Table(name = "unit_file")
public class UnitFile {
    @Id
    private Integer id;

    private String file_name;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date create_time;

    @Column(name = "unitId")
    private Integer unitId;
}
