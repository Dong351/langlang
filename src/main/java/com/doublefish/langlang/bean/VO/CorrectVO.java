package com.doublefish.langlang.bean.VO;

import lombok.Data;

import java.util.List;

@Data
public class CorrectVO {
    private Float score;
    private String evaluation;
    private List<SelectFillVO> selectFillVOList;
}
