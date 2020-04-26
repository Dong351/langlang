package com.doublefish.langlang.controller;

import com.doublefish.langlang.pojo.DTO.DiscussionCreateDTO;
import com.doublefish.langlang.pojo.entity.User;
import com.doublefish.langlang.service.DiscussionService;
import com.doublefish.langlang.token.Token;
import com.doublefish.langlang.utils.JsonResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.async.WebAsyncTask;

@RestController
@RequestMapping("/discussion")
public class DiscussionController {

    @Autowired
    DiscussionService discussionService;

    @PostMapping("/create/{subjectId}")
    public WebAsyncTask<Object> CreateDiscussion(@RequestBody DiscussionCreateDTO dto,@PathVariable("subjectId") Integer sid,
                                            @Token User user){
        return new WebAsyncTask<>(()-> JsonResult.ok(discussionService.CreateDiscussion(dto,sid,user)));
    }

    @GetMapping("/get/{subjectId}")
    public WebAsyncTask<Object> GetDiscussions(@PathVariable("subjectId") Integer sid,
                                                 @Token User user){
        return new WebAsyncTask<>(()-> JsonResult.ok(discussionService.GetDiscussions(sid,user)));
    }

    @GetMapping("/delete/{discussionId}")
    public WebAsyncTask<Object> DeleteDiscussion(@PathVariable("discussionId") Integer did,
                                               @Token User user){
        return new WebAsyncTask<>(()-> JsonResult.ok(discussionService.DeleteDiscussion(did,user)));
    }

    @PostMapping("/reply/create/{discussionId}")
    public WebAsyncTask<Object> CreateReply(@RequestBody DiscussionCreateDTO dto,
                                            @PathVariable("discussionId") Integer did,
                                                 @Token User user){
        return new WebAsyncTask<>(()-> JsonResult.ok(discussionService.CreateReply(dto,did,user)));
    }

    @PutMapping("/get/{discussionId}")
    public WebAsyncTask<Object> GetDiscussion(@PathVariable("discussionId") Integer did,
                                               @Token User user){
        return new WebAsyncTask<>(()-> JsonResult.ok(discussionService.GetDiscussion(did,user)));
    }

    @DeleteMapping("/reply/delete/{replyId}")
    public WebAsyncTask<Object> DeleteReply(@PathVariable("replyId") Integer replyId,
                                              @Token User user){
        return new WebAsyncTask<>(()-> JsonResult.ok(discussionService.DeleteReply(replyId,user)));
    }
}
