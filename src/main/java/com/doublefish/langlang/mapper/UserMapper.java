package com.doublefish.langlang.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.doublefish.langlang.bean.User;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

@Repository
public interface UserMapper extends BaseMapper<User> {
    @Select("select * from user  where username=#{username}")
    public User login(User user);

    @Select("select * from user where username=#{username}")
    public User selectByName(String name);
}
