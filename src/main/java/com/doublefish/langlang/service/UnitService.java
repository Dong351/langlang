package com.doublefish.langlang.service;

import com.doublefish.langlang.exception.CommonException;
import com.doublefish.langlang.mapper.*;
import com.doublefish.langlang.pojo.entity.Unit;
import com.doublefish.langlang.pojo.entity.UnitFile;
import com.doublefish.langlang.pojo.entity.User;
import com.doublefish.langlang.pojo.entity.UserClass;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
public class UnitService {
    @Autowired
    UnitMapper unitMapper;
    @Autowired
    SubjectMapper subjectMapper;
    @Autowired
    ClassMapper classMapper;
    @Autowired
    UserClassMapper userClassMapper;
    @Autowired
    UnitFileMapper unitFileMapper;

    /**
     * 创建单元
     * @param sid
     * @param unitName
     * @param user 创建者token
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public Object CreateUnit(Integer sid, String unitName, User user) {
        if(user.getUid() != subjectMapper.selectByPrimaryKey(sid).getUid()){
            throw  new CommonException("您不是课程创建者，无法创建单元");
        }

        Unit unit = new Unit();
        unit.setName(unitName);
        unit.setCreate_time(new Date());
        unit.setSid(sid);
        unitMapper.insert(unit);

        return null;
    }


    /**
     * 获取课程内的所有单元
     * @param sid
     * @param user 班级成员
     * @return
     */
    public Object GetUnits(Integer sid, User user) {
        //判断user是否为该班级成员
        Integer cid = subjectMapper.selectByPrimaryKey(sid).getCid();
        UserClass findUserClass = new UserClass();
        findUserClass.setCid(cid);
        findUserClass.setUid(user.getUid());
        if(userClassMapper.selectOne(findUserClass) == null){
            throw new CommonException("您不是班级成员，无法获取该班级课程");
        }

        Unit findUnit = new Unit();
        findUnit.setSid(sid);
        List<Unit> unitList = unitMapper.select(findUnit);
        return unitList;
    }

    /**
     * 删除指定单元
     * @param unitId 单元id
     * @param user  创建者
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public Object DeleteUnit(Integer unitId, User user) {
        Integer sid = unitMapper.selectByPrimaryKey(unitId).getSid();
        if(user.getUid() != subjectMapper.selectByPrimaryKey(sid).getUid()){
            throw  new CommonException("您不是课程创建者，无法创建单元");
        }

        unitMapper.deleteByPrimaryKey(unitId);
        return null;
    }

    @Transactional(rollbackFor = Exception.class)
    public void insertFile(String fileName, Integer unitId) {
        UnitFile unitFile = new UnitFile();
        unitFile.setFile_name(fileName);
        unitFile.setUnitId(unitId);
        UnitFile result = unitFileMapper.selectOne(unitFile);
        unitFile.setCreate_time(new Date());

        if (result == null){
            unitFileMapper.insert(unitFile);
        }
        else {
            unitFileMapper.updateByPrimaryKeySelective(unitFile);
        }
    }


    /**
     * 获取单元里的文件名称
     * @param unitId
     * @param user
     * @return
     */
    public Object getFiles(Integer unitId, User user) {
        UnitFile findUnitFile = new UnitFile();
        findUnitFile.setUnitId(unitId);
        List<UnitFile> unitFileList = unitFileMapper.select(findUnitFile);
        return unitFileList;
    }

    @Transactional(rollbackFor = Exception.class)
    public void DeleteUnitFile(String fileName) {
        UnitFile find = new UnitFile();
        find.setFile_name(fileName);
        UnitFile unitFile = unitFileMapper.selectOne(find);
        unitFileMapper.delete(unitFile);
    }
}
