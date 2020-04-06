package com.doublefish.langlang.mapper;

import com.doublefish.langlang.pojo.entity.Student;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.BaseMapper;

@Repository
public interface StudentMapper extends BaseMapper<Student> {
}
