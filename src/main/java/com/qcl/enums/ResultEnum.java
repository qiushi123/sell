package com.qcl.enums;

import lombok.Getter;

/**
 * 把所有的状态码统一用枚举列出来
 * Created by qcl on 2017/12/16.
 */
@Getter
public enum ResultEnum {
    RESULT_ERROR(-100, "未知错误"),
    RESULT_NO(-101, "没有这条结果"),
    RESULT_OK(100, "成功"),
    PRODUCT_NOT_EXIST(10, "商品不存在"),
    PRODUCT_ERROR_0(11, "商品库存已经为零"),
    ORDER_NOT_EXIST(20, "订单不存在"),
    ORDER_DETAIL_NOT_EXIST(21, "订单详情不存在"),;

    private Integer code;
    private String msg;

    ResultEnum(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public Integer getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }
}
