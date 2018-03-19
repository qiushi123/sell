package com.qcl.controller;

import com.qcl.api.ResultApi;
import com.qcl.converter.OrderForm2OrderDTOConverter;
import com.qcl.dto.OrderDTO;
import com.qcl.enums.ResultEnum;
import com.qcl.exception.SellException;
import com.qcl.form.OrderForm;
import com.qcl.service.BuyerService;
import com.qcl.service.OrderService;
import com.qcl.utils.ResultApiUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import lombok.extern.slf4j.Slf4j;

/**
 * Created by qcl on 2018/3/18.
 */
@RestController
@RequestMapping("/buyer/order")
@Slf4j
public class BuyerOrderController {
    @Autowired
    private OrderService orderService;

    @Autowired
    BuyerService buyerService;

    //创建单个订单
    @PostMapping("/create")
    /**
     * 在postman的x-www-from-urlencoded下传入下列格式的参数
     openid:qcl001
     phone:15611823564
     name:夏天
     address:杭州市临平街道
     items:[{productId:1,productQuantity:2},{productId:2,productQuantity:2}]
     */
    public ResultApi<Map<String, String>> create(@Valid OrderForm orderForm,
                                                 BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            log.error("[创建订单] 参数不正确，orderForm={}", orderForm);
            throw new SellException(ResultEnum.PARAM_ERROR.getCode()
                    , bindingResult.getFieldError().getDefaultMessage());
        }
        OrderDTO orderDTO = OrderForm2OrderDTOConverter.converter(orderForm);
        if (CollectionUtils.isEmpty(orderDTO.getOrderDetailList())) {
            log.error("[创建订单] 购物车不能为空");
            throw new SellException(ResultEnum.CART_EMPTY);
        }
        OrderDTO result = orderService.create(orderDTO);
        Map<String, String> map = new HashMap<>();
        map.put("orderID", result.getOrderId());
        return ResultApiUtil.success(ResultEnum.RESULT_OK, map);
    }

    //订单列表

    /**
     * 在postman的x-www-from-urlencoded下传入下列格式的参数
     * openid:qcl001
     * page:1
     * size:2
     */
    @PostMapping("/list")
    public ResultApi<List<OrderDTO>> list(
            @RequestParam("openid") String openid,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size) {
        if (StringUtils.isEmpty(openid)) {
            log.error("[查询订单列表] openid为空");
            throw new SellException(ResultEnum.PARAM_ERROR);
        }

        PageRequest request = new PageRequest(page, size);
        Page<OrderDTO> orderDTOPage = orderService.findList(openid, request);
        return ResultApiUtil.success(orderDTOPage.getContent());
    }


    //获取订单详情
    @PostMapping("/detail")
    public ResultApi<OrderDTO> detail(
            @RequestParam("openid") String openid,
            @RequestParam("orderId") String orderId
    ) {
        OrderDTO result = buyerService.findOrderOne(openid, orderId);
        return ResultApiUtil.success(result);

    }

    //取消订单
    @PostMapping("/cancel")
    public ResultApi cancel(
            @RequestParam("openid") String openid,
            @RequestParam("orderId") String orderId) {
        OrderDTO orderDTO = buyerService.cancelOrder(openid, orderId);
        return ResultApiUtil.success(null);

    }

}
