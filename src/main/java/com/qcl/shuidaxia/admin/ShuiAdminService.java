package com.qcl.shuidaxia.admin;

import com.qcl.shuidaxia.bean.ShuiAdmin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by qcl on 2018/7/10.
 */
@Service
public class ShuiAdminService {
    @Autowired
    private ShuiAdminRepository repository;

    /**
     * 创建管理员
     *
     * @param user
     * @return
     */
    public ShuiAdmin create(ShuiAdmin user) {
        return repository.save(user);
    }


    /**
     * 查询管理员信息
     *
     * @param userPhone
     * @return
     */
    public ShuiAdmin findByUserPhone(String userPhone) {
        return repository.findByAdminPhone(userPhone);
    }


}
