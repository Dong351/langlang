package com.doublefish.langlang.mapper;

import com.doublefish.langlang.pojo.entity.Subject;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.BaseMapper;

@Repository
public interface SubjectMapper extends BaseMapper<Subject> {
}
