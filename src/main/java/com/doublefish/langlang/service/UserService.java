package com.doublefish.langlang.service;

import com.doublefish.langlang.exception.CommonException;
import com.doublefish.langlang.mapper.ResetMailCodeMapper;
import com.doublefish.langlang.mapper.StudentMapper;
import com.doublefish.langlang.mapper.TeacherMapper;
import com.doublefish.langlang.mapper.UserMapper;
import com.doublefish.langlang.pojo.DTO.*;
import com.doublefish.langlang.pojo.VO.UserInfoVO;
import com.doublefish.langlang.pojo.entity.ResetMailCode;
import com.doublefish.langlang.pojo.entity.Student;
import com.doublefish.langlang.pojo.entity.Teacher;
import com.doublefish.langlang.pojo.entity.User;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.Random;

@Service
public class UserService {

    Random random = new Random();
    @Autowired
    UserMapper userMapper;
    @Autowired
    StudentMapper studentMapper;
    @Autowired
    TeacherMapper teacherMapper;
    @Autowired
    ResetMailCodeMapper resetMailCodeMapper;



    public boolean userExistsByPhone(String phone) {
        User exampleUser = new User();
        exampleUser.setPhone(phone);

        if (userMapper.selectOne(exampleUser) != null) {
            return true;
        }
        else return false;
    }

    private boolean userExistsByEMail(String Email){
        User exampleUser = new User();
        exampleUser.setEmail(Email);
        if (userMapper.selectOne(exampleUser) != null) {
            return true;
        }
        else return false;
    }

    public void updateToken(User user) {

        User exampleUser = new User();

        String accessToken;
        // 必须保证token的唯一性
        do {
            accessToken = generateToken(user.getUid(), System.currentTimeMillis());
            exampleUser.setAccesstoken(accessToken);
        } while (userMapper.selectOne(exampleUser) != null);

        exampleUser.setUid(user.getUid());
        userMapper.updateByPrimaryKeySelective(exampleUser);
        user.setAccesstoken(accessToken);
    }

    private String generateToken(Integer uid, long time) {
        return DigestUtils.md5Hex(uid + "-" + time);
    }

    private String encryptPassword(String source) {
        return DigestUtils.sha256Hex(source);
    }

    public User queryUserByToken(String token) {
        User exampleUser = new User();
        exampleUser.setAccesstoken(token);
        return userMapper.selectOne(exampleUser);
    }

    public UserInfoVO findUserInfoByUid(Integer uid) {

        //在user表中寻找
        User user = userMapper.selectByPrimaryKey(uid);

        //在相应身份类型中寻找
        UserInfoVO userInfoVO = new UserInfoVO();
        if(user.getType() == 1){
            Student student = studentMapper.selectByPrimaryKey(user.getUid());
            BeanUtils.copyProperties(student,userInfoVO);
            userInfoVO.setTypeName("学生");
        }
        else if(user.getType() == 2){
            Teacher teacher = new Teacher();
            BeanUtils.copyProperties(teacher,userInfoVO);
            userInfoVO.setTypeName("老师");
        }
        BeanUtils.copyProperties(user,userInfoVO);

        userInfoVO.setUserType(user.getType());
        return userInfoVO;
    }

    private UserInfoVO getUserInfoVO(User example){

        User user = userMapper.selectOne(example);
        UserInfoVO userInfoVO = new UserInfoVO();
        BeanUtils.copyProperties(user,userInfoVO);

        //获得用户类型
        if(user.getType() == 1){
            userInfoVO.setTypeName("学生");
            userInfoVO.setUserType(1);
            Student student = studentMapper.selectByPrimaryKey(example.getUid());
            BeanUtils.copyProperties(student,userInfoVO);
        }
        if(user.getType() == 2){
            userInfoVO.setTypeName("老师");
            userInfoVO.setUserType(2);
            Teacher teacher = teacherMapper.selectByPrimaryKey(example.getUid());
            BeanUtils.copyProperties(teacher,userInfoVO);
        }
        return userInfoVO;
    }

    public Object login(UserLoginDTO dto) {
// 根据手机号在数据库中查找用户
        User exampleUser = new User();
        exampleUser.setPhone(dto.getPhone());
        User user = userMapper.selectOne(exampleUser);

        //没有查到用户，则说明手机号为注册
        if(user == null){
            throw new CommonException("手机号未注册");
        }

        //比对手机登录密码
        if(!Objects.equals(user.getPassword(),encryptPassword(dto.getPassword()))){
            throw new CommonException("账号密码不正确");
        }

        //登陆成功则返回token
        updateToken(user);

        //返回用户信息
        return getUserInfoVO(user);
    }

