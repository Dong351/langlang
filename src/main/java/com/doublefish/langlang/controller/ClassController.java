package com.doublefish.langlang.controller;

import com.doublefish.langlang.pojo.entity.User;
import com.doublefish.langlang.service.ClassService;
import com.doublefish.langlang.token.Token;
import com.doublefish.langlang.utils.JsonResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
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

}
