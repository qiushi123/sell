package com.qcl.enums;

import lombok.Getter;

/**
 * 把所有的状态码统一用枚举列出来
 * Created by qcl on 2017/12/16.
 */
@Getter
public enum ResultEnum {
    //请求参数相关
    PARAM_ERROR(1, "参数不正确"),
    RESULT_ERROR(-100, "未知错误"),
    RESULT_NO(-101, "没有这条结果"),
    RESULT_OK(100, "成功"),
    PRODUCT_NOT_EXIST(10, "商品不存在"),
    CODE_NOT_EXIST(12, "验证码不存在"),

    PRODUCT_ERROR_0(11, "商品库存已经为零"),
    ORDER_NOT_EXIST(20, "订单不存在"),
    ORDER_DETAIL_NOT_EXIST(21, "订单详情不存在"),
    ORDER_STATUS_ERROR(22, "订单状态不正确"),
    ORDER_UPDATE_FAIL(23, "订单状态更新失败"),
    ORDER_DETAIL_EMPTY(24, "订单里商品为空"),
    ORDER_PAY_STATUS_ERROR(25, "订单支付状态不正确"),
    ORDER_OPENID_ERROR(26, "查询的订单和用户不匹配"),
    ORDER_HAS_ROBBED(27, "订单已被抢"),
    //购物车相关
    CART_EMPTY(31, "购物车不能为空"),
    //用户相关
    USER_HAS_REGISTER(41, "用户已经注册过"),
    USER_HAVE_EXIST(43, "用户已经验证过"),
    USER_NO_LOGIN(44, "用户没有登陆"),
    USER_ADMIN_NO(45, "管理员不存在"),
    USER_PASSWORD_ERROR(46, "密码错误"),
    USER_NO_AUTHORITY(47, "没有操作权限");


    private Integer code;
    private String msg;

    ResultEnum(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public Integer getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }
}
