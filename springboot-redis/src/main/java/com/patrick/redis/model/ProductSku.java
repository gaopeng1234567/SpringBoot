package com.patrick.redis.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author patrick
 * @date 2021/3/27 10:14 上午
 * @Des 商品
 * 最簡單的事是堅持，最難的事還是堅持
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductSku {
    // 编号
    private Integer skuId;
    // 商品名称
    private String skuName;
    // 单价
    private Double skuPrice;
    // 购买个数
    private Integer totalNum;
    // 总价
    private Double totalPrice;
    // 商品类型
    private Enum skuCategory;
}
