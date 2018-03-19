package com.qcl.service;

import com.qcl.dto.OrderDTO;

/**
 * Created by qcl on 2018/3/19.
 * 买家相关的服务
 */
public interface BuyerService {
    /**
     * 查询订单
     *
     * @param openid
     * @param orderid
     * @return
     */
    OrderDTO findOrderOne(String openid, String orderid);

    //取消订单
    OrderDTO cancelOrder(String openid, String orderid);
}
