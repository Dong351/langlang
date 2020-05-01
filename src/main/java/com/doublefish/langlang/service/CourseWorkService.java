package com.doublefish.langlang.service;

import com.doublefish.langlang.bean.VO.CorrectVO;
import com.doublefish.langlang.bean.VO.SelectFillVO;
import com.doublefish.langlang.exception.CommonException;
import com.doublefish.langlang.mapper.*;
import com.doublefish.langlang.pojo.DTO.CorrectDTO;
import com.doublefish.langlang.pojo.DTO.SelectFillCreateDTO;
import com.doublefish.langlang.pojo.VO.CourseWorkVO;
import com.doublefish.langlang.pojo.VO.UserInfoVO;
import com.doublefish.langlang.pojo.entity.Class;
import com.doublefish.langlang.pojo.entity.*;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class CourseWorkService {
    @Autowired
    CourseWorkMapper courseWorkMapper;
    @Autowired
    StudentWorkMapper studentWorkMapper;
    @Autowired
    SubjectMapper subjectMapper;
    @Autowired
    UserClassMapper userClassMapper;
    @Autowired
    UserMapper userMapper;
    @Autowired
    ClassMapper classMapper;
    @Autowired
    SelectFillMapper selectFillMapper;


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

            StudentWork findStudentWork = new StudentWork();
            findStudentWork.setCwid(courseWork.getId());
            findStudentWork.setUid(user.getUid());
            StudentWork studentWork = studentWorkMapper.selectOne(findStudentWork);
            courseWorkVO.setScore("未提交");
            if(studentWork != null){
                if(studentWork.getScore() == null){
                    courseWorkVO.setScore("尚未批改");
                }
                else {
                    courseWorkVO.setScore(studentWork.getScore().toString());
                }
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
        if(courseWorkVO.getIntroduction().equals("select&&fill")){
            courseWorkVO.setIntroduction(null);
            SelectFill findSelectFill = new SelectFill();
            findSelectFill.setCwId(cwid);
            List<SelectFill> selectFillList = selectFillMapper.select(findSelectFill);
            for(SelectFill selectFill:selectFillList){
                selectFill.setAnswer(null);
                selectFill.setCwId(null);
            }
            courseWorkVO.setSelectFillList(selectFillList);
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

        //删除student_work表
        StudentWork find = new StudentWork();
        find.setCwid(cwid);
        List<StudentWork> studentWorks = studentWorkMapper.select(find);
        for(StudentWork studentWork:studentWorks){
            studentWorkMapper.delete(studentWork);
        }

        //删除select_fill表
        SelectFill findSelectFill = new SelectFill();
        findSelectFill.setCwId(cwid);
        List<SelectFill> selectFills = selectFillMapper.select(findSelectFill);
        for(SelectFill selectFill:selectFills){
            selectFillMapper.delete(selectFill);
        }
        courseWorkMapper.deleteByPrimaryKey(cwid);
        return null;
    }

    //插入student_work表
    public void InsertStudent(Integer cwid, String introduction, User user, int length) {
        StudentWork studentWork = new StudentWork();
        studentWork.setCwid(cwid);
        studentWork.setUid(user.getUid());
        StudentWork selectOne = studentWorkMapper.selectOne(studentWork);

        studentWork.setIntroduction(introduction);
        studentWork.setImg_number(length);
        studentWork.setUpload_time(new Date());

        if(selectOne == null) {
            studentWorkMapper.insert(studentWork);
        }
        else {
            studentWorkMapper.updateByPrimaryKeySelective(studentWork);
        }
    }

    /**
     * 更新student_work
     * @param cwid
     * @param introduction
     * @param user
     * @param length
     */
//    public void UpdateWork(Integer cwid, String introduction, User user, int length) {
//        //判断学生作业是否存在
//        StudentWork find = new StudentWork();
//        studentWorkMapper.selectOne(find);
//        if()
//    }

    /**
     * 获取cwid作业的所有学生
     * @param cwid 作业id
     * @param user 操作者user，权限老师
     * @return 学生vo集合
     */
    public Object GetStudentWorks(Integer cwid, User user) {
        //检查token
        if(user.getType() != 2){
            throw new CommonException("您不是教师，无权限查看作业提交情况");
        }

        //获取班级学生名单
        Integer sid = courseWorkMapper.selectByPrimaryKey(cwid).getSid();
        Integer cid = subjectMapper.selectByPrimaryKey(sid).getCid();
        UserClass findUserClass = new UserClass();
        findUserClass.setCid(cid);
        List<UserClass> select = userClassMapper.select(findUserClass);
        List<UserInfoVO> userInfoVOList = new ArrayList<>();
        for(UserClass userClass:select){
            User result = userMapper.selectByPrimaryKey(userClass.getUid());
            if(result.getType() == 2){
                continue;
            }
            UserInfoVO userInfoVO = new UserInfoVO();
            userInfoVO.setUsername(result.getUsername());
            userInfoVO.setUid(result.getUid());
            userInfoVO.setState(0);

            //判断user是否提交作业,state=0,1,2分别代表为提交，未修改，已修改
            StudentWork findStudentWork = new StudentWork();
            findStudentWork.setUid(result.getUid());
            findStudentWork.setCwid(cwid);
            StudentWork studentWork = studentWorkMapper.selectOne(findStudentWork);
            if(studentWork != null){
                userInfoVO.setUpload_time(studentWork.getUpload_time());
                Integer state = studentWork.getScore()==null?1:2;
                userInfoVO.setState(state);
                userInfoVO.setScore(studentWork.getScore());
            }

            userInfoVOList.add(userInfoVO);
        }
        return userInfoVOList;
    }

    /**
     * 获取cwid作业中指定uid用户的作业详情
     * @param cwid
     * @param uid
     * @param user
     * @return student_workInfoVO
     */
    public Object GetStudentWork(Integer cwid, Integer uid, User user) {
        Integer sid = courseWorkMapper.selectByPrimaryKey(cwid).getSid();
        Integer cid = subjectMapper.selectByPrimaryKey(sid).getCid();
        Class aClass = classMapper.selectByPrimaryKey(cid);
        if(aClass.getUid() != user.getUid()){
            throw new CommonException("权限不足");
        }

        StudentWork find = new StudentWork();
        find.setCwid(cwid);
        find.setUid(uid);
        StudentWork studentWork = studentWorkMapper.selectOne(find);
        return studentWork;
    }

    public Object CorrectWork(Integer cwid, Integer uid, User user, CorrectDTO dto) {
        Integer sid = courseWorkMapper.selectByPrimaryKey(cwid).getSid();
        Integer cid = subjectMapper.selectByPrimaryKey(sid).getCid();
        Class aClass = classMapper.selectByPrimaryKey(cid);
        if(aClass.getUid() != user.getUid()){
            throw new CommonException("权限不足");
        }

        StudentWork find = new StudentWork();
        find.setCwid(cwid);
        find.setUid(uid);
        StudentWork studentWork = studentWorkMapper.selectOne(find);
        studentWork.setScore(dto.getScore());
        studentWork.setEvaluation(dto.getEvaluation());
        studentWorkMapper.updateByPrimaryKeySelective(studentWork);
        return null;
    }


    public Object GetCorrectInfo(Integer cwid, User user) {

        StudentWork findStudentWork = new StudentWork();
        findStudentWork.setUid(user.getUid());
        findStudentWork.setCwid(cwid);
        StudentWork studentWork = studentWorkMapper.selectOne(findStudentWork);
        if(studentWork == null){
//            throw new CommonException("尚未提交作业！");
            return "尚未提交作业!";
        }

        else if(studentWork.getScore() == null){
//            throw new CommonException("未批改");
            return "未批改!";
        }

        List<SelectFillVO> selectFillVOS = new ArrayList<>();
        if(courseWorkMapper.selectByPrimaryKey(cwid).getIntroduction().equals("select&&fill")){
            SelectFill findSelectFill = new SelectFill();
            findSelectFill.setCwId(cwid);
            List<SelectFill> selectFillList = selectFillMapper.select(findSelectFill);
            String introduction = studentWork.getIntroduction();
            String[] split = introduction.split("-");
            System.out.println(split);
            int i = 0;
            for(SelectFill selectFill:selectFillList){
                SelectFillVO selectFillVO = new SelectFillVO();
                BeanUtils.copyProperties(selectFill,selectFillVO);
                selectFillVO.setStudent_answer(split[i]);
                selectFillVO.setCwId(null);
                if(selectFill.getAnswer().equals(split[i])){
                    selectFillVO.setStudent_score(selectFill.getScore());
                }
                else{
                    selectFillVO.setStudent_score(0);
                }
                selectFillVOS.add(selectFillVO);
                i++;
            }
        }

        CorrectVO correctVO = new CorrectVO();
        BeanUtils.copyProperties(studentWork,correctVO);
        correctVO.setSelectFillVOList(selectFillVOS);
        return correctVO;
    }

    /**
     * 创建选择填空式作业
     * @param subjectId
     * @param user 课程创建者
     * @param dto
     * @return
     * @throws ParseException
     */
    @Transactional(rollbackFor = Exception.class)
    public Object CreateSelectFill(Integer subjectId, User user, SelectFillCreateDTO dto) throws ParseException {
        if(subjectMapper.selectByPrimaryKey(subjectId).getUid() != user.getUid()){
            throw new CommonException("权限不足");
        }

        CourseWork courseWork = new CourseWork();
        BeanUtils.copyProperties(dto,courseWork);
        //日期格式转换
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        courseWork.setStart_time(sdf.parse(dto.getStart_time()));
        courseWork.setEnd_time(sdf.parse(dto.getEnd_time()));
        courseWork.setIntroduction("select&&fill");
        courseWork.setImg_number(0);
        courseWork.setSid(subjectId);
        courseWorkMapper.insertWork(courseWork);
//        System.out.println(courseWork.getId());

        //讲dto里的选择填空数据插入select_fill表
        List<SelectFill> selectFillList = dto.getSelectFillList();
        for(SelectFill selectFill:selectFillList){
            selectFill.setCwId(courseWork.getId());
            selectFillMapper.insert(selectFill);
        }
        return null;
    }

    /**
     * 学生提交选择填空作业，并对各题打分
     * @param courseworkId
     * @param user
     * @param answers
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public Object SubmitSelectFill(Integer courseworkId, User user, Map<Integer, String> answers) {
        Integer score = 0;
        String evaluation = "本题为系统评分"+"\n";
        String introduction = "";
//        int i = 1;
        for(Map.Entry<Integer,String> entry:answers.entrySet()){
            Integer id = entry.getKey();
            String answer = entry.getValue();
            introduction = introduction + answer + "-";
            SelectFill selectFill = selectFillMapper.selectByPrimaryKey(id);
            Integer GetScore = 0;
            if(selectFill.getAnswer().equals(answer)){
                GetScore = selectFill.getScore();
                score += GetScore;
            }
//            evaluation = evaluation+i+". "+GetScore+"\n";
//            i++;
        }
        StudentWork studentWork = new StudentWork();
        studentWork.setUid(user.getUid());
        studentWork.setCwid(courseworkId);
        if(studentWorkMapper.selectOne(studentWork) != null){
            throw new CommonException("你已提交过作业!");
        }
        studentWork.setUpload_time(new Date());
        studentWork.setImg_number(0);
        studentWork.setScore(Float.valueOf(score));
        studentWork.setIntroduction(introduction);
        studentWork.setEvaluation(evaluation);

        studentWorkMapper.insert(studentWork);
        return null;
    }
}
