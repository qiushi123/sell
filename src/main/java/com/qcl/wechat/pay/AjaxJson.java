package com.qcl.wechat.pay;

import java.io.Serializable;
import java.util.concurrent.ConcurrentMap;

import lombok.Data;

@Data
public class AjaxJson implements Serializable {

    private static final long serialVersionUID = 1L;

    private boolean success = true;// 是否成功

    private String msg = "操作成功";// 提示信息

    private Object obj = null;// 其他信息

    private ConcurrentMap<String, Object> attributes;// 其他参数

    private String errorCode;// 错误码

    private Integer totalSize;// 错误码


}