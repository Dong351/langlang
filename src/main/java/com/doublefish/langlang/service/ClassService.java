package com.doublefish.langlang.service;

import com.doublefish.langlang.exception.CommonException;
import com.doublefish.langlang.mapper.ClassMapper;
import com.doublefish.langlang.mapper.UserClassMapper;
import com.doublefish.langlang.mapper.UserMapper;
import com.doublefish.langlang.pojo.VO.ClassInfoVO;
import com.doublefish.langlang.pojo.VO.UserInfoVO;
import com.doublefish.langlang.pojo.entity.Class;
import com.doublefish.langlang.pojo.entity.User;
import com.doublefish.langlang.pojo.entity.UserClass;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

@Service
public class ClassService {
    @Autowired
    ClassMapper classMapper;
    @Autowired
    UserMapper userMapper;
    @Autowired
    Random random;
    @Autowired
    UserClassMapper userClassMapper;

    private String generateCode(Integer codeSize) {

        StringBuilder code = new StringBuilder();
        Class aClass = new Class();
        do {
            for (int i = 0; i < codeSize; i++) {
                code.append(random.nextInt(10));
            }
            String s = code.toString();
            aClass.setCode(s);
        }while (classMapper.selectOne(aClass) != null);

        return code.toString();
    }

    public ClassInfoVO getClassInfoByClass(Class find){
        Class aClass = classMapper.selectOne(find);
        ClassInfoVO classInfoVO = new ClassInfoVO();
        BeanUtils.copyProperties(aClass,classInfoVO);

        User user = userMapper.selectByPrimaryKey(find.getUid());
        classInfoVO.setCreatePeople(user.getUsername());

        return classInfoVO;
    }

//    @Transactional(rollbackFor = Exception.class)
    public Object CreateClass(String name, User user) {
        if(user.getType() != 2){
            throw new CommonException("您不是老师，无法创建课程");
        }
        if(name.equals(null)){
            throw new CommonException("班级名不能为空");
        }
        //插入class
        Class aClass = new Class();
        aClass.setName(name);
        aClass.setUid(user.getUid());
        aClass.setNumber(1);
        Date nowDate = new Date();
        aClass.setCreateTime(nowDate);
        String code = generateCode(8);
        aClass.setCode(code);
        classMapper.insert(aClass);

        //封装VO
        ClassInfoVO classInfoVO = new ClassInfoVO();
        BeanUtils.copyProperties(aClass,classInfoVO);
        classInfoVO.setCreatePeople(user.getUsername());
        aClass.setCreateTime(null);
        Integer cid = classMapper.selectOne(aClass).getId();
        classInfoVO.setId(cid);

        //插入user_class表
        UserClass userClass = new UserClass();
        userClass.setCid(cid);
        userClass.setUid(user.getUid());
        userClassMapper.insert(userClass);

        return classInfoVO;
    }

    //输入验证码加入班级
    @Transactional(rollbackFor = Exception.class)
    public Object JoinClass(String code, User user) {
        Class find = new Class();
        find.setCode(code);
        Class aClass = classMapper.selectOne(find);
        //判断邀请码存在
        if(aClass == null){
            throw new CommonException("邀请码不存在");
        }

        //判断是否已在表中
        UserClass findUserClass = new UserClass();
        findUserClass.setUid(user.getUid());
        findUserClass.setCid(aClass.getId());
        if(userClassMapper.selectOne(findUserClass) != null){
            throw new CommonException("您已在班级中");
        }

        //插入user_class表
        UserClass userClass = new UserClass();
        userClass.setUid(user.getUid());
        userClass.setCid(aClass.getId());
        userClassMapper.insert(userClass);

        //班级人数更新
        aClass.setNumber(userClassMapper.GetClassNumber(aClass.getId()));
        classMapper.updateByPrimaryKeySelective(aClass);

        return null;
    }

    //获取自己参加的班级
    public Object GetClass(User user) {
        UserClass find = new UserClass();
        find.setUid(user.getUid());
        List<UserClass> select = userClassMapper.select(find);

        List<ClassInfoVO> classList = new ArrayList<>();
        for (UserClass userClass:select){
            ClassInfoVO classInfoVO = new ClassInfoVO();
            Class aClass = classMapper.selectByPrimaryKey(userClass.getCid());
            BeanUtils.copyProperties(aClass,classInfoVO);
            classInfoVO.setCreatePeople(userMapper.selectByPrimaryKey(aClass.getUid()).getUsername());
            classInfoVO.setCode(null);
            classList.add(classInfoVO);
        }

        return classList;
    }

    //获取指定cid的班级详情
    public ClassInfoVO GetClassInfo(Integer cid) {
        //查找class表
        Class find = new Class();
        find.setId(cid);
        Class aClass = classMapper.selectOne(find);

        //封装VO
        ClassInfoVO classInfoVO = new ClassInfoVO();
        BeanUtils.copyProperties(aClass,classInfoVO);
        classInfoVO.setCreatePeople(userMapper.selectByPrimaryKey(aClass.getUid()).getUsername());

        //封装班级成员
        UserClass finUserClass = new UserClass();
        finUserClass.setCid(aClass.getId());
        List<UserClass> select = userClassMapper.select(finUserClass);
        List<UserInfoVO> members = new ArrayList<>();
        for(UserClass userClass:select){
            User user = userMapper.selectByPrimaryKey(userClass.getUid());
            UserInfoVO userInfoVO = new UserInfoVO();
            BeanUtils.copyProperties(user,userInfoVO);
            userInfoVO.setAccesstoken(null);
            if(user.getType() == 1){
                userInfoVO.setTypeName("学生");
            }
            else if(user.getType() == 2){
                userInfoVO.setTypeName("老师");
            }
            members.add(userInfoVO);
        }
        classInfoVO.setMember(members);
        return classInfoVO;
    }

    public Object GetCode(Integer classId) {
        Class aClass = classMapper.selectByPrimaryKey(classId);
        return aClass.getCode();
    }

    //踢出班级
    @ Transactional(rollbackFor = Exception.class)
    public Object DeleteMember(Integer cid, Integer uid, User user) {
        Class aClass = classMapper.selectByPrimaryKey(cid);
        if(aClass.getUid() != user.getUid()){
            throw new CommonException("您不是班主任");
        }

        UserClass findUserClass = new UserClass();
        findUserClass.setCid(cid);
        findUserClass.setUid(uid);
        UserClass userClass = userClassMapper.selectOne(findUserClass);

        if(userClass == null){
            throw new CommonException("该同学不在班上");
        }
        userClassMapper.delete(userClass);
        aClass.setNumber(userClassMapper.GetClassNumber(aClass.getId()));
        classMapper.updateByPrimaryKeySelective(aClass);
        return null;
    }
}
