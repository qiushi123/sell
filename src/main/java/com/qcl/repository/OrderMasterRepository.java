package com.qcl.repository;

import com.qcl.dataobject.OrderMaster;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by qcl on 2018/3/13.
 */
public interface OrderMasterRepository
        extends JpaRepository<OrderMaster, String> {
    //按照用户微信id，分页查询用户订单
    Page<OrderMaster> findByBuyerOpenid(String buyerOpenid, Pageable pageable);
}
