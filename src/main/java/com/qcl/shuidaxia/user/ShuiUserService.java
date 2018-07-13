package com.qcl.shuidaxia.user;

import com.qcl.shuidaxia.bean.ShuiUser;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by qcl on 2018/7/10.
 */
@Service
public class ShuiUserService {
    @Autowired
    private ShuiUserRepository repository;

    /**
     * 创建订单
     *
     * @param user
     * @return
     */
    public ShuiUser create(ShuiUser user) {
        return repository.save(user);
    }

    /**
     * 查询单个订单
     *
     * @param userId
     * @return
     */
    public ShuiUser findByUserId(Long userId) {
        return repository.findByUserId(userId);
    }

    /**
     * 查询单个订单
     *
     * @param userPhone
     * @return
     */
    public ShuiUser findByUserPhone(String userPhone) {
        return repository.findByUserPhone(userPhone);
    }

    /**
     * 查询单个订单
     *
     * @param userAdderss
     * @return
     */
    public ShuiUser findByUserAdderss(String userAdderss) {
        return repository.findByUserAdderss(userAdderss);
    }


}
