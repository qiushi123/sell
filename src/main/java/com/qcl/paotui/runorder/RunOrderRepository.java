package com.qcl.paotui.runorder;

import com.qcl.paotui.bean.RunOrder;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * 类目
 * jpa用来操作bean进行数据库操作的dao
 * Created by qcl on 2017/12/16.
 */
public interface RunOrderRepository extends JpaRepository<RunOrder, String>, JpaSpecificationExecutor<RunOrder> {
    Page<RunOrder> findByBuyerOpenid(String openid, Pageable pageable);

    Page<RunOrder> findByRunnerId(String openid, Pageable pageable);

    //查询一个订单
    RunOrder findByOrderId(String orderid);
    //    Runner findByOpenId(String openid);
}
