package com.doublefish.langlang.pojo.DTO;

import lombok.Data;

@Data
public class DiscussionCreateDTO {
    private String title;

    private String content;

    private Integer replyId;
}
