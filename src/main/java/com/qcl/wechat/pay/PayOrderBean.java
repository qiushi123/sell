package com.qcl.wechat.pay;

import lombok.Data;

/**
 * Created by qcl on 2018/9/27.
 */
@Data
public class PayOrderBean {
    private Float totalMoney;//总费用
    private String orderId;//订单号
    private String orderType = "跑腿-支付订单";
    private String openid;

    private String appid;

    //支付相关
    private String mch_id;//支付商户号id
    private String paySign;//支付密码钥匙

    private String notify_url;//支付成功后的回调接口


}
