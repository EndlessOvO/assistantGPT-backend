package com.endlessovo.assistantGPT.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.endlessovo.assistantGPT.model.entity.User;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper extends BaseMapper<User> {
}
