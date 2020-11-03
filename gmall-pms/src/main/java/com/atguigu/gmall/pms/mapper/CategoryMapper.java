package com.atguigu.gmall.pms.mapper;

import com.atguigu.gmall.pms.entity.CategoryEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 商品三级分类
 * 
 * @author lmx
 * @email lmx@atguigu.com
 * @date 2020-10-28 12:30:25
 */
@Mapper
public interface CategoryMapper extends BaseMapper<CategoryEntity> {
	
}