    @Transactional(rollbackFor = Exception.class)
    public void register(UserRegisterDTO dto) {
        // 手机号必须未注册
        if(userExistsByPhone(dto.getPhone())){
            throw new CommonException("手机号已注册");
        }

        //邮箱必须未注册
        if(userExistsByEMail(dto.getEmail())){
            throw new CommonException("该邮箱已注册");
        }

        User user = new User();
        BeanUtils.copyProperties(dto,user);

        //密码加密
        user.setPassword(encryptPassword(dto.getPassword()));

        // 生成token，如果不进行这一步会出现多个用户token为空的情况，违反唯一性约束, 这样子还是会有极小的概率出现token不唯一的情况
        user.setAccesstoken(generateToken(random.nextInt(65535), System.currentTimeMillis()));
        //插入user表
        userMapper.insertSelective(user);

    }

    public void insertTypeUser(UserRegisterDTO dto){
        User find = new User();
        BeanUtils.copyProperties(dto,find);
        find.setPassword(null);
        User user = userMapper.selectOne(find);
        if(user.getType() == 1){
            Student student = new Student();
            BeanUtils.copyProperties(dto,student);
            student.setUid(user.getUid());
            studentMapper.insert(student);
        }
        //插入teacher表
        else if(user.getType() == 2){
            Teacher teacher = new Teacher();
            BeanUtils.copyProperties(dto,teacher);
            teacher.setUid(user.getUid());
            teacherMapper.insert(teacher);
        }
    }

    public UserInfoVO findUserInfoByDTO(UserRegisterDTO dto) {
        User find = new User();
        BeanUtils.copyProperties(dto,find);
        find.setPassword(null);
        User user = userMapper.selectOne(find);
        UserInfoVO userInfoVO = new UserInfoVO();
        userInfoVO = getUserInfoVO(user);
        return userInfoVO;
    }

    @Transactional(rollbackFor = Exception.class)
    public Object updateUserInfo(UserUpdateDTO dto, User user) {
        User exampleUser = new User();
        BeanUtils.copyProperties(dto, exampleUser);

        //更新user表
        exampleUser.setType(user.getType());
        exampleUser.setUid(user.getUid());
        userMapper.updateByPrimaryKeySelective(exampleUser);

        //更新type表
        if(dto.getUserType() == 1){
            Student student = new Student();
            student.setUid(user.getUid());
            student.setGrade(dto.getGrade());
            student.setSchool(dto.getSchool());
            studentMapper.updateByPrimaryKeySelective(student);
        }
        else if(dto.getUserType() == 2){
            Teacher teacher = new Teacher();
            teacher.setUid(user.getUid());
            teacher.setTeachId(dto.getTeachId());
            teacher.setSchool(dto.getSchool());
            teacherMapper.updateByPrimaryKeySelective(teacher);
        }
        return getUserInfoVO(exampleUser);
    }

    public void modifyPassword(UserModifyPasswordDTO dto, User user) {
        if(dto.getPrePassword() == null){
            throw new CommonException("原密码不能为空");
        }

        if(!Objects.equals(user.getPassword(),encryptPassword(dto.getPrePassword()))){
            throw new CommonException("原密码不正确");
        }

        //原密码正确，修改数据库
        User exampleUser = new User();
        exampleUser.setUid(user.getUid());
        exampleUser.setPassword(encryptPassword(dto.getPassword()));
        userMapper.updateByPrimaryKeySelective(exampleUser);
    }

    public void resetPassword(UserResetPasswordDTO dto) {
        ResetMailCode resetMailCode = resetMailCodeMapper.findByEmail(dto.getEmail());
        if(resetMailCode == null){
            List<User> userList = userMapper.findUserByEmail(dto.getEmail());
            if (userList.isEmpty()){
                throw new CommonException("该邮箱未注册");
            }
            else{
                throw new CommonException("验证码已过期");
            }
        }
        else if (Objects.equals(dto.getCode(),resetMailCode.getCode())){
            User user = new User();
            user.setUid(resetMailCode.getUid());
            user.setPassword(encryptPassword(dto.getPassword()));
            userMapper.updateByPrimaryKeySelective(user);
            resetMailCodeMapper.deleteByPrimaryKey(resetMailCode.getId());
        }
        else{
            throw new CommonException("验证码错误");
        }
    }
}
