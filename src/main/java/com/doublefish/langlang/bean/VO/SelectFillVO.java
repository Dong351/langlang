package com.doublefish.langlang.bean.VO;

import com.doublefish.langlang.pojo.entity.SelectFill;
import lombok.Data;

@Data
public class SelectFillVO extends SelectFill {
    private String student_answer;
    private Integer student_score;
}
