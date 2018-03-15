package com.qcl.dto;

import lombok.Data;

/**
 * Created by qcl on 2018/3/14.
 * 购物车
 */
@Data
public class CartDTO {
    //商品id
    private String productId;
    //数量
    private Integer productQuantity;

    public CartDTO(String productId, Integer productQuantity) {
        this.productId = productId;
        this.productQuantity = productQuantity;
    }
}
