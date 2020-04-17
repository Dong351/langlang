package com.doublefish.langlang.controller;

import com.doublefish.langlang.pojo.DTO.CorrectDTO;
import com.doublefish.langlang.pojo.entity.User;
import com.doublefish.langlang.service.CourseWorkService;
import com.doublefish.langlang.token.Token;
import com.doublefish.langlang.utils.JsonResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.async.WebAsyncTask;

@RestController
@RequestMapping("/coursework")
public class CourseWorkController {

    @Autowired
    CourseWorkService courseWorkService;

    //拉取该课程的所有作业
    @GetMapping("/get/{subjectId}")
    public WebAsyncTask<Object> GetCourseWorks(@PathVariable("subjectId")Integer sid, @Token User user){
        return new WebAsyncTask<>(()-> JsonResult.ok(courseWorkService.GetCourseWorks(sid,user)));
    }

    @PutMapping("/get/{courseworkId}")
    public WebAsyncTask<Object> GetCourseWork(@PathVariable("courseworkId")Integer cwid, @Token User user){
        return new WebAsyncTask<>(()-> JsonResult.ok(courseWorkService.GetCourseWork(cwid,user)));
    }

    @DeleteMapping("/delete/{courseworkId}")
    public WebAsyncTask<Object> DeleteCourseWork(@PathVariable("courseworkId")Integer cwid, @Token User user){
        return new WebAsyncTask<>(()-> JsonResult.ok(courseWorkService.DeleteCourseWork(cwid,user)));
    }

    //拉取所有指定作业的学生
    @GetMapping("/studentwork/get/{courseworkId}")
    public WebAsyncTask<Object> GetStudentWorks(@PathVariable("courseworkId")Integer cwid, @Token User user){
        return new WebAsyncTask<>(()-> JsonResult.ok(courseWorkService.GetStudentWorks(cwid,user)));
    }

    @GetMapping("/studentwork/get/{courseworkId}/{uid}")
    public WebAsyncTask<Object> GetStudentWork(@PathVariable("uid")Integer uid, @PathVariable("courseworkId") Integer cwid,
                                               @Token User user){
        return new WebAsyncTask<>(()-> JsonResult.ok(courseWorkService.GetStudentWork(cwid,uid,user)));
    }

    @PostMapping("/studentwork/correct/{courseworkId}/{uid}")
    public WebAsyncTask<Object> CorrectWork(@PathVariable("uid")Integer uid, @PathVariable("courseworkId") Integer cwid,
                                            @RequestBody CorrectDTO dto, @Token User user){
        return new WebAsyncTask<>(()-> JsonResult.ok(courseWorkService.CorrectWork(cwid,uid,user,dto)));
    }
}
