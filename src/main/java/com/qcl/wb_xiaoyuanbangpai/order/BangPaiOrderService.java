package com.qcl.wb_xiaoyuanbangpai.order;


import com.qcl.wb_xiaoyuanbangpai.bean.BangPaiOrder;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.criteria.Predicate;

/**
 * Created by qcl on 2018/4/27.
 */
@Service
public class BangPaiOrderService {
    @Autowired
    private BangPaiOrderRepository repository;

    /**
     * 创建跑腿订单
     *
     * @param orderDTO
     * @return
     */
    public BangPaiOrder create(BangPaiOrder orderDTO) {
        return repository.save(orderDTO);
    }

    /**
     * 查询一个订单
     *
     * @param orderid
     * @return
     */
    public BangPaiOrder findOne(String orderid) {
        return repository.findByOrderId(orderid);
    }

    /**
     * 某一个用户的所有订单
     *
     * @param openid
     * @param pageable
     * @return
     */
    public Page<BangPaiOrder> findList(String openid, PageRequest pageable) {
        Page<BangPaiOrder> orderMasters = repository.findByOpenid(openid, pageable);
        List<BangPaiOrder> orderDTOS = orderMasters.getContent();
        return new PageImpl<BangPaiOrder>(orderDTOS);
    }

    /**
     * 某一个跑腿员抢的所有订单
     * runnerOpenid
     * runnerOrderType: 0新抢到待取件，1已取件，2已送达
     */
    public Page<BangPaiOrder> findRunnerList(String runnerOpenid, int runnerOrderType, PageRequest
            pageable) {
        //订单状态 -1取消订单，0新下单待抢单，1已被抢单，2已取到，3已送达，4客户确认收货
        Specification<BangPaiOrder> spec = (Specification<BangPaiOrder>) (root, query, cb) -> {
            List<Predicate> list = new ArrayList<Predicate>();
            if (runnerOrderType == 2) {//已送达
                list.add(cb.greaterThanOrEqualTo(root.get("orderStatus"), 3));//大于等于3代表已送达
            } else if (runnerOrderType == 1) {//已取到件
                list.add(cb.equal(root.get("orderStatus"), 2));
            } else {//新抢到待取件
                list.add(cb.equal(root.get("orderStatus"), 1));
            }
            list.add(cb.equal(root.get("runnerId"), runnerOpenid));
            Predicate[] p = new Predicate[list.size()];
            return cb.and(list.toArray(p));
        };
        return repository.findAll(spec, pageable);
        //        Page<BangPaiOrder> orderMasters = repository.findAll(spec, pageable);
        //        List<BangPaiOrder> orderDTOS = orderMasters.getContent();
        //        return new PageImpl<BangPaiOrder>(orderDTOS);
    }

    /**
     * 所有可以抢的订单
     * orderType;//0代取快递，1代寄快递
     * payStatus=1支付成功
     *
     * @param pageable
     * @return
     */
    public Page<BangPaiOrder> canRobbedOrders(int orderType, PageRequest pageable) {
        //查询条件构造
        Specification<BangPaiOrder> spec = (Specification<BangPaiOrder>) (root, query, cb) -> {
            List<Predicate> list = new ArrayList<Predicate>();
            list.add(cb.equal(root.get("orderStatus"), 0));
            list.add(cb.equal(root.get("payStatus"), 1));
            if (orderType == 1) {
                list.add(cb.equal(root.get("orderType"), 1));
            } else {
                list.add(cb.equal(root.get("orderType"), 0));
            }
            Predicate[] p = new Predicate[list.size()];
            return cb.and(list.toArray(p));
        };
        return repository.findAll(spec, pageable);
    }
}
