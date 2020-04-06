package com.doublefish.langlang.mapper;

import com.doublefish.langlang.pojo.entity.ResetMailCode;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.BaseMapper;


@Repository
public interface ResetMailCodeMapper extends BaseMapper<ResetMailCode> {

    @Delete("DELETE FROM reset_mail_code where ((UNIX_TIMESTAMP(NOW())-UNIX_TIMESTAMP(time))/60) > 5")
    void deleteOutOfTime();

    @Select("SELECT * FROM reset_mail_code where email = #{email}")
     ResetMailCode findByEmail(String email);
}
