package com.doublefish.langlang.controller;

import com.doublefish.langlang.bean.User;
import com.doublefish.langlang.mapper.UserMapper;
import com.doublefish.langlang.service.TokenService;
import com.doublefish.langlang.service.UserService;
import com.doublefish.langlang.utils.Result;
import net.minidev.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

@RestController
public class UserController {
    @Autowired
    UserService userService;
    @Autowired
    TokenService tokenService;
    @Autowired
    UserMapper userMapper;

    @PostMapping("/regist")
    public Result regist(User user){
        Result result = userService.regist(user);
        return result;
    }

    @GetMapping("/login")
    public JSONObject login(User user, HttpServletResponse response){
        JSONObject jsonObject = new JSONObject();

        User findResult = userMapper.login(user);
        if(findResult == null){
            jsonObject.put("success",false);
            jsonObject.put("Msg","此用户不存在");
        }
        else if(!findResult.getPassword().equals(user.getPassword()))
        {
            jsonObject.put("success",false);
            jsonObject.put("Msg","密码错误");
        }
        else{
            jsonObject.put("success",true);
            jsonObject.put("Msg","登陆成功");
            jsonObject.put("Detail",findResult);
            String token = tokenService.getToken(userMapper.selectByName(user.getUsername()));
            jsonObject.put("token",token);
            Cookie cookie = new Cookie("token",token);
            cookie.setPath("/");
            response.addCookie(cookie);
        }
        return jsonObject;
    }

}
