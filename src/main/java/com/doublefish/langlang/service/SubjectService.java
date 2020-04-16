package com.doublefish.langlang.service;

import com.doublefish.langlang.exception.CommonException;
import com.doublefish.langlang.mapper.CourseWorkMapper;
import com.doublefish.langlang.mapper.SubjectMapper;
import com.doublefish.langlang.mapper.UserClassMapper;
import com.doublefish.langlang.mapper.UserMapper;
import com.doublefish.langlang.pojo.DTO.SubjectCreateDTO;
import com.doublefish.langlang.pojo.VO.SubjectVO;
import com.doublefish.langlang.pojo.entity.CourseWork;
import com.doublefish.langlang.pojo.entity.Subject;
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
public class SubjectService {

    @Autowired
    UserMapper userMapper;
    @Autowired
    SubjectMapper subjectMapper;
    @Autowired
    UserClassMapper userClassMapper;
    @Autowired
    CourseWorkMapper courseWorkMapper;

    public Object CreateSubject(SubjectCreateDTO dto, User user) {
        if(user.getType() != 2){
            throw new CommonException("您不是老师无法创建课程");
        }
        Subject subject = new Subject();
        BeanUtils.copyProperties(dto,subject);
        Date nowTime = new Date();
        subject.setCreateTime(nowTime);
        subject.setUid(user.getUid());
        subjectMapper.insert(subject);

        SubjectVO subjectVO = new SubjectVO();
        BeanUtils.copyProperties(subject,subjectVO);
        subjectVO.setCreatePeople(user.getUsername());
        subject.setCreateTime(null);
        subjectVO.setId(subjectMapper.selectOne(subject).getId());

        return subjectVO;
    }

    @Transactional(rollbackFor = Exception.class)
    public Object DeleteSubject(Integer sid, User user) {
        Subject subject = subjectMapper.selectByPrimaryKey(sid);
        if(user.getUid() != subject.getUid()){
            throw new CommonException("您不是课程创建人，无法删除课程");
        }
        subjectMapper.deleteByPrimaryKey(sid);

        //删除该课程的所有作业
        CourseWork find = new CourseWork();
        find.setSid(sid);
        List<CourseWork> select = courseWorkMapper.select(find);
        for(CourseWork courseWork:select){
            courseWorkMapper.delete(courseWork);
        }
        return null;
    }

    //获取当前班级的课程
    public Object getSubjectList(Integer cid, User user) {
        //判断user是否在该班级里
        UserClass find = new UserClass();
        find.setUid(user.getUid());
        find.setCid(cid);
        UserClass userClass = userClassMapper.selectOne(find);
        if(userClass == null){
            throw new CommonException("您不在该班级中");
        }

        //封装subjeVOList
        Subject findSubject = new Subject();
        findSubject.setCid(cid);
        List<Subject> subjects = subjectMapper.select(findSubject);
        List<SubjectVO> subjectVOList = new ArrayList<>();
        for(Subject subject:subjects){
            SubjectVO subjectVO = new SubjectVO();
            BeanUtils.copyProperties(subject,subjectVO);
            subjectVO.setCreatePeople(userMapper.selectByPrimaryKey(subject.getUid()).getUsername());
            subjectVOList.add(subjectVO);
        }
        return subjectVOList;
    }
}
