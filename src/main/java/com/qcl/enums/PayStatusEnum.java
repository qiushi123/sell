package com.qcl.enums;

import lombok.Getter;

/**
 * Created by qcl on 2018/3/13.
 */
@Getter
public enum PayStatusEnum {
    //-2已退款，-1已申请退款，0等待支付，1支付完成
    MONEY_BACK(-2, "已退款"),
    REFUND(-1, "已申请退款"),
    WAIT(0, "等待支付"),
    SUCESS(1, "支付成功");

    PayStatusEnum(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    private Integer code;
    private String message;
}
