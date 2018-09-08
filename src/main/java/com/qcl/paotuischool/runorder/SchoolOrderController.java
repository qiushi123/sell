package com.qcl.paotuischool.runorder;

import com.qcl.api.ResultApi;
import com.qcl.enums.OrderStatusEnum;
import com.qcl.enums.OrderTypeEnum;
import com.qcl.enums.ResultEnum;
import com.qcl.exception.SellException;
import com.qcl.paotuischool.bean.RunOrderForm2DTOConverter;
import com.qcl.paotuischool.bean.RunSchoolOrder;
import com.qcl.paotuischool.bean.SchoolRunner;
import com.qcl.paotuischool.form.RobOrderForm;
import com.qcl.paotuischool.form.RunSchoolOrderForm;
import com.qcl.paotuischool.runner.SchoolerService;
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
import java.util.Map;

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

    //创建单个订单
    @PostMapping("/create")
    public ResultApi<Map<String, String>> create(@Valid RunSchoolOrderForm orderForm,
                                                 BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            log.error("[创建订单] 参数不正确，orderForm={}", orderForm);
            throw new SellException(ResultEnum.PARAM_ERROR.getCode()
                    , bindingResult.getFieldError().getDefaultMessage());
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

        //订单状态，-1取消订单，0新下单，1已抢单，2已送达，3订单完成
        RunSchoolOrder order = service.findOne(orderid);
        if (order == null) {
            throw new SellException(ResultEnum.ORDER_NOT_EXIST);
        }
        if (orderType == OrderStatusEnum.SENTED.getCode()) {
            if (!openid.equals(order.getRunnerId())) {//只有抢单的跑腿员才能送达
                log.error("[修改订单] 您没有操作权限");
                throw new SellException(ResultEnum.USER_NO_AUTHORITY);
            }
        } else if (orderType == OrderStatusEnum.CANCEL.getCode()
                || orderType == OrderStatusEnum.FINISHED.getCode()) {//只有用户才能取消或者确认订单
            if (!openid.equals(order.getOpenid())) {//只有抢单的跑腿员才能送达
                log.error("[修改订单] 您没有操作权限");
                throw new SellException(ResultEnum.USER_NO_AUTHORITY);
            }
        }


        order.setOrderStatus(orderType);
        RunSchoolOrder runSchoolOrder = service.create(order);
        return ResultApiUtil.success(runSchoolOrder);

    }
}
