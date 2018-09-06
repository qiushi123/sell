package com.qcl.paotuischool.runorder;

import com.qcl.paotuischool.bean.RunSchoolOrder;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * 类目
 * jpa用来操作bean进行数据库操作的dao
 * Created by qcl on 2017/12/16.
 */
public interface SchoolOrderRepository extends JpaRepository<RunSchoolOrder, String>, JpaSpecificationExecutor<RunSchoolOrder> {
    Page<RunSchoolOrder> findByOpenid(String openid, Pageable pageable);

    Page<RunSchoolOrder> findByRunnerId(String openid, Pageable pageable);

    //查询一个订单
    RunSchoolOrder findByOrderId(String orderid);
}
