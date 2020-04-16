package com.doublefish.langlang.service;

import com.doublefish.langlang.exception.CommonException;
import com.doublefish.langlang.mapper.CourseWorkMapper;
import com.doublefish.langlang.mapper.SubjectMapper;
import com.doublefish.langlang.pojo.VO.CourseWorkVO;
import com.doublefish.langlang.pojo.entity.CourseWork;
import com.doublefish.langlang.pojo.entity.Subject;
import com.doublefish.langlang.pojo.entity.User;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class CourseWorkService {
    @Autowired
    CourseWorkMapper courseWorkMapper;
    @Autowired
    SubjectMapper subjectMapper;


    //插入coursework表
    @Transactional(rollbackFor = Exception.class)
    public void Insert(Integer subjectId, String introduction, String name, int length, String start_time, String end_time) throws ParseException {
        //判断作业名是否重复
        CourseWork find = new CourseWork();
        find.setName(name);
        CourseWork result = courseWorkMapper.selectOne(find);
        if(result != null){
            throw new CommonException("该作业已存在（作业名重复）");
        }

        CourseWork courseWork = new CourseWork();
        courseWork.setIntroduction(introduction);
        courseWork.setSid(subjectId);
        courseWork.setName(name);
        courseWork.setImg_number(length);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        courseWork.setStart_time(sdf.parse(start_time));
        courseWork.setEnd_time(sdf.parse(end_time));

        courseWorkMapper.insert(courseWork);
    }

    public Object GetCourseWorks(Integer sid, User user) {

        CourseWork find = new CourseWork();
        find.setSid(sid);
        List<CourseWork> courseWorks = courseWorkMapper.select(find);
        List<CourseWorkVO> courseWorkVOList = new ArrayList<>();
        for(CourseWork courseWork:courseWorks){
            courseWork.setIntroduction(null);

            CourseWorkVO courseWorkVO = new CourseWorkVO();
            BeanUtils.copyProperties(courseWork,courseWorkVO);
            courseWorkVO.setState(0);
            if(courseWork.getEnd_time().before(new Date())){
                courseWorkVO.setState(1);
            }
            courseWorkVOList.add(courseWorkVO);
        }
        return courseWorkVOList;
    }

    public Object GetCourseWork(Integer cwid, User user) {
        CourseWork courseWork = courseWorkMapper.selectByPrimaryKey(cwid);
        CourseWorkVO courseWorkVO = new CourseWorkVO();
        BeanUtils.copyProperties(courseWork,courseWorkVO);
        courseWorkVO.setState(0);
        if(courseWork.getEnd_time().before(new Date())){
            courseWorkVO.setState(1);
        }
        return courseWorkVO;
    }

    @Transactional(rollbackFor = Exception.class)
    public Object DeleteCourseWork(Integer cwid, User user) {
        //判断执行者是否为创建者
        CourseWork courseWork = courseWorkMapper.selectByPrimaryKey(cwid);
        Subject subject = subjectMapper.selectByPrimaryKey(courseWork.getSid());
        if(user.getUid() != subject.getUid()){
            throw new CommonException("您没有权限删除该作业");
        }

        courseWorkMapper.deleteByPrimaryKey(cwid);
        return null;
    }
}
