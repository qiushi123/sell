package com.qcl.paotuischool.runorder;

import com.jpay.ext.kit.HttpKit;
import com.jpay.ext.kit.PaymentKit;
import com.jpay.weixin.api.WxPayApi;
import com.qcl.api.ResultApi;
import com.qcl.enums.OrderStatusEnum;
import com.qcl.enums.OrderTypeEnum;
import com.qcl.enums.PayStatusEnum;
import com.qcl.enums.ResultEnum;
import com.qcl.exception.SellException;
import com.qcl.paotuischool.bean.RunOrderForm2DTOConverter;
import com.qcl.paotuischool.bean.RunSchoolOrder;
import com.qcl.paotuischool.bean.SchoolRunner;
import com.qcl.paotuischool.form.RobOrderForm;
import com.qcl.paotuischool.form.RunSchoolOrderForm;
import com.qcl.paotuischool.runner.SchoolerService;
import com.qcl.paotuischool.wechat.AjaxJson;
import com.qcl.paotuischool.wechat.WxPushService;
import com.qcl.utils.ProtectUserUtils;
import com.qcl.utils.ResultApiUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import lombok.extern.slf4j.Slf4j;

/**
 * 跑腿
 */

@RestController
@RequestMapping("/runSchoolOrder")
@Slf4j
public class SchoolOrderController {

    @Autowired
    private SchoolOrderService service;

    //查询跑腿员信息
    @Autowired
    private SchoolerService schoolerService;

    //微信推送的服务
    @Autowired
    private WxPushService wxPushService;

    //创建单个订单
    @PostMapping("/create")
    public ResultApi<Map<String, String>> create(@Valid RunSchoolOrderForm orderForm,
                                                 BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            log.error("[创建订单] 参数不正确，orderForm={}", orderForm);
            throw new SellException(ResultEnum.PARAM_ERROR.getCode()
                    , bindingResult.getFieldError().getDefaultMessage());
        }
        //金额必须大于0
        if (orderForm.getTotalMoney() == null || orderForm.getTotalMoney() <= 0) {
            throw new SellException(ResultEnum.ORDER_MONEY_ERROR);
        }

        RunSchoolOrder orderDTO = RunOrderForm2DTOConverter.converter(orderForm);
        RunSchoolOrder result = service.create(orderDTO);
        return ResultApiUtil.success(result);
    }

    /**
     * 跑腿员抢单
     */
    @PostMapping("/robOrder")
    public ResultApi<RunSchoolOrder> robOneOrder(@Valid RobOrderForm orderForm,
                                                 BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            log.error("[创建订单] 参数不正确，orderForm={}", orderForm);
            throw new SellException(ResultEnum.PARAM_ERROR.getCode()
                    , bindingResult.getFieldError().getDefaultMessage());
        }

        //-1审核失败，1审核中的跑腿员，2审核通过普通跑腿员，3培训过可以寄件跑腿员
        SchoolRunner schoolRunner = schoolerService.findOneOpenid(orderForm.getOpenid());
        if (schoolRunner.getType() < 2) {
            log.error("[查询可抢订单列表] 跑腿员没有审核通过");
            throw new SellException(ResultEnum.USER_NO_AUTHORITY);
        }


        RunSchoolOrder order = service.findOne(orderForm.getOrderid());
        if (order == null) {
            throw new SellException(ResultEnum.ORDER_NOT_EXIST);
        }

        //0代取快递，1代寄快递；只有培训过的跑腿员才可以抢寄件单
        if (order.getOrderType() == OrderTypeEnum.SEND.getCode()) {
            //-1审核失败，1审核中的跑腿员，2审核通过普通跑腿员，3培训过可以寄件跑腿员
            if (schoolRunner.getType() < 3) {
                log.error("[查询可抢订单列表] 跑腿员没有培训过");
                throw new SellException(ResultEnum.USER_NO_TRAINING);
            }
        }

        //订单状态，-1取消订单，0新下单，1已抢单，2订单完成
        if (order.getOrderStatus() != 0) {
            throw new SellException(ResultEnum.ORDER_HAS_ROBBED);
        }
        //未支付的订单不能抢
        if (order.getPayStatus() != 1) {
            throw new SellException(ResultEnum.ORDER_HAS_NOPAY);
        }
        order.setOrderStatus(OrderStatusEnum.HAS_BE_ROBBED.getCode());//设置已被抢单
        order.setRunnerId(orderForm.getOpenid());
        order.setRunnerName(orderForm.getName());
        order.setRunnerPhone(orderForm.getPhone());
        RunSchoolOrder result = service.create(order);
        return ResultApiUtil.success(result);
    }

    /**
     * 某一个用户的所有订单
     */
    @PostMapping("/myOrderList")
    public ResultApi<List<RunSchoolOrder>> list(
            @RequestParam("openid") String openid,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "200") int size) {
        if (StringUtils.isEmpty(openid)) {
            log.error("[查询订单列表] openid为空");
            throw new SellException(ResultEnum.PARAM_ERROR);
        }

        //按订单创建时间到排序，新订单在最前面
        Sort sort = new Sort(Sort.Direction.DESC, "updateTime");
        PageRequest request = new PageRequest(page, size, sort);

        Page<RunSchoolOrder> orderPage = service.findList(openid, request);

        return ResultApiUtil.success(orderPage.getContent());
    }


    /**
     * 查询某一个订单
     */
    @PostMapping("/oneOrder")
    public ResultApi<RunSchoolOrder> oneOrder(
            @RequestParam("orderid") String orderid) {
        if (StringUtils.isEmpty(orderid)) {
            log.error("[查询订单列表] 订单号不能为空");
            throw new SellException(ResultEnum.ORDER_NOT_EXIST);
        }
        RunSchoolOrder order = service.findOne(orderid);
        if (order == null) {
            throw new SellException(ResultEnum.ORDER_NOT_EXIST);
        }
        return ResultApiUtil.success(order);
    }

    /**
     * 某一个跑腿员抢的所有订单
     */
    @PostMapping("/ruunerOrderList")
    public ResultApi<List<RunSchoolOrder>> runerOrderlist(
            @RequestParam("runnerOpenid") String runnerOpenid,
            @RequestParam("isOk") boolean isOk,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "200") int size) {
        if (StringUtils.isEmpty(runnerOpenid)) {
            log.error("[查询订单列表] openid为空");
            throw new SellException(ResultEnum.PARAM_ERROR);
        }

        //按订单创建时间到排序，新订单在最前面,加急排前
        List<Sort.Order> list = new ArrayList<>(2);
        Sort.Order order1 = new Sort.Order(Sort.Direction.DESC, "isJiaJi");
        Sort.Order order2 = new Sort.Order(Sort.Direction.DESC, "updateTime");
        list.add(order1);
        list.add(order2);
        Sort sort = new Sort(list);
        PageRequest request = new PageRequest(page, size, sort);

        Page<RunSchoolOrder> orderPage = service.findRunnerList(runnerOpenid, isOk, request);

        return ResultApiUtil.success(orderPage.getContent());
    }

    /**
     * 查询所有可以被抢的订单
     * 匹配城市，只展示跑腿员所在城市的订单
     * orderType;//0代取快递，1代寄快递
     */
    @PostMapping("/canRobbedOrders")
    public ResultApi<List<RunSchoolOrder>> canRobbedOrders(
            @RequestParam("runnerOpenid") String runnerOpenid,
            @RequestParam(value = "orderType", defaultValue = "0") Integer orderType,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "200") int size) {
        if (StringUtils.isEmpty(runnerOpenid)) {
            log.error("[查询订单列表] 跑腿员openid为空");
            throw new SellException(ResultEnum.PARAM_ERROR);
        }
        //只有注册了跑腿员，才能查看可以抢的订单
        //-1审核失败，1审核中的跑腿员，2审核通过普通跑腿员，3培训过可以寄件跑腿员
        SchoolRunner schoolRunner = schoolerService.findOneOpenid(runnerOpenid);
        if (schoolRunner.getType() < 2) {
            log.error("[查询可抢订单列表] 跑腿员没有审核通过");
            throw new SellException(ResultEnum.USER_NO_AUTHORITY);
        }

        //按订单创建时间到排序，新订单在最前面
        List<Sort.Order> list = new ArrayList<>(2);
        Sort.Order order1 = new Sort.Order(Sort.Direction.DESC, "isJiaJi");
        Sort.Order order2 = new Sort.Order(Sort.Direction.DESC, "updateTime");
        list.add(order1);
        list.add(order2);
        Sort sort = new Sort(list);
        PageRequest request = new PageRequest(page, size, sort);
        Page<RunSchoolOrder> orderPage = service.canRobbedOrders(orderType, request);

        List<RunSchoolOrder> orderList = ProtectUserUtils.protectSchoolUserOrders(orderPage.getContent());
        return ResultApiUtil.success(orderList);
    }

    //改变订单状态
    @PostMapping("/changeOrder")
    public ResultApi changeOrder(
            @RequestParam("orderid") String orderid,
            @RequestParam("openid") String openid,
            @RequestParam("orderType") Integer orderType) {
        if (StringUtils.isEmpty(orderid)) {
            log.error("[查询订单列表] 订单号不能为空");
            throw new SellException(ResultEnum.ORDER_NOT_EXIST);
        }
        if (StringUtils.isEmpty(openid)) {
            log.error("[查询订单列表] openid不能为空");
            throw new SellException(ResultEnum.USER_NO_LOGIN);
        }
        if (ObjectUtils.isEmpty(orderType)) {
            log.error("[修改订单] 订单状态不能为空");
            throw new SellException(ResultEnum.PARAM_ERROR);
        }

        //订单状态，-1取消订单并退款，0新下单，1已抢单，2已送达，3订单完成
        RunSchoolOrder order = service.findOne(orderid);
        if (order == null) {
            throw new SellException(ResultEnum.ORDER_NOT_EXIST);
        }
        if (orderType == OrderStatusEnum.SENTED.getCode()) {
            if (!openid.equals(order.getRunnerId())) {//只有抢单的跑腿员才能送达
                log.error("[修改订单] 您没有操作权限");
                throw new SellException(ResultEnum.USER_NO_AUTHORITY);
            }
        } else if (orderType == OrderStatusEnum.FINISHED.getCode()) {//只有用户才能确认订单
            if (!openid.equals(order.getOpenid())) {//只有抢单的跑腿员才能送达
                log.error("[修改订单] 您没有操作权限");
                throw new SellException(ResultEnum.USER_NO_AUTHORITY);
            }
        } else if (orderType == OrderStatusEnum.CANCEL.getCode()) {//只有管理员才能取消订单并退款
            // TODO: 2018/9/9 待开发，管理员取消订单并退款功能

        }


        order.setOrderStatus(orderType);
        RunSchoolOrder runSchoolOrder = service.create(order);
        return ResultApiUtil.success(runSchoolOrder);

    }


    //=======================下面是小程序微信支付相关=======================

    /**
     * 小程序微信支付的第一步,统一下单
     *
     * @return
     * @author zgd
     * @time 2018年7月9日17:53:35
     */
    @PostMapping("/createWechatPayOrder")
    //    @ResponseBody
    public AjaxJson createUnifiedOrder(
            HttpServletRequest request,
            @RequestParam("orderid") String orderid,
            @RequestParam("openid") String openid) {
        String notify_url = "https://30paotui.com/runSchoolOrder/wxPaynotify";
        //String notify_url = "http://localhost:8080/runSchoolOrder/wxPay";
        String partnerKey = "Lizulizuqiuchunleiqiuchunlei0908";//partnerKey支付密码钥匙


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
        RunSchoolOrder MyOrder = service.findOne(orderid);
        if (MyOrder == null) {
            throw new SellException(ResultEnum.ORDER_NOT_EXIST);
        }

        String return_msg = "创建微信订单失败";

        try {
            //支付金额 **金额不能有小数点,单位是分!!**
            BigDecimal price = new BigDecimal(MyOrder.getTotalMoney().toString());
            BigDecimal beishu = new BigDecimal("100");
            BigDecimal priceFee = price.multiply(beishu);
            //商家订单号
            String orderNo = MyOrder.getOrderId();

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
            paraMap.put("appid", "wx3a9741a27fe04fd3");
            //设置请求参数(商户号)
            paraMap.put("mch_id", "1514204271");
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
            paraMap.put("notify_url", notify_url);
            //设置请求参数(交易类型)
            paraMap.put("trade_type", String.valueOf(WxPayApi.TradeType.JSAPI));
            //设置请求参数(openid)(在接口文档中 该参数 是否必填项 但是一定要注意 如果交易类型设置成'JSAPI'则必须传入openid)
            paraMap.put("openid", openid);
            //MD5运算生成签名，这里是第一次签名，用于调用统一下单接口
            String sign = PaymentKit.createSign(paraMap, partnerKey);

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
                returnMap.put("appId", "wx3a9741a27fe04fd3");
                returnMap.put("nonceStr", nonceStr);
                String prepay_id = (String) map.get("prepay_id");
                returnMap.put("package", "prepay_id=" + prepay_id);
                returnMap.put("signType", "MD5");
                //这边要将返回的时间戳转化成字符串，不然小程序端调用wx.requestPayment方法会报签名错误
                returnMap.put("timeStamp", timeStamp);
                //                returnMap.put("orderid",orderid);
                //拼接签名需要的参数
                //再次签名，这个签名用于小程序端调用wx.requesetPayment方法
                String paySign = PaymentKit.createSign(returnMap, partnerKey).toUpperCase();
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
     *
     * @param return_code
     * @return
     * @author zgd
     * @time 2018年7月9日17:53:13
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
    * 比如上面配置的notify_url是http://系统的ip和端口/wxPay/notify
    * */
    @RequestMapping("/wxPaynotify")
    public void notify(HttpServletRequest request) {
        String paternerKey = "Lizulizuqiuchunleiqiuchunlei0908";

        //获取所有的参数
        StringBuffer sbf = new StringBuffer();

        // 支付结果通用通知文档: https://pay.weixin.qq.com/wiki/doc/api/jsapi.php?chapter=9_7
        String xmlMsg = HttpKit.readData(request);
        System.out.println("支付通知=" + xmlMsg);
        Map<String, String> params = PaymentKit.xmlToMap(xmlMsg);
        String result_code = params.get("result_code");

        log.error("校验通过.返回的参数={}", params);
        //校验返回来的支付结果,根据已经配置的密钥
        if (PaymentKit.verifyNotify(params, paternerKey)) {
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
