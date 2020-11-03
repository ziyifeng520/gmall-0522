package vo;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class SkuSaleVo {
    private Long skuId;

    //积分活动
    private BigDecimal growBounds;
    private BigDecimal buyBounds;

    private List<Integer> work;

    //满减活动
    private BigDecimal fullPrice;
    private BigDecimal reducePrice;
    private Integer fullAddOther;

    //打折优惠
    private Integer fullCount;
    private BigDecimal discount;

    //是否叠加其他优惠
    private Integer addOther;
}
