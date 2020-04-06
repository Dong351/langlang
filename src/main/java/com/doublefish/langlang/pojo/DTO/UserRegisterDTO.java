package com.doublefish.langlang.pojo.DTO;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

import static com.doublefish.langlang.utils.Constants.REGEX_PHONE_NUMBER;
import static com.doublefish.langlang.utils.Constants.REGEX_USER_LOGIN_PASSWORD;

@Data
public class UserRegisterDTO {

    public static final String DATE_FORMAT = "yyyy-MM-dd HH";

    @NotBlank(message = "{user.register.not-blank.name}")
    private String username;

    @NotBlank(message = "{user.register.not-blank.phone}")
    @Pattern(regexp = REGEX_PHONE_NUMBER, message = "{user.register.patten.phone}")
    private String phone;

    @NotBlank(message = "{user.register.not-blank.password}")
    @Pattern(regexp = REGEX_USER_LOGIN_PASSWORD, message = "{user.register.patten.password}")
    private String password;

    @NotBlank(message = "{user.register.not-blank.email}")
    @Email(message = "{user.register.patten.email}")
    private String email;

    private String school;

    private Integer grade;
    //学生学号
    private String studentId;

    //教师工号
    private String teachId;

    //用户类型
    private Integer type;
}
