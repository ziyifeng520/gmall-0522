package com.atguigu.gmall.oms.mapper;

import com.atguigu.gmall.oms.entity.OrderEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 订单
 * 
 * @author lmx
 * @email lmx@atguigu.com
 * @date 2020-10-28 17:59:04
 */
@Mapper
public interface OrderMapper extends BaseMapper<OrderEntity> {
	
}
