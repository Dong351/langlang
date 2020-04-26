package com.doublefish.langlang.pojo.VO;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

@Data
public class ReplyVO {
    private Integer id;

    private String username;

    private String answer;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date create_time;

//        private Integer uid;

    private Integer reply_id;
}
