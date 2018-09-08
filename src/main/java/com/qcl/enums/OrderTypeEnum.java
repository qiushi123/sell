package com.qcl.enums;

import lombok.Getter;

/**
 * Created by qcl on 2018/3/13.
 * 订单类型
 */
@Getter
public enum OrderTypeEnum {
    TAKE(0, "代取订单"),
    SEND(1,"代寄订单");


    OrderTypeEnum(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    private Integer code;
    private String message;

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
