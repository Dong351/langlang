package com.doublefish.langlang.service;

import com.doublefish.langlang.bean.User;
import com.doublefish.langlang.mapper.UserMapper;
import com.doublefish.langlang.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    UserMapper userMapper;


    public Result regist(User user){
        Result result = new Result();
        result.setSuccess(false);
        result.setDetail(null);

        User findResult = userMapper.selectByName(user.getUsername());
        if(findResult != null){
            result.setSuccess(false);
            result.setMsg("此账户已存在");
        }
        else{
            result.setSuccess(true);
            result.setMsg("注册成功");
            userMapper.insert(user);
            result.setDetail(user);
        }
        return result;
    }

//    public JSONObject login(User user){
//        JSONObject jsonObject = new JSONObject();
//
//        User findResult = userMapper.login(user);
//        if(findResult == null){
//            jsonObject.put("success",false);
//            jsonObject.put("Msg","此用户不存在");
//        }
//        else if(!findResult.getPassword().equals(user.getPassword()))
//        {
//            jsonObject.put("success",false);
//            jsonObject.put("Msg","密码错误");
//        }
//        else{
//            jsonObject.put("success",true);
//            jsonObject.put("Msg","登陆成功");
//            jsonObject.put("Detail",findResult);
//
//        }
//        return jsonObject;
//    }
}
