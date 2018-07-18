package com.qcl.shuidaxia.liushui;

import com.qcl.shuidaxia.bean.ShuiLiuShui;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Created by qcl on 2018/7/10.
 */
public interface ShuiLiushuiRepository extends JpaRepository<ShuiLiuShui, String>
        //        JpaSpecificationExecutor<ShuiOrder>
{
    //    Page<ShuiOrder> findByBuyerOpenid(String openid, Pageable pageable);

    ShuiLiuShui findByOrderId(String orderId);
    //查询一个员工流水列表(员工ID)
    List<ShuiLiuShui> findByStaffId(Long staffId, Sort sort);

}
