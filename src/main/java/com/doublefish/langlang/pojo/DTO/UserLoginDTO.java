package com.doublefish.langlang.pojo.DTO;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class UserLoginDTO {
    @NotBlank(message = "{user.register.not-blank.phone}")
    private String phone;

    @NotBlank(message = "{user.register.not-blank.password}")
    private String password;
}
