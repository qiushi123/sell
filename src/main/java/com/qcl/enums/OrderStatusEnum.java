package com.qcl.enums;

import lombok.Getter;

/**
 * Created by qcl on 2018/3/13.
 * 订单状态
 */
@Getter
public enum OrderStatusEnum {
    NEW(0, "新下订单"),
    HAS_BE_ROBBED(1,"已经被抢单"),
    SENTED(2,"已经送达"),
    FINISHED(3, "完成订单"),
    CANCEL(-1, "取消订单");

    OrderStatusEnum(Integer code, String message) {
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
