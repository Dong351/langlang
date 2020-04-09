package com.doublefish.langlang.mapper;

import com.doublefish.langlang.pojo.entity.UserClass;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.BaseMapper;

@Repository
public interface UserClassMapper extends BaseMapper<UserClass> {

    @Select("SELECT COUNT(*) FROM user_class WHERE cid=#{cid}")
    public Integer GetClassNumber(Integer cid);
}
