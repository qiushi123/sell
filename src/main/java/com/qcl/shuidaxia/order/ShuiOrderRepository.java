package com.qcl.shuidaxia.order;

import com.qcl.shuidaxia.bean.ShuiOrder;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Created by qcl on 2018/7/10.
 */
public interface ShuiOrderRepository extends JpaRepository<ShuiOrder, String>
        //        JpaSpecificationExecutor<ShuiOrder>
{
    //    Page<ShuiOrder> findByBuyerOpenid(String openid, Pageable pageable);

    //查询一个订单(订单id)
    ShuiOrder findByOrderId(String orderid);

    //查询多个订单(姓名)
    List<ShuiOrder> findByBuyerName(String buyerName);

    //查询一个订单列表(手机号)
    List<ShuiOrder> findByBuyerPhone(String buyerPhone, Sort sort);

    //查询一个订单列表(用户ID)
    List<ShuiOrder> findByBuyerId(Long userId, Sort sort);

    //查询一个订单(地址)
    List<ShuiOrder> findByBuyerAdderss(String buyerAdderss);
}
