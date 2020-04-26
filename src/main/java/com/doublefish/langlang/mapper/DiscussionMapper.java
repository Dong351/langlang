package com.doublefish.langlang.mapper;

import com.doublefish.langlang.pojo.entity.Discussion;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.BaseMapper;

@Repository
public interface DiscussionMapper extends BaseMapper<Discussion> {
}
