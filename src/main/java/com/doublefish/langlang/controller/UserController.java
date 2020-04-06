package com.doublefish.langlang.controller;

import com.doublefish.langlang.mapper.UserMapper;
import com.doublefish.langlang.pojo.DTO.*;
import com.doublefish.langlang.pojo.entity.User;
import com.doublefish.langlang.service.UserService;
import com.doublefish.langlang.token.Token;
import com.doublefish.langlang.utils.JsonResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.async.WebAsyncTask;

@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    UserService userService;
    @Autowired
    UserMapper userMapper;

    @PostMapping("/register")
    public WebAsyncTask<Object> register(@RequestBody @Validated UserRegisterDTO dto){
        return new WebAsyncTask<>(()->{
            userService.register(dto);
            userService.insertTypeUser(dto);
            return JsonResult.ok(userService.findUserInfoByDTO(dto));
        });
    }

    @PostMapping("/login")
    public WebAsyncTask<Object> login(@RequestBody @Validated UserLoginDTO dto){
        return new WebAsyncTask<>(()-> JsonResult.ok(userService.login(dto)));
    }

    @PutMapping("/update")
    public WebAsyncTask<Object> update(@RequestBody @Validated UserUpdateDTO dto,
                                       @Token User user){
        return new WebAsyncTask<>(()-> JsonResult.ok(userService.updateUserInfo(dto,user)));

    }

    @PutMapping("/modifyPassword")
    public WebAsyncTask<Object> modifyPassword(@RequestBody @Validated UserModifyPasswordDTO dto,
                                               @Token User user){
        return new WebAsyncTask<>(()->{
            userService.modifyPassword(dto,user);
            return JsonResult.ok();
        });
    }

    @PostMapping("/resetPassword")
    public WebAsyncTask<Object> resetPassword(@RequestBody @Validated UserResetPasswordDTO dto){
        return new WebAsyncTask<>(()->{
            userService.resetPassword(dto);
            return JsonResult.ok();
        });
    }

}
