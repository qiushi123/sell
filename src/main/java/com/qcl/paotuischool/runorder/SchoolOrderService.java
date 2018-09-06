package com.qcl.paotuischool.runorder;

import com.qcl.paotuischool.bean.RunSchoolOrder;

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
public class SchoolOrderService {
    @Autowired
    private SchoolOrderRepository repository;

    /**
     * 创建跑腿订单
     *
     * @param orderDTO
     * @return
     */
    public RunSchoolOrder create(RunSchoolOrder orderDTO) {
        return repository.save(orderDTO);
    }

    /**
     * 查询一个订单
     *
     * @param orderid
     * @return
     */
    public RunSchoolOrder findOne(String orderid) {
        return repository.findByOrderId(orderid);
    }

    /**
     * 某一个用户的所有订单
     *
     * @param openid
     * @param pageable
     * @return
     */
    public Page<RunSchoolOrder> findList(String openid, PageRequest pageable) {
        Page<RunSchoolOrder> orderMasters = repository.findByOpenid(openid, pageable);
        List<RunSchoolOrder> orderDTOS = orderMasters.getContent();
        return new PageImpl<RunSchoolOrder>(orderDTOS);
    }

    /**
     * 某一个跑腿员抢的所有订单
     *
     * @param runnerOpenid
     * @param isOk         :是否完成订单
     * @return
     */
    public Page<RunSchoolOrder> findRunnerList(String runnerOpenid, boolean isOk, PageRequest
            pageable) {
        //1已抢单，2已送达，3订单完成
        Specification<RunSchoolOrder> spec = (Specification<RunSchoolOrder>) (root, query, cb) -> {
            List<Predicate> list = new ArrayList<Predicate>();
            if (isOk) {
                list.add(cb.greaterThanOrEqualTo(root.get("orderStatus"), 2));//大于等于2
            } else {
                list.add(cb.equal(root.get("orderStatus"), 1));
            }
            list.add(cb.equal(root.get("openid"), runnerOpenid));
            Predicate[] p = new Predicate[list.size()];
            return cb.and(list.toArray(p));
        };
        Page<RunSchoolOrder> orderMasters = repository.findAll(spec, pageable);
        List<RunSchoolOrder> orderDTOS = orderMasters.getContent();
        return new PageImpl<RunSchoolOrder>(orderDTOS);
    }

    /**
     * 所有可以抢的订单
     *
     * @param pageable
     * @return
     */
    public Page<RunSchoolOrder> canRobbedOrders(PageRequest pageable) {
        //查询条件构造
        Specification<RunSchoolOrder> spec = (Specification<RunSchoolOrder>) (root, query, cb) -> {
            List<Predicate> list = new ArrayList<Predicate>();
            list.add(cb.equal(root.get("orderStatus"), 0));

            Predicate[] p = new Predicate[list.size()];
            return cb.and(list.toArray(p));
        };
        return repository.findAll(spec, pageable);
    }
}
