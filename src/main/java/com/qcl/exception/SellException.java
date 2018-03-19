package com.qcl.exception;

import com.qcl.enums.ResultEnum;

/**
 * Created by qcl on 2018/3/14.
 * 自定义异常
 */

public class SellException extends RuntimeException {
    private Integer code;

    public SellException(ResultEnum resultEnum) {
        super(resultEnum.getMsg());
        this.code = resultEnum.getCode();
    }

    public SellException(Integer code, String defaultMessage) {
        super(defaultMessage);
        this.code = code;
    }
}
