package com.doublefish.langlang.service;

import com.doublefish.langlang.exception.CommonException;
import com.doublefish.langlang.mapper.LiveMapper;
import com.doublefish.langlang.mapper.SubjectMapper;
import com.doublefish.langlang.pojo.entity.Live;
import com.doublefish.langlang.pojo.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Service
public class LiveService {
    @Autowired
    LiveMapper liveMapper;
    @Autowired
    SubjectMapper subjectMapper;

    /**
     * 发起直播，前端传入url保存至数据库
     * @param url
     * @param sid
     * @param user 课程创建者token
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public Object CreateLive(String url, Integer sid, User user) {
        if(subjectMapper.selectByPrimaryKey(sid).getUid() != user.getUid()){
            throw new CommonException("您不是课程创建者，无权限直播");
        }

        Live live = new Live();
        live.setSid(sid);
        live.setUrl(url);
        live.setCreate_time(new Date());

        liveMapper.insert(live);
        return null;
    }

    /**
     * 用户进入直播间，返回url
     * @param sid
     * @param user
     * @return
     */
    public Object JoinLive(Integer sid, User user) {
        Live findLive = new Live();
        findLive.setSid(sid);
        Live live = liveMapper.selectOne(findLive);
        if(live == null){
            throw new CommonException("老师尚未开始直播");
        }
        return live.getUrl();
    }

    /**
     * 老师关闭直播间，删除数据库中url
     * @param sid
     * @param user 课程创建者
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public Object CloseLive(Integer sid, User user) {
        if(subjectMapper.selectByPrimaryKey(sid).getUid() != user.getUid()){
            throw new CommonException("您不是课程创建者，无权限");
        }

        Live findLive = new Live();
        findLive.setSid(sid);
        liveMapper.delete(findLive);
        return null;
    }
}
