package com.qcl.wb_xiaoyuanbangpai.order;

import com.qcl.wb_xiaoyuanbangpai.bean.BangPaiOrder;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * 类目
 * jpa用来操作bean进行数据库操作的dao
 * Created by qcl on 2017/12/16.
 */
public interface BangPaiOrderRepository extends JpaRepository<BangPaiOrder, String>, JpaSpecificationExecutor<BangPaiOrder> {
    Page<BangPaiOrder> findByOpenid(String openid, Pageable pageable);

    Page<BangPaiOrder> findByRunnerId(String openid, Pageable pageable);

    //查询一个订单
    BangPaiOrder findByOrderId(String orderid);
}
