package com.qcl.paotui.runner;

import com.qcl.paotui.bean.Runner;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by qcl on 2018/4/27.
 */
@Service
public class RunnerService {
    @Autowired
    private RunnerRepository repository;

    /**
     * 查询跑腿员信息
     *
     * @param openid
     * @return
     */
    public Runner findOneOpenid(String openid) {
        return repository.findByOpenId(openid);
    }

    /**
     * 注册跑腿员
     *
     * @param user
     * @return
     */
    public Runner save(Runner user) {
        return repository.save(user);
    }


}
