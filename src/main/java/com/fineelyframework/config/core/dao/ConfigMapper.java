package com.fineelyframework.config.core.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.fineelyframework.config.core.entity.ConfigPlus;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ConfigMapper extends BaseMapper<ConfigPlus> {
}
