package com.doublefish.langlang.controller;

import com.doublefish.langlang.pojo.entity.User;
import com.doublefish.langlang.service.UnitService;
import com.doublefish.langlang.token.Token;
import com.doublefish.langlang.utils.JsonResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.async.WebAsyncTask;

@RestController
@RequestMapping("/unit")
public class UnitController {

    @Autowired
    UnitService unitService;

    //创建单元
    @PostMapping("/create/{subjectId}")
    public WebAsyncTask<Object> CreateUnit(String UnitName,@PathVariable("subjectId") Integer sid,
                                            @Token User user){
        return new WebAsyncTask<>(()-> JsonResult.ok(unitService.CreateUnit(sid,UnitName,user)));
    }

    //删除指定单元
    @DeleteMapping("/delete/{unitId}")
    public WebAsyncTask<Object> DeleteUnit(@PathVariable("unitId") Integer unitId,
                                         @Token User user){
        return new WebAsyncTask<>(()-> JsonResult.ok(unitService.DeleteUnit(unitId,user)));
    }

    //获取课程里的所有单元
    @GetMapping("/get/{subjectId}")
    public WebAsyncTask<Object> GetUnits(@PathVariable("subjectId") Integer sid,
                                           @Token User user){
        return new WebAsyncTask<>(()-> JsonResult.ok(unitService.GetUnits(sid,user)));
    }

    //获取单元里的所以文件名称
    @GetMapping("/getFiles/{unitId}")
    public WebAsyncTask<Object> getFiles(@PathVariable("unitId") Integer unitId,
                                         @Token User user){
        return new WebAsyncTask<>(()-> JsonResult.ok(unitService.getFiles(unitId,user)));
    }
}
