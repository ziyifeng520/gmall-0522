package com.atguigu.gmall.sms.api;

import com.atguigu.gmall.common.bean.ResponseVo;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import vo.SkuSaleVo;


public interface GmallSmsApi {

    @PostMapping("/sms/skubounds/skusale/save")
    public ResponseVo<Object> saveSkuSaleInfo(@RequestBody SkuSaleVo skuSaleVo);
}
