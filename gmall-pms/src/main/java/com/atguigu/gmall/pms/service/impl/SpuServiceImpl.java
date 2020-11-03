package com.atguigu.gmall.pms.service.impl;

import com.atguigu.gmall.pms.entity.*;
import com.atguigu.gmall.pms.feign.GmallSmsClient;
import com.atguigu.gmall.pms.mapper.SkuMapper;
import com.atguigu.gmall.pms.mapper.SpuDescMapper;
import com.atguigu.gmall.pms.service.SkuAttrValueService;
import com.atguigu.gmall.pms.service.SkuImagesService;
import com.atguigu.gmall.pms.service.SpuAttrValueService;
import com.atguigu.gmall.pms.vo.SkuVo;
import com.atguigu.gmall.pms.vo.SpuAttrValueVo;
import com.atguigu.gmall.pms.vo.SpuVo;
import io.seata.spring.annotation.GlobalTransactional;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.atguigu.gmall.common.bean.PageResultVo;
import com.atguigu.gmall.common.bean.PageParamVo;

import com.atguigu.gmall.pms.mapper.SpuMapper;
import com.atguigu.gmall.pms.service.SpuService;
import org.springframework.util.CollectionUtils;
import vo.SkuSaleVo;


@Service("spuService")
public class SpuServiceImpl extends ServiceImpl<SpuMapper, SpuEntity> implements SpuService {

   @Autowired
   private SpuDescMapper descMapper;

   @Autowired
   private SkuMapper skuMapper;

   @Autowired
   private SpuAttrValueService spuAttrValueService;

   @Autowired
   private SkuAttrValueService skuattrValueService;


   @Autowired
   private SkuImagesService skuImagesService;

   @Autowired
   private GmallSmsClient gmallSmsClient;

    @Override
    public PageResultVo queryPage(PageParamVo paramVo) {
        IPage<SpuEntity> page = this.page(
                paramVo.getPage(),
                new QueryWrapper<SpuEntity>()
        );

        return new PageResultVo(page);
    }

    @Override
    public PageResultVo querySpuInfo(PageParamVo pageParamVo, Long categoryId) {
        //封装查询条件
        QueryWrapper<SpuEntity> wrapper = new QueryWrapper<>();
        // 如果分类id不为0，要根据分类id查，否则查全部
        if(categoryId != 0){
            wrapper.eq("category_id",categoryId);
        }
        //如果用户输入了检索条件，根据检索条件查
        String key = pageParamVo.getKey();
        if(StringUtils.isNotBlank(key)){
            wrapper.and(t->t.like("name",key).or().like("id",key));
        }


        IPage<SpuEntity> page = this.page(
                pageParamVo.getPage(),
                wrapper
        );

        return new PageResultVo(page);

    }

    @GlobalTransactional
    @Override
    public void bigSave(SpuVo spu) {
        //1.保存Spu相关信息
        //1.1保存spu表
        spu.setCreateTime(new Date());
        spu.setUpdateTime(spu.getCreateTime());
        this.save(spu);
        Long spuId = spu.getId();

        //1.2保存spu_desc表
        List<String> spuImages = spu.getSpuImages();
        if(!CollectionUtils.isEmpty(spuImages)){
            SpuDescEntity spuDescEntity = new SpuDescEntity();
            spuDescEntity.setSpuId(spuId);
            spuDescEntity.setDecript(StringUtils.join(spuImages,","));
            this.descMapper.insert(spuDescEntity);
        }

        //1.3保存spu_attr_value表
        List<SpuAttrValueVo> baseAttrs = spu.getBaseAttrs();
        if(!CollectionUtils.isEmpty(baseAttrs)){
            List<SpuAttrValueEntity> spuAttrValueEntities = baseAttrs.stream().map(SpuAttrValueVo -> {
                SpuAttrValueEntity spuAttrValueEntity = new SpuAttrValueEntity();
                BeanUtils.copyProperties(SpuAttrValueVo, spuAttrValueEntity);
                spuAttrValueEntity.setSpuId(spuId);
                return spuAttrValueEntity;
            }).collect(Collectors.toList());

            this.spuAttrValueService.saveBatch(spuAttrValueEntities);
        }

        //int i = 1/0;
        //2.保存sku相关信息
        List<SkuVo> skus = spu.getSkus();
        if(CollectionUtils.isEmpty(skus)){
            return;
        }

        skus.forEach(skuVo -> {
            //2.1 保存sku表
            skuVo.setSpuId(spuId);
            skuVo.setBrandId(spu.getBrandId());
            skuVo.setCatagoryId(spu.getCategoryId());
            List<String> images = skuVo.getImages();
            if(!CollectionUtils.isEmpty(images)){
                skuVo.setDefaultImage(StringUtils.isNotBlank(skuVo.getDefaultImage())? skuVo.getDefaultImage() : images.get(0));
            }
            this.skuMapper.insert(skuVo);
            Long skuId = skuVo.getId();

            //2.2 保存sku图片表
            if (!CollectionUtils.isEmpty(images)) {
                this.skuImagesService.saveBatch(images.stream().map(image->{
                    SkuImagesEntity skuImagesEntity = new SkuImagesEntity();
                    skuImagesEntity.setSkuId(skuId);
                    skuImagesEntity.setUrl(image);
                    skuImagesEntity.setDefaultStatus(StringUtils.equals(image,skuVo.getDefaultImage())? 1 : 0);
                    return skuImagesEntity;
                }).collect(Collectors.toList()));
            }

            //2.3 保存sku_attr_value表
            List<SkuAttrValueEntity> saleAttrs = skuVo.getSaleAttrs();
            if(!CollectionUtils.isEmpty(saleAttrs)){
                saleAttrs.forEach(skuAttrValueEntity -> skuAttrValueEntity.setSkuId(skuId));
                this.skuattrValueService.saveBatch(saleAttrs);
            }

            //3.保存营销相关信息，远程调用
            SkuSaleVo skuSaleVo = new SkuSaleVo();
            BeanUtils.copyProperties(skuVo,skuSaleVo);
            skuSaleVo.setSkuId(skuId);
            this.gmallSmsClient.saveSkuSaleInfo(skuSaleVo);

        });
    }

}