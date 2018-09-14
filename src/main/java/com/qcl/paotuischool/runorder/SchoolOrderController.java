package com.qcl.paotuischool.runorder;

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
import com.qcl.paotuischool.wechat.WxPayService;
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

import java.util.ArrayList;
import java.util.List;

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


    @Autowired
    private WxPayService wxPayService; //微信支付的服务

    //创建单个订单
    @PostMapping("/create")
    public ResultApi create(@Valid RunSchoolOrderForm orderForm,
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
    public ResultApi robOneOrder(@Valid RobOrderForm orderForm,
                                 BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            log.error("[创建订单] 参数不正确，orderForm={}", orderForm);
            throw new SellException(ResultEnum.PARAM_ERROR.getCode()
                    , bindingResult.getFieldError().getDefaultMessage());
        }

        //-1审核失败，1审核中的跑腿员，2审核通过普通跑腿员，3培训过可以寄件跑腿员
        SchoolRunner schoolRunner = schoolerService.findOneOpenid(orderForm.getOpenid());
        if (schoolRunner == null || schoolRunner.getType() < 2) {
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

        //订单状态 -1取消订单，0新下单待抢单，1已被抢单，2已取到，3已送达，4客户确认收货
        //只有新下单并支付过的订单才可以抢
        if (order.getOrderStatus() != 0) {
            throw new SellException(ResultEnum.ORDER_HAS_ROBBED);
        }
        //支付状态 -2已退款，-1已申请退款，0等待支付，1支付完成
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
    public ResultApi list(
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
    public ResultApi oneOrder(
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
     * runnerOrderType: 0新抢到待取件，1已取件，2已送达
     */
    @PostMapping("/ruunerOrderList")
    public ResultApi runerOrderlist(
            @RequestParam("runnerOpenid") String runnerOpenid,
            @RequestParam("runnerOrderType") int runnerOrderType,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "1000") int size) {
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

        Page<RunSchoolOrder> orderPage = service.findRunnerList(runnerOpenid, runnerOrderType, request);

        return ResultApiUtil.success(orderPage.getContent());
    }

    /**
     * 查询所有可以被抢的订单
     * orderType;//0代取快递，1代寄快递
     */
    @PostMapping("/canRobbedOrders")
    public ResultApi canRobbedOrders(
            @RequestParam("runnerOpenid") String runnerOpenid,
            @RequestParam(value = "orderType", defaultValue = "0") Integer orderType,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "1000") int size) {
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

    //跑腿员改变订单状态
    @PostMapping("/changeOrder")
    public ResultApi changeOrder(
            @RequestParam("orderid") String orderid,
            @RequestParam("yundanhao") String yundanhao,
            @RequestParam("openid") String openid,
            @RequestParam("orderStatus") Integer orderStatus) {
        if (StringUtils.isEmpty(orderid)) {
            log.error("[查询订单列表] 订单号不能为空");
            throw new SellException(ResultEnum.ORDER_NOT_EXIST);
        }
        if (StringUtils.isEmpty(openid)) {
            log.error("[查询订单列表] openid不能为空");
            throw new SellException(ResultEnum.USER_NO_LOGIN);
        }
        //-1取消订单，0新下单待抢单，1已被抢单，2已取到，3已送达，4客户确认收货
        //跑腿员只能操作2，3 取消订单需要联系客服，只能客户确认收货
        if (ObjectUtils.isEmpty(orderStatus)) {
            log.error("[修改订单] 订单状态不能为空");
            throw new SellException(ResultEnum.PARAM_ERROR);
        } else if (orderStatus < 0 || orderStatus > 3) {
            throw new SellException(ResultEnum.USER_NO_AUTHORITY);
        }

        RunSchoolOrder order = service.findOne(orderid);
        if (order == null) {
            throw new SellException(ResultEnum.ORDER_NOT_EXIST);
        }

        if (!openid.equals(order.getRunnerId())) {//只有抢单的跑腿员才能操作自己的抢单
            log.error("[修改订单] 您没有操作权限");
            throw new SellException(ResultEnum.USER_NO_AUTHORITY);
        }

        log.error("[填写运单号] yundanhao={}",yundanhao);
        //寄件单，跑腿员确认发件时需要填写发件运单号
        if (!StringUtils.isEmpty(yundanhao)&&orderStatus==3&&order.getOrderType()==1) {
            order.setYundanhao(yundanhao);
            log.error("[填写运单号] 不为空，并改下成功运单号");
        }

        order.setOrderStatus(orderStatus);
        RunSchoolOrder runSchoolOrder = service.create(order);
        return ResultApiUtil.success(runSchoolOrder);

    }

    /*
    * 用户确认收货或者取消订单
    * 未支付订单直接取消
    * 已支付订单联系客服退款
    * */
    @PostMapping("/userChangeOrder")
    public ResultApi userCancelOrder(
            @RequestParam("orderid") String orderid,
            @RequestParam("openid") String openid,
            @RequestParam("orderStatus") int orderStatus) {
        if (StringUtils.isEmpty(orderid)) {
            log.error("[用户取消订单] 订单号不能为空");
            throw new SellException(ResultEnum.ORDER_NOT_EXIST);
        }
        if (StringUtils.isEmpty(openid)) {
            log.error("[用户取消订单] openid不能为空");
            throw new SellException(ResultEnum.USER_NO_LOGIN);
        }

        RunSchoolOrder order = service.findOne(orderid);
        if (order == null) {
            throw new SellException(ResultEnum.ORDER_NOT_EXIST);
        }
        if (!openid.equals(order.getOpenid())) {//用户只能取消自己的订单
            log.error("[修改订单] 您没有操作权限");
            throw new SellException(ResultEnum.USER_NO_AUTHORITY);
        }
        //-1取消订单，0新下单待抢单，1已被抢单，2已取到，3已送达，4客户确认收货
        if (orderStatus == 4) {//确认收货
            order.setOrderStatus(OrderStatusEnum.FINISHED.getCode());
        } else if (orderStatus == -1) {//取消订单
            //支付状态 -2已退款，-1已申请退款退款中，0等待支付，1支付完成
            if (order.getPayStatus() == 1) {//如果已经支付，就申请退款中
                order.setOrderStatus(OrderStatusEnum.CANCEL.getCode());
                order.setPayStatus(PayStatusEnum.REFUND.getCode());
            } else {//未支付直接取消订单
                order.setOrderStatus(OrderStatusEnum.CANCEL.getCode());
            }
        } else {
            throw new SellException(ResultEnum.USER_NO_AUTHORITY);
        }

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
        return wxPayService.createPayOrder(request, orderid, openid);
    }


    /*
    *  微信支付给我们后台的回调
    * 比如上面配置的notify_url是http://系统的ip和端口/wxPay/notify
    * */
    @RequestMapping("/wxPaynotify")
    public void notify(HttpServletRequest request) {
        wxPayService.wxPayCallBacknotify(request);
    }


}
