package com.doublefish.langlang.controller;

import com.doublefish.langlang.pojo.entity.User;
import com.doublefish.langlang.service.LiveService;
import com.doublefish.langlang.token.Token;
import com.doublefish.langlang.utils.JsonResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.async.WebAsyncTask;

@RequestMapping("/live")
@RestController
public class LiveController {
    @Autowired
    LiveService liveService;

    @PostMapping("/create/{subjectId}")
    public WebAsyncTask<Object> CreateLive(String url, @PathVariable("subjectId") Integer sid,
                                                 @Token User user){
        return new WebAsyncTask<>(()-> JsonResult.ok(liveService.CreateLive(url,sid,user)));
    }

    @GetMapping("/join/{subjectId}")
    public WebAsyncTask<Object> JoinLive(@PathVariable("subjectId") Integer sid,
                                           @Token User user){
        return new WebAsyncTask<>(()-> JsonResult.ok(liveService.JoinLive(sid,user)));
    }

    @GetMapping("/close/{subjectId}")
    public WebAsyncTask<Object> CloseLive(@PathVariable("subjectId") Integer sid,
                                         @Token User user){
        return new WebAsyncTask<>(()-> JsonResult.ok(liveService.CloseLive(sid,user)));
    }
}
