package com.qcl.paotuischool.runner;

import com.qcl.paotuischool.bean.SchoolRunner;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by qcl on 2018/4/27.
 */
@Service
public class SchoolerService {
    @Autowired
    private SchoolerRepository repository;

    /**
     * 查询跑腿员信息
     *
     * @param openid
     * @return
     */
    public SchoolRunner findOneOpenid(String openid) {
        return repository.findByOpenId(openid);
    }

    /**
     * 注册跑腿员
     *
     * @param user
     * @return
     */
    public SchoolRunner save(SchoolRunner user) {
        return repository.save(user);
    }


}
