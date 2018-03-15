package com.qcl.enums;

import lombok.Getter;

/**
 * 商品上架状态的枚举
 * Created by qcl on 2017/12/17.
 */
@Getter
public enum ProductStatusEnum {
    PRODUCT_UP(0, "在架"),
    PRODUCT_DOWN(1, "下架");

    private Integer code;
    private String message;

    ProductStatusEnum(Integer code, String message) {
        this.code = code;
        this.message = message;
    }
}
