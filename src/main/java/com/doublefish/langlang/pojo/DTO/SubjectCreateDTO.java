package com.doublefish.langlang.pojo.DTO;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class SubjectCreateDTO {

    @NotBlank(message = "课程名称不能为空")
    private String name;

    private Integer cid;
}
