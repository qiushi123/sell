package com.qcl.repository;

import com.qcl.dataobject.OrderDetail;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Created by qcl on 2018/3/13.
 */
public interface OrderDetailRepository extends JpaRepository<OrderDetail, String> {
    List<OrderDetail> findByOrderId(String orderId);
}
