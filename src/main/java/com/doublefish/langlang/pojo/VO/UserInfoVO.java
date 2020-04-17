package com.doublefish.langlang.pojo.VO;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.Date;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserInfoVO {
    //user表参数
    private Integer uid;
    private String phone;
    private String username;
    private String email;
    private String accesstoken;
    private Integer userType;

    //student表参数
    private String school;
    private Integer grade;

    //teacher表参数
    private String teachId;
    private String typeName;

    //作业相关参数
    private Integer state;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date upload_time;
    private Float score;

}
