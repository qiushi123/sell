package com.qcl.paotui.runorder;

import com.qcl.paotui.bean.RunOrder;

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
public class RunnOrderService {
    @Autowired
    private RunOrderRepository repository;

    /**
     * 创建跑腿订单
     *
     * @param orderDTO
     * @return
     */
    public RunOrder create(RunOrder orderDTO) {
        return repository.save(orderDTO);
    }

    /**
     * 查询一个订单
     *
     * @param orderid
     * @return
     */
    public RunOrder findOne(String orderid) {
        return repository.findByOrderId(orderid);
    }

    /**
     * 某一个用户的所有订单
     *
     * @param openid
     * @param pageable
     * @return
     */
    public Page<RunOrder> findList(String openid, PageRequest pageable) {
        Page<RunOrder> orderMasters = repository.findByBuyerOpenid(openid, pageable);
        List<RunOrder> orderDTOS = orderMasters.getContent();
        return new PageImpl<RunOrder>(orderDTOS);
    }

    /**
     * 某一个跑腿员抢的所有订单
     *
     * @param runnerOpenid
     * @param pageable
     * @return
     */
    public Page<RunOrder> findRunnerList(String runnerOpenid, PageRequest pageable) {
        Page<RunOrder> orderMasters = repository.findByRunnerId(runnerOpenid, pageable);
        List<RunOrder> orderDTOS = orderMasters.getContent();
        return new PageImpl<RunOrder>(orderDTOS);
    }

    /**
     * 某一个跑腿员抢的所有订单
     *
     * @param city:城市很重要，跑腿员只能看到同城市的订单
     * @param pageable
     * @return
     */
    public Page<RunOrder> canRobbedOrders(String city, PageRequest pageable) {
        //查询条件构造
        Specification<RunOrder> spec = (Specification<RunOrder>) (root, query, cb) -> {
            List<Predicate> list = new ArrayList<Predicate>();
            list.add(cb.equal(root.get("orderStatus"), 0));
            list.add(cb.equal(root.get("city"), city));

            Predicate[] p = new Predicate[list.size()];
            return cb.and(list.toArray(p));
        };
        return repository.findAll(spec, pageable);
    }
}
