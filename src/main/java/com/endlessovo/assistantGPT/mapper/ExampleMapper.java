package com.endlessovo.assistantGPT.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.endlessovo.assistantGPT.model.entity.ExampleEntity;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ExampleMapper extends BaseMapper<ExampleEntity> {
}
