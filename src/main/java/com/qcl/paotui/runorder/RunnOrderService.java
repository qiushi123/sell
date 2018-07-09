package com.qcl.paotui.runorder;

import com.qcl.paotui.bean.RunOrder;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

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
     * @param pageable
     * @return
     */
    public Page<RunOrder> canRobbedOrders(PageRequest pageable) {
        //查询条件构造
        Specification<RunOrder> spec = (Specification<RunOrder>) (root, query, cb) -> {
            Predicate p3 = cb.equal(root.get("orderStatus"), 0);
            return cb.and(p3);
            // 下面方式使用JPA的API设置了查询条件，所以不需要再返回查询条件Predicate给Spring Data Jpa，
            // 故最后return null;即可。
            //query.where(p);
            //return  null;
        };
        return repository.findAll(spec, pageable);
    }
}
