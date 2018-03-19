package com.qcl.service.impl;

import com.qcl.dto.OrderDTO;
import com.qcl.enums.ResultEnum;
import com.qcl.exception.SellException;
import com.qcl.service.BuyerService;
import com.qcl.service.OrderService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

/**
 * Created by qcl on 2018/3/19.
 * 买家相关服务的实现类
 */
@Service
@Slf4j
public class BuyerServiceImpl implements BuyerService {

    @Autowired
    private OrderService orderService;

    @Override
    public OrderDTO findOrderOne(String openid, String orderid) {
        return checkOrderOwner(openid, orderid);
    }

    @Override
    public OrderDTO cancelOrder(String openid, String orderid) {
        OrderDTO orderDTO = checkOrderOwner(openid, orderid);
        if (orderDTO == null) {
            throw new SellException(ResultEnum.ORDER_NOT_EXIST);
        }
        return orderService.cancel(orderDTO);
    }

    private OrderDTO checkOrderOwner(String openid, String orderid) {
        OrderDTO orderDTO = orderService.findOne(orderid);
        if (orderDTO == null) {
            return null;
        }

        if (!orderDTO.getBuyerOpenid().equals(openid)) {
            log.error("[查询订单] 查询的用户不匹配 orderid={}", orderid);
            throw new SellException(ResultEnum.ORDER_OPENID_ERROR);
        }
        return orderDTO;

    }
}
