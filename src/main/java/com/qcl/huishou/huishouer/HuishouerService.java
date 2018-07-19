package com.qcl.huishou.huishouer;

import com.qcl.huishou.bean.Huishouer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by qcl on 2018/4/27.
 */
@Service
public class HuishouerService {
    @Autowired
    private HuishouerRepository repository;

    /**
     * 查询回收商信息
     *
     * @param openid
     * @return
     */
    public Huishouer findOneOpenid(String openid) {
        return repository.findByOpenId(openid);
    }

    /**
     * 注册回收商
     *
     * @param user
     * @return
     */
    public Huishouer save(Huishouer user) {
        return repository.save(user);
    }


}
