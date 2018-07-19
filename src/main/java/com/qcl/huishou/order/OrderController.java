package com.qcl.huishou.order;

import com.qcl.api.ResultApi;
import com.qcl.enums.OrderStatusEnum;
import com.qcl.enums.ResultEnum;
import com.qcl.exception.SellException;
import com.qcl.huishou.bean.HuishouOrder;
import com.qcl.huishou.bean.Huishouer;
import com.qcl.huishou.form.RobOrderForm;
import com.qcl.huishou.form.RunOrderForm;
import com.qcl.huishou.huishouer.HuishouerService;
import com.qcl.utils.KeyUtil;
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

import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import lombok.extern.slf4j.Slf4j;

/**
 * 跑腿
 */

@RestController
@RequestMapping("/huishou")
@Slf4j
public class OrderController {

    @Autowired
    private OrderService service;

    //查询跑腿员信息
    @Autowired
    private HuishouerService huishouerService;

    //创建单个订单
    @PostMapping("/create")
    public ResultApi<Map<String, String>> create(@Valid RunOrderForm orderForm,
                                                 BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            log.error("[创建订单] 参数不正确，orderForm={}", orderForm);
            throw new SellException(ResultEnum.PARAM_ERROR.getCode()
                    , bindingResult.getFieldError().getDefaultMessage());
        }

        HuishouOrder orderDTO = new HuishouOrder();
        orderDTO.setOrderId(KeyUtil.genUniqueKey());
        orderDTO.setBuyerOpenid(orderForm.getOpenid());
        orderDTO.setBuyerName(orderForm.getName());
        orderDTO.setBuyerPhone(orderForm.getPhone());
        orderDTO.setCity(orderForm.getCity());
        orderDTO.setBuyerAdderss(orderForm.getAddress());
        orderDTO.setWhatDesc(orderForm.getWhat());
        HuishouOrder result = service.create(orderDTO);
        return ResultApiUtil.success(result);
    }

    /**
     * 跑腿员抢单
     */
    @PostMapping("/robOrder")
    public ResultApi<HuishouOrder> robOneOrder(@Valid RobOrderForm orderForm,
                                               BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            log.error("[创建订单] 参数不正确，orderForm={}", orderForm);
            throw new SellException(ResultEnum.PARAM_ERROR.getCode()
                    , bindingResult.getFieldError().getDefaultMessage());
        }

        HuishouOrder order = service.findOne(orderForm.getOrderid());
        if (order == null) {
            throw new SellException(ResultEnum.ORDER_NOT_EXIST);
        }
        //订单状态，-1取消订单，0新下单，1已抢单，2订单完成
        if (order.getOrderStatus() != 0) {
            throw new SellException(ResultEnum.ORDER_HAS_ROBBED);
        }

        order.setOrderStatus(OrderStatusEnum.HAS_BE_ROBBED.getCode());//设置已被抢单
        order.setRunnerId(orderForm.getOpenid());
        order.setRunnerName(orderForm.getName());
        order.setRunnerPhone(orderForm.getPhone());
        HuishouOrder result = service.create(order);
        return ResultApiUtil.success(result);
    }

    /**
     * 某一个用户的所有订单
     */
    @PostMapping("/myOrderList")
    public ResultApi<List<HuishouOrder>> list(
            @RequestParam("openid") String openid,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "100") int size) {
        if (StringUtils.isEmpty(openid)) {
            log.error("[查询订单列表] openid为空");
            throw new SellException(ResultEnum.PARAM_ERROR);
        }

        //按订单创建时间到排序，新订单在最前面
        Sort sort = new Sort(Sort.Direction.DESC, "updateTime");
        PageRequest request = new PageRequest(page, size, sort);

        Page<HuishouOrder> orderPage = service.findList(openid, request);

        return ResultApiUtil.success(orderPage.getContent());
    }


    /**
     * 查询某一个订单
     */
    @PostMapping("/oneOrder")
    public ResultApi<HuishouOrder> oneOrder(
            @RequestParam("orderid") String orderid) {
        if (StringUtils.isEmpty(orderid)) {
            log.error("[查询订单列表] 订单号不能为空");
            throw new SellException(ResultEnum.ORDER_NOT_EXIST);
        }
        HuishouOrder order = service.findOne(orderid);
        return ResultApiUtil.success(order);
    }

    /**
     * 某一个跑腿员抢的所有订单
     */
    @PostMapping("/ruunerOrderList")
    public ResultApi<List<HuishouOrder>> runerOrderlist(
            @RequestParam("runnerOpenid") String runnerOpenid,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "100") int size) {
        if (StringUtils.isEmpty(runnerOpenid)) {
            log.error("[查询订单列表] openid为空");
            throw new SellException(ResultEnum.PARAM_ERROR);
        }

        //按订单创建时间到排序，新订单在最前面
        Sort sort = new Sort(Sort.Direction.DESC, "updateTime");
        PageRequest request = new PageRequest(page, size, sort);

        Page<HuishouOrder> orderPage = service.findRunnerList(runnerOpenid, request);

        return ResultApiUtil.success(orderPage.getContent());
    }

    /**
     * 查询所有可以被抢的订单
     * 匹配城市，只展示跑腿员所在城市的订单
     */
    @PostMapping("/canRobbedOrders")
    public ResultApi<List<HuishouOrder>> canRobbedOrders(
            @RequestParam("runnerOpenid") String runnerOpenid,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "100") int size) {
        if (StringUtils.isEmpty(runnerOpenid)) {
            log.error("[查询订单列表] 跑腿员openid为空");
            throw new SellException(ResultEnum.PARAM_ERROR);
        }
        //只有注册了跑腿员，才能查看可以抢的订单
        Huishouer runner = huishouerService.findOneOpenid(runnerOpenid);
        if (runner.getType() != 2) {
            log.error("[查询可抢订单列表] 跑腿员没有审核通过");
            throw new SellException(ResultEnum.PARAM_ERROR);
        }
        String city=runner.getCity();

        //按订单创建时间到排序，新订单在最前面
        Sort sort = new Sort(Sort.Direction.DESC, "updateTime");
        PageRequest request = new PageRequest(page, size, sort);
        Page<HuishouOrder> orderPage = service.canRobbedOrders(city,request);

        List<HuishouOrder> orderList = ProtectUserUtils.protectHuishouUserOrders(orderPage.getContent());
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
        HuishouOrder order = service.findOne(orderid);
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
            if (!openid.equals(order.getBuyerOpenid())) {//只有抢单的跑腿员才能送达
                log.error("[修改订单] 您没有操作权限");
                throw new SellException(ResultEnum.USER_NO_AUTHORITY);
            }
        }


        order.setOrderStatus(orderType);
        HuishouOrder huishouOrder = service.create(order);
        return ResultApiUtil.success(huishouOrder);

    }
}
