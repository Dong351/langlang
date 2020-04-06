package com.doublefish.langlang.pojo.VO;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserInfoVO {
    private Integer uid;

    private String phone;

    private String username;

    private String email;

    //比user多一个用户类型名称
    private String typeName;

    private String accesstoken;

    private String school;

    private Integer grade;

    private Integer userType;

    //学生学号
    private String studentId;

    //学生专业
    private String major;

    //教师工号
    private String teachId;
}
