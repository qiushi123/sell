package com.qcl.api;

import lombok.Data;

/**
 * 返回给前端的接口json
 * Created by qcl on 2017/12/17.
 */
@Data
public class ResultApi<T> {
    private Integer code;
    private String msg;
    private T data;
}
