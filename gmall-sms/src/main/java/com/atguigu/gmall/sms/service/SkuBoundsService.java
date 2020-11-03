package com.atguigu.gmall.sms.service;

import vo.SkuSaleVo;
import com.baomidou.mybatisplus.extension.service.IService;
import com.atguigu.gmall.common.bean.PageResultVo;
import com.atguigu.gmall.common.bean.PageParamVo;
import com.atguigu.gmall.sms.entity.SkuBoundsEntity;

/**
 * 商品spu积分设置
 *
 * @author lmx
 * @email lmx@atguigu.com
 * @date 2020-10-28 18:33:13
 */
public interface SkuBoundsService extends IService<SkuBoundsEntity> {

    PageResultVo queryPage(PageParamVo paramVo);

    void saveSkuInfo(SkuSaleVo skuSaleVo);
}

