package com.qcl.enums;

import lombok.Getter;

/**
 * Created by qcl on 2018/3/13.
 * 订单状态
 */
@Getter
public enum OrderStatusEnum {
    //-1取消订单，0新下单待抢单，1已被抢单，2已取到，3已送达，4客户确认收货
    CANCEL(-1, "取消订单"),//如果是支付订单，就改变支付状态未退款中
    NEW(0, "新下订单"),
    HAS_BE_ROBBED(1, "已经被抢单"),
    HAS_TAKE(2, "已取件"),
    SENTED(3, "已送达"),
    FINISHED(4, "客户确认收货");

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
