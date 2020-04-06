package com.doublefish.langlang.service;

import com.doublefish.langlang.exception.CommonException;
import com.doublefish.langlang.mapper.ClassMapper;
import com.doublefish.langlang.mapper.UserMapper;
import com.doublefish.langlang.pojo.VO.ClassInfoVO;
import com.doublefish.langlang.pojo.entity.Class;
import com.doublefish.langlang.pojo.entity.User;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Service
public class ClassService {
    @Autowired
    ClassMapper classMapper;
    @Autowired
    UserMapper userMapper;

    public ClassInfoVO getClassInfoByClass(Class find){
        Class aClass = classMapper.selectOne(find);
        ClassInfoVO classInfoVO = new ClassInfoVO();
        BeanUtils.copyProperties(aClass,classInfoVO);

        User user = userMapper.selectByPrimaryKey(find.getUid());
        classInfoVO.setCreatePeople(user.getUsername());

        return classInfoVO;
    }

    @Transactional(rollbackFor = Exception.class)
    public Object CreateClass(String name, User user) {
        if(user.getType() != 2){
            throw new CommonException("您不是老师，无法创建课程");
        }
        if(name.equals(null)){
            throw new CommonException("班级名不能为空");
        }
        Class aClass = new Class();
        aClass.setName(name);
        aClass.setUid(user.getUid());
        aClass.setNumber(1);
        Date nowDate = new Date();
        aClass.setCreateTime(nowDate);
        classMapper.insert(aClass);

        ClassInfoVO classInfoVO = new ClassInfoVO();
        BeanUtils.copyProperties(aClass,classInfoVO);
        classInfoVO.setCreatePeople(user.getUsername());

        return classInfoVO;
    }
}
