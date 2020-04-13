package com.doublefish.langlang.controller;

import com.doublefish.langlang.pojo.DTO.SubjectCreateDTO;
import com.doublefish.langlang.pojo.entity.User;
import com.doublefish.langlang.service.SubjectService;
import com.doublefish.langlang.token.Token;
import com.doublefish.langlang.utils.JsonResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.async.WebAsyncTask;

@RestController
@RequestMapping("/subject")
public class SubjectController {
    @Autowired
    SubjectService subjectService;

    @PostMapping("/create")
    public WebAsyncTask<Object> CreateSubject(@RequestBody @Validated SubjectCreateDTO dto, @Token User user){
        return new WebAsyncTask<>(()->{
            return JsonResult.ok(subjectService.CreateSubject(dto,user));
        });
    }

    @DeleteMapping("/delete/{subjectId}")
    public WebAsyncTask<Object> DeleteSubject(@PathVariable("subjectId") Integer sid, @Token User user){
        return new WebAsyncTask<>(()->{
            return JsonResult.ok(subjectService.DeleteSubject(sid,user));
        });
    }

    //获取当前班级的课程ss
    @GetMapping("/get/{classId}")
    public WebAsyncTask<Object> getSubjectList(@PathVariable("classId") Integer cid,@Token User user){
        return new WebAsyncTask<>(()->{
            return JsonResult.ok(subjectService.getSubjectList(cid,user));
        });
    }
}
