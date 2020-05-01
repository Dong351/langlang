package com.doublefish.langlang.mapper;

import com.doublefish.langlang.pojo.entity.CourseWork;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Options;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.BaseMapper;

@Repository
public interface CourseWorkMapper extends BaseMapper<CourseWork> {

    @Insert("insert into coursework(name,introduction,img_number,start_time,end_time,sid) values(#{name},#{introduction},#{img_number},#{start_time},#{end_time},#{sid})")
    @Options(useGeneratedKeys = true,keyProperty = "id")
    int insertWork(CourseWork courseWork);
}
