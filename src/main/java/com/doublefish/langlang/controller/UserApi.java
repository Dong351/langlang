package com.doublefish.langlang.controller;

import com.doublefish.langlang.annotation.UserLoginToken;
import com.doublefish.langlang.service.TokenService;
import com.doublefish.langlang.service.UserService;
import com.doublefish.langlang.utils.TokenUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserApi {
    @Autowired
    UserService userService;
    @Autowired
    TokenService tokenService;

    // 登录
//    @GetMapping("/login")
//    public Object login(User user, HttpServletResponse response) {
//        JSONObject jsonObject = new JSONObject();
//        User userForBase = new User();
//        userForBase.setId(Integer.valueOf("6"));
//        userForBase.setPassword("123");
//        userForBase.setUsername("mrc");
//
//        if (!userForBase.getPassword().equals(user.getPassword())) {
//            jsonObject.put("message", "登录失败,密码错误");
//            return jsonObject;
//        } else {
//            String token = tokenService.getToken(userForBase);
//            jsonObject.put("token", token);
//
//            Cookie cookie = new Cookie("token", token);
//            cookie.setPath("/");
//            response.addCookie(cookie);
//
//            return jsonObject;
//
//        }
//    }

    /***
     * 这个请求需要验证token才能访问
     *
     * @author: MRC
     * @date 2019年5月27日 下午5:45:19
     * @return String 返回类型
     */
    @UserLoginToken
    @GetMapping("/getMessage")
    public String getMessage() {

        // 取出token中带的用户id 进行操作
        System.out.println(TokenUtils.getTokenUserId());

        return "你已通过验证";
    }
}
