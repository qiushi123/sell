package com.qcl.huishou.order;

import com.qcl.huishou.bean.HuishouOrder;

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
public class OrderService {
    @Autowired
    private OrderRepository repository;

    /**
     * 创建跑腿订单
     *
     * @param orderDTO
     * @return
     */
    public HuishouOrder create(HuishouOrder orderDTO) {
        return repository.save(orderDTO);
    }

    /**
     * 查询一个订单
     *
     * @param orderid
     * @return
     */
    public HuishouOrder findOne(String orderid) {
        return repository.findByOrderId(orderid);
    }

    /**
     * 某一个用户的所有订单
     *
     * @param openid
     * @param pageable
     * @return
     */
    public Page<HuishouOrder> findList(String openid, PageRequest pageable) {
        Page<HuishouOrder> orderMasters = repository.findByBuyerOpenid(openid, pageable);
        List<HuishouOrder> orderDTOS = orderMasters.getContent();
        return new PageImpl<HuishouOrder>(orderDTOS);
    }

    /**
     * 某一个跑腿员抢的所有订单
     *
     * @param runnerOpenid
     * @param pageable
     * @return
     */
    public Page<HuishouOrder> findRunnerList(String runnerOpenid, PageRequest pageable) {
        Page<HuishouOrder> orderMasters = repository.findByRunnerId(runnerOpenid, pageable);
        List<HuishouOrder> orderDTOS = orderMasters.getContent();
        return new PageImpl<HuishouOrder>(orderDTOS);
    }

    /**
     * 某一个跑腿员抢的所有订单
     *
     * @param city:城市很重要，跑腿员只能看到同城市的订单
     * @param pageable
     * @return
     */
    public Page<HuishouOrder> canRobbedOrders(String city, PageRequest pageable) {
        //查询条件构造
        Specification<HuishouOrder> spec = (Specification<HuishouOrder>) (root, query, cb) -> {
            List<Predicate> list = new ArrayList<Predicate>();
            list.add(cb.equal(root.get("orderStatus"), 0));
            list.add(cb.equal(root.get("city"), city));

            Predicate[] p = new Predicate[list.size()];
            return cb.and(list.toArray(p));
        };
        return repository.findAll(spec, pageable);
    }
}
