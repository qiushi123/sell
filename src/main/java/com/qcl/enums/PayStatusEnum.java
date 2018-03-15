package com.qcl.enums;

import lombok.Getter;

/**
 * Created by qcl on 2018/3/13.
 *
 *
 */
@Getter
public enum PayStatusEnum {
    WAIT(0, "等待支付"),
    SUCESS(1, "支付成功");

    PayStatusEnum(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    private Integer code;
    private String message;
}
