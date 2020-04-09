package com.doublefish.langlang.controller;

import com.doublefish.langlang.pojo.entity.User;
import com.doublefish.langlang.service.ClassService;
import com.doublefish.langlang.token.Token;
import com.doublefish.langlang.utils.JsonResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.async.WebAsyncTask;

@RestController
@RequestMapping("/class")
public class ClassController {
    @Autowired
    ClassService classService;

    @PostMapping("/create")
    public WebAsyncTask<Object> CreateClass(String className,
                                            @Token User user){
        return new WebAsyncTask<>(()-> JsonResult.ok(classService.CreateClass(className,user)));
    }

    @PostMapping("/join")
    public WebAsyncTask<Object> JoinClass(String code,
                                            @Token User user){
        return new WebAsyncTask<>(()-> JsonResult.ok(classService.JoinClass(code,user)));
    }

    //拉取参加的课程
    @GetMapping("/get")
    public WebAsyncTask<Object> GetClass(@Token User user){
        return new WebAsyncTask<>(()-> JsonResult.ok(classService.GetClass(user)));
    }

    @GetMapping("/get/{classId}")
    public WebAsyncTask<Object> GetClassInfo(@PathVariable("classId")Integer classId){
        return new WebAsyncTask<>(()-> JsonResult.ok(classService.GetClassInfo(classId)));
    }

    @GetMapping("/getCode/{classId}")
    public WebAsyncTask<Object> GetCode(@PathVariable("classId")Integer classId){
        return new WebAsyncTask<>(()-> JsonResult.ok(classService.GetCode(classId)));
    }

    @DeleteMapping("/delete/{classId}/{uid}")
    public WebAsyncTask<Object> DeleteMember(@PathVariable("classId") Integer cid,
                                             @PathVariable("uid")Integer uid,@Token User user){
        return new WebAsyncTask<>(()-> JsonResult.ok(classService.DeleteMember(cid,uid,user)));
    }

}
