package com.qcl.wbspoor;

import org.hibernate.validator.constraints.NotEmpty;

import java.util.Date;

import javax.validation.constraints.NotNull;

import lombok.Data;

/**
 * Created by qcl on 2018/3/18.
 * 用来验证用户注册提交的表单字段
 */
@Data
public class SpoorForm {

    @NotNull(message = "用户id必传")
    private Long userid;//用户id

    @NotEmpty(message = "所在城市必传")
    private String city;//所在城市

    private Date startTime;//开始时间

    private Date endTime;//结束时间

    private String lenght;//长度

    private String message;//附加信息（文字描述）


}
