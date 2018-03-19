package com.qcl.utils;

import com.qcl.api.ResultApi;
import com.qcl.enums.ResultEnum;

/**
 * 返回给前端的json格式工具类
 * Created by qcl on 2017/12/17.
 */
public class ResultApiUtil {

    public static ResultApi success(Object object) {
        ResultApi success = success(ResultEnum.RESULT_OK, object);
        return success;
    }

    public static ResultApi success(ResultEnum resultEnum, Object object) {
        ResultApi resultApi = new ResultApi();
        resultApi.setCode(resultEnum.getCode());
        resultApi.setMsg(resultEnum.getMsg());
        resultApi.setData(object);
        return resultApi;
    }
}
