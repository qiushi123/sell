package com.qcl.wbspoor;

import org.hibernate.validator.constraints.NotEmpty;

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

    private String startTime;//开始时间

    private String endTime;//结束时间

    private Float startLatitude;
    private Float startlongitude;

    private Float endLatitude;
    private Float endlongitude;

    private String lenght;//长度
    private String duration;//时长



}
