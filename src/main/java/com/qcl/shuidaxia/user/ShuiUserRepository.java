package com.qcl.shuidaxia.user;

import com.qcl.shuidaxia.bean.ShuiUser;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by qcl on 2018/7/10.
 */
public interface ShuiUserRepository extends JpaRepository<ShuiUser, Long>
        //        JpaSpecificationExecutor<ShuiOrder>
{
    //    Page<ShuiOrder> findByBuyerOpenid(String openid, Pageable pageable);

    //查询会员信息
    ShuiUser findByUserId(Long userId);//会员id

    ShuiUser findByUserPhone(String userPhone);//会员电话

    ShuiUser findByUserAdderss(String userAdderss);//会员地址
}
