package com.doublefish.langlang.service;

import com.doublefish.langlang.exception.CommonException;
import com.doublefish.langlang.mapper.*;
import com.doublefish.langlang.pojo.DTO.DiscussionCreateDTO;
import com.doublefish.langlang.pojo.VO.DiscussionVO;
import com.doublefish.langlang.pojo.VO.ReplyVO;
import com.doublefish.langlang.pojo.entity.Discussion;
import com.doublefish.langlang.pojo.entity.Reply;
import com.doublefish.langlang.pojo.entity.User;
import com.doublefish.langlang.pojo.entity.UserClass;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class DiscussionService {

    @Autowired
    UserClassMapper userClassMapper;
    @Autowired
    SubjectMapper subjectMapper;
    @Autowired
    DiscussionMapper discussionMapper;
    @Autowired
    ReplyMapper replyMapper;
    @Autowired
    UserMapper userMapper;

    /**
     * 创建讨论答疑区
     * @param dto
     * @param sid
     * @param user  班级token
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public Object CreateDiscussion(DiscussionCreateDTO dto, Integer sid, User user) {
        Integer cid = subjectMapper.selectByPrimaryKey(sid).getCid();
        UserClass findUserClass = new UserClass();
        findUserClass.setUid(user.getUid());
        findUserClass.setCid(cid);
        if(userClassMapper.selectOne(findUserClass) == null){
            throw new CommonException("您不是班级成员，无法创建讨论");
        }

        Discussion discussion = new Discussion();
        BeanUtils.copyProperties(dto,discussion);
        discussion.setCreate_time(new Date());
        discussion.setSid(sid);
        discussion.setUid(user.getUid());

        discussionMapper.insert(discussion);
        return null;
    }

    /**
     * 获取课程内的所有讨论
     * @param sid
     * @param user 班级成员token
     * @return
     */
    public Object GetDiscussions(Integer sid, User user) {
        Integer cid = subjectMapper.selectByPrimaryKey(sid).getCid();
        UserClass findUserClass = new UserClass();
        findUserClass.setUid(user.getUid());
        findUserClass.setCid(cid);
        if(userClassMapper.selectOne(findUserClass) == null){
            throw new CommonException("您不是班级成员，无法创建讨论");
        }

        Discussion findDiscussion = new Discussion();
        findDiscussion.setSid(sid);
        List<Discussion> discussionList = discussionMapper.select(findDiscussion);
        List<DiscussionVO> discussionVOList = new ArrayList<>();
        for(Discussion discussion:discussionList){
            DiscussionVO discussionVO = new DiscussionVO();
            BeanUtils.copyProperties(discussion,discussionVO);
            discussionVO.setCreatePeople(userMapper.selectByPrimaryKey(discussion.getUid()).getUsername());
            discussionVO.setContent(null);
            discussionVOList.add(discussionVO);
        }
        return discussionVOList;
    }

    /**
     * 删除讨论
     * @param did
     * @param user 创建者token或者老师
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public Object DeleteDiscussion(Integer did, User user) {
        if(user.getType() != 2){
            if (discussionMapper.selectByPrimaryKey(did).getUid() != user.getUid()){
                throw new CommonException("您不是创建者，无法删除讨论");
            }
        }

        Reply findReply = new Reply();
        findReply.setDid(did);
        List<Reply> select = replyMapper.select(findReply);
        for(Reply reply:select){
            replyMapper.delete(reply);
        }
        discussionMapper.deleteByPrimaryKey(did);
        return null;
    }

    /**
     * 进行回复
     * @param dto 含有参数content和answerId answerId为回复特定的回复的回复，可为空
     * @param did
     * @param user
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public Object CreateReply(DiscussionCreateDTO dto, Integer did,User user) {
        Reply reply = new Reply();
        reply.setAnswer(dto.getContent());
        reply.setCreate_time(new Date());
        reply.setDid(did);
        reply.setUid(user.getUid());
        if(dto.getReplyId() != null){
            reply.setReply_id(dto.getReplyId());
        }
        replyMapper.insert(reply);
        return null;
    }

    /**
     * 获取讨论详情
     * @param did
     * @param user
     * @return
     */
    public Object GetDiscussion(Integer did, User user) {
        DiscussionVO discussionVO = new DiscussionVO();
        Discussion discussion = discussionMapper.selectByPrimaryKey(did);
        BeanUtils.copyProperties(discussion,discussionVO);

        Reply findReply = new Reply();
        findReply.setDid(did);
        List<Reply> replyList = replyMapper.select(findReply);
        List<ReplyVO> replyVOList = new ArrayList<>();
        for (Reply reply:replyList){
            ReplyVO replyVO = new ReplyVO();
            BeanUtils.copyProperties(reply,replyVO);
            replyVO.setUsername(userMapper.selectByPrimaryKey(reply.getUid()).getUsername());
            replyVOList.add(replyVO);
        }
        discussionVO.setReplyVOList(replyVOList);
        return discussionVO;
    }

    /**
     * 删除评论
     * @param replyId
     * @param user 创建者或者老师token
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public Object DeleteReply(Integer replyId, User user) {
        Reply reply = replyMapper.selectByPrimaryKey(replyId);
        if(reply == null){
            throw new CommonException("评论不存在");
        }
        if(user.getType() != 2){
            if(reply.getUid() != user.getUid()){
                throw new CommonException("权限不足");
            }
        }

        replyMapper.deleteByPrimaryKey(replyId);
        return null;
    }
}
