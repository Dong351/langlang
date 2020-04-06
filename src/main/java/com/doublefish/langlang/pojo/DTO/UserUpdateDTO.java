package com.doublefish.langlang.pojo.DTO;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Data
public class UserUpdateDTO {
    private String username;

    @NotBlank(message = "{user.register.not-blank.email}")
    @Email(message = "{user.register.patten.email}")
    private String email;

    private Integer userType;

    private String school;

    private String teachId;

    private Integer grade;

    private String phone;

}
