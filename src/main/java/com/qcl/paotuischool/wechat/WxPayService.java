package com.qcl.paotuischool.wechat;

import com.jpay.ext.kit.HttpKit;
import com.jpay.ext.kit.PaymentKit;
import com.jpay.weixin.api.WxPayApi;
import com.qcl.enums.PayStatusEnum;
import com.qcl.enums.ResultEnum;
import com.qcl.exception.SellException;
import com.qcl.paotuischool.bean.RunSchoolOrder;
import com.qcl.paotuischool.runorder.SchoolOrderService;
import com.qcl.utils.ConstantUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import lombok.extern.slf4j.Slf4j;

/**
 * Created by qcl on 2018/9/11.
 * 微信支付的服务
 */
@Service
@Slf4j
public class WxPayService {

    @Autowired
    private SchoolOrderService service;//订单的服务

    @Autowired
    private WxPushService wxPushService;//微信推送的服务

    /**
     * 小程序微信支付的第一步,统一下单,创建支付订单
     *
     * @return
     */
    public AjaxJson createPayOrder(
            HttpServletRequest request,
            String orderid, String openid) {

        AjaxJson aj = new AjaxJson();
        aj.setSuccess(false);
        if (StringUtils.isEmpty(orderid)) {
            log.error("[微信支付创建订单] 订单号不能为空");
            throw new SellException(ResultEnum.ORDER_NOT_EXIST);
        }
        if (StringUtils.isEmpty(openid)) {
            log.error("[微信支付创建订单] openid不能为空");
            throw new SellException(ResultEnum.USER_NO_LOGIN);
        }
        //这里调用service层根据订单id获取订单数据,这里省略不表
        RunSchoolOrder myOrder = service.findOne(orderid);
        if (myOrder == null) {
            throw new SellException(ResultEnum.ORDER_NOT_EXIST);
        }

        String return_msg = "创建微信订单失败";

        try {
            //支付金额 **金额不能有小数点,单位是分!!**
            BigDecimal price = new BigDecimal(myOrder.getTotalMoney().toString());
            BigDecimal beishu = new BigDecimal("100");
            BigDecimal priceFee = price.multiply(beishu);
            //商家订单号
            String orderNo = myOrder.getOrderId();

            //创建 时间戳
            String timeStamp = Long.valueOf(System.currentTimeMillis()).toString();
            //生成32位随机数
            UUID uuid = UUID.randomUUID();
            String nonceStr = uuid.toString().replaceAll("-", "");
            //商品描述
            String body = "找我啊校园跑腿-支付订单";
            //创建hashmap(用户获得签名)
            SortedMap<String, String> paraMap = new TreeMap<>();
            //设置请求参数(小程序ID)
            paraMap.put("appid", ConstantUtils.SCHOOL_APPID);
            //设置请求参数(商户号)
            paraMap.put("mch_id", ConstantUtils.WXPAY_MCH_ID);
            //设置请求参数(随机字符串)
            paraMap.put("nonce_str", nonceStr);
            //设置请求参数(商品描述)
            paraMap.put("body", body);
            //设置请求参数(商户订单号)
            paraMap.put("out_trade_no", orderNo);
            //设置请求参数(总金额)
            paraMap.put("total_fee", priceFee.toBigInteger().toString());
            //设置请求参数(终端IP) 如果是springmvc,或者能获取到request的servlet,用下面这种
            paraMap.put("spbill_create_ip", request.getRemoteAddr());
            //设置请求参数(通知地址)
            paraMap.put("notify_url", ConstantUtils.WXPAY_NOTIFY_URL);
            //设置请求参数(交易类型)
            paraMap.put("trade_type", String.valueOf(WxPayApi.TradeType.JSAPI));
            //设置请求参数(openid)(在接口文档中 该参数 是否必填项 但是一定要注意 如果交易类型设置成'JSAPI'则必须传入openid)
            paraMap.put("openid", openid);
            //MD5运算生成签名，这里是第一次签名，用于调用统一下单接口
            String sign = PaymentKit.createSign(paraMap, ConstantUtils.WXPAY_PARTNERKEY);

            paraMap.put("sign", sign);
            //统一下单,向微信api发送数据
            log.error("微信小程序统一下单发送的数据:={}", paraMap.toString());
            String xmlResult = WxPayApi.pushOrder(false, paraMap);
            log.error("微信小程序统一下单接受返回的结果:={}", xmlResult);
            //转成xml
            Map<String, String> map = PaymentKit.xmlToMap(xmlResult);
            //返回状态码
            String return_code = (String) map.get("return_code");
            return_msg = return_msg + ", " + (String) map.get("return_msg");
            //返回给小程序端需要的参数
            Map<String, String> returnMap = new HashMap<String, String>();
            if ("SUCCESS".equals(return_code)) {
                //返回的预付单信息
                returnMap.put("appId", ConstantUtils.SCHOOL_APPID);
                returnMap.put("nonceStr", nonceStr);
                String prepay_id = (String) map.get("prepay_id");
                returnMap.put("package", "prepay_id=" + prepay_id);
                returnMap.put("signType", "MD5");
                //这边要将返回的时间戳转化成字符串，不然小程序端调用wx.requestPayment方法会报签名错误
                returnMap.put("timeStamp", timeStamp);
                //                returnMap.put("orderid",orderid);
                //拼接签名需要的参数
                //再次签名，这个签名用于小程序端调用wx.requesetPayment方法
                String paySign = PaymentKit.createSign(returnMap, ConstantUtils.WXPAY_PARTNERKEY).toUpperCase();
                returnMap.put("paySign", paySign);
                aj.setObj(returnMap);
                aj.setMsg("操作成功");
                aj.setSuccess(true);
                return aj;
            } else {
                aj.setMsg(getMsgByCode(return_code));
                log.error(Thread.currentThread().getStackTrace()[1].getMethodName() + ">>>" + return_msg);
            }
        } catch (Exception e) {
            log.error(Thread.currentThread().getStackTrace()[1].getMethodName() + "发生的异常是: ", e);
            aj.setMsg("微信支付下单失败,请稍后再试");
        }
        return aj;
    }

