package com.qcl.wechat.pay;

import org.hibernate.validator.constraints.NotEmpty;

import lombok.Data;

/**
 * Created by qcl on 2018/9/27.
 */
@Data
public class PayOrderForm {

    private String orderType = "跑腿-支付订单";


    @NotEmpty(message = "订单号必传")
    private String orderId;//订单号
    @NotEmpty(message = "微信id必传")
    private String openid;

    @NotEmpty(message = "微信appid必传")
    private String appid;

    //支付相关
    @NotEmpty(message = "支付商户号id必传")
    private String mch_id;//支付商户号id
    @NotEmpty(message = "支付密码钥匙必传")
    private String paySign;//支付密码钥匙

    @NotEmpty(message = "支付回调的url必传")
    private String notify_url;//支付成功后的回调接口


}
