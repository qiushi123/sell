package com.qcl.form;

import org.hibernate.validator.constraints.NotEmpty;

import lombok.Data;

/**
 * Created by qcl on 2018/3/18.
 * 用来验证提交的表单字段
 */
@Data
public class OrderForm {
    //微信id
    @NotEmpty(message = "微信id必传")
    private String openid;
    @NotEmpty(message = "姓名必传")
    private String name;
    @NotEmpty(message = "手机号必传")
    private String phone;
    @NotEmpty(message = "收货地址必传")
    private String address;
    @NotEmpty(message = "购物车不能为空")
    private String items;
}
