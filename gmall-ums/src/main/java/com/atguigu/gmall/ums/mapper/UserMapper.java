package com.atguigu.gmall.ums.mapper;

import com.atguigu.gmall.ums.entity.UserEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 用户表
 * 
 * @author lmx
 * @email lmx@atguigu.com
 * @date 2020-10-28 16:51:38
 */
@Mapper
public interface UserMapper extends BaseMapper<UserEntity> {
	
}
