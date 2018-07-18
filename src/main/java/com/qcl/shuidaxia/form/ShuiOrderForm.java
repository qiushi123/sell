package com.qcl.shuidaxia.form;

import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;

import lombok.Data;

/**
 * Created by qcl on 2018/3/18.
 * 用来验证用户注册提交的表单字段
 */
@Data
public class ShuiOrderForm {
    private String orderId;
    @NotNull(message = "会员编号不能为空")
    private Long userId;
    @NotEmpty(message = "姓名不能为空")
    private String name;
    @NotEmpty(message = "联系方式不能为空")
    //    @Size(min = 11, max = 11, message = "手机号必须是11位")
    private String phone;
    @NotEmpty(message = "配送地址不能为空")
    private String address;

    @NotEmpty(message = "送水侠不能为空")
    private String songshuixia;
    @NotEmpty(message = "配送时间不能为空")
    private String time;
    @NotNull(message = "送几桶不能为空")
    private Integer songjitong;


    private Integer huikongtong;
    private Integer gongjitong;
    private Integer yushui;
    private Integer xinmaishui;
    private Integer shouru;//新加收入
    private Integer zhichu;//新加支出
    @NotEmpty(message = "收款人不能为空")
    private String shoukuanren;
    private String beizhu;
}