    /**
     * 判断返回的return_code,给前端相应的提示
     */
    private String getMsgByCode(String return_code) {
        switch (return_code) {
            case "NOTENOUGH":
                return "您的账户余额不足";
            case "ORDERPAID":
                return "该订单已支付完成,请勿重复支付";
            case "ORDERCLOSED":
                return "当前订单已关闭，请重新下单";
            case "SYSTEMERROR":
                return "系统超时，请重新支付";
            case "OUT_TRADE_NO_USED":
                return "请勿重复提交该订单";
            default:
                return "网络正在开小差,请稍后再试";
        }
    }

    /*
    *  微信支付给我们后台的回调
    * */
    public void wxPayCallBacknotify(HttpServletRequest request) {

        //获取所有的参数
        StringBuffer sbf = new StringBuffer();

        // 支付结果通用通知文档: https://pay.weixin.qq.com/wiki/doc/api/jsapi.php?chapter=9_7
        String xmlMsg = HttpKit.readData(request);
        System.out.println("支付通知=" + xmlMsg);
        Map<String, String> params = PaymentKit.xmlToMap(xmlMsg);
        String result_code = params.get("result_code");

        log.error("校验通过.返回的参数={}", params);
        //校验返回来的支付结果,根据已经配置的密钥
        if (PaymentKit.verifyNotify(params, ConstantUtils.WXPAY_PARTNERKEY)) {
            //Constants.SUCCESS="SUCCESS"
            if (("SUCCESS").equals(result_code)) {
                //                    校验通过.更改订单状态为已支付, 修改库存
                String orderid = params.get("out_trade_no");
                log.error("校验通过.返回的订单out_trade_no={}", orderid);
                RunSchoolOrder myOrder = service.findOne(orderid);
                myOrder.setPayStatus(PayStatusEnum.SUCESS.getCode());
                service.create(myOrder);
                log.error("校验通过.更改订单状态为已支付, 修改库存");
                System.out.println("校验通过.更改订单状态为已支付, 修改库存");

                log.error("订单创建成功，推送服务");
                wxPushService.pushAll(myOrder);
            }
        }
    }

}
