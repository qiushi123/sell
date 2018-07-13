package com.qcl.shuidaxia.order;

import com.qcl.shuidaxia.bean.ShuiOrder;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by qcl on 2018/7/10.
 */
@Service
public class ShuiOrderService {
    @Autowired
    private ShuiOrderRepository repository;

    /**
     * 创建订单
     *
     * @param order
     * @return
     */
    public ShuiOrder create(ShuiOrder order) {
        return repository.save(order);
    }

    /**
     * 查询单个订单
     *
     * @param orderid
     * @return
     */
    public ShuiOrder findByOrderid(String orderid) {
        return repository.findByOrderId(orderid);
    }

    /**
     * 根据用户手机号查询用户所有订单
     *
     * @param buyerPhone
     * @param sort
     * @return
     */
    public List<ShuiOrder> findListByBuyerPhone(String buyerPhone, Sort sort) {
        return repository.findByBuyerPhone(buyerPhone, sort);
    }

    /**
     * 根据用户姓名查询用户所有订单
     *
     * @param buyerName
     * @return
     */
    public List<ShuiOrder> findListByBuyerUserId(Long userid, Sort sort) {
        return repository.findByBuyerId(userid, sort);
    }

    /**
     * 根据用户地址查询用户所有订单
     *
     * @param buyerAdderss
     * @return
     */
    public List<ShuiOrder> findListByBuyerAdderss(String buyerAdderss) {
        return repository.findByBuyerAdderss(buyerAdderss);
    }


}
