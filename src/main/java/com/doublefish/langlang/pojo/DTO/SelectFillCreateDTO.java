package com.doublefish.langlang.pojo.DTO;

import com.doublefish.langlang.pojo.entity.SelectFill;
import lombok.Data;

import java.util.List;

@Data
public class SelectFillCreateDTO {
    private String name;

    private String start_time;

    private String end_time;

    private List<SelectFill> selectFillList;
}
