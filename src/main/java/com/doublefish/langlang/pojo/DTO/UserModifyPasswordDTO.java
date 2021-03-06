package com.doublefish.langlang.pojo.DTO;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

import static com.doublefish.langlang.utils.Constants.REGEX_USER_LOGIN_PASSWORD;

@Data
public class UserModifyPasswordDTO {
    @NotBlank(message = "{user.register.not-blank.phone}")
    private String phone;

    @NotBlank(message = "{user.modifyPassword.not-blank.password}")
    @Pattern(regexp = REGEX_USER_LOGIN_PASSWORD, message = "{user.register.patten.password}")
    private String password;

    @NotBlank(message = "{user.modifyPassword.not-blank,prePassword}")
    private String prePassword;
}
