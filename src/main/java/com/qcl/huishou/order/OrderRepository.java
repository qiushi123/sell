package com.qcl.huishou.order;

import com.qcl.huishou.bean.HuishouOrder;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * 类目
 * jpa用来操作bean进行数据库操作的dao
 * Created by qcl on 2017/12/16.
 */
public interface OrderRepository extends JpaRepository<HuishouOrder, String>, JpaSpecificationExecutor<HuishouOrder> {

    //用户下单列表
    Page<HuishouOrder> findByBuyerOpenid(String openid, Pageable pageable);

    //回收商回收订单列表
    Page<HuishouOrder> findByRunnerId(String openid, Pageable pageable);

    //查询一个订单
    HuishouOrder findByOrderId(String orderid);
    //    Runner findByOpenId(String openid);
}
