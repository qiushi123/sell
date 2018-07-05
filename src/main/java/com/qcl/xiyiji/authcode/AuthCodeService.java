package com.qcl.xiyiji.authcode;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by qcl on 2018/4/27.
 */
@Service
public class AuthCodeService {
    @Autowired
    private AuthCodeRepository repository;

    /**
     * @param openid
     * @return
     */
    public AuthCodeBean findOneOpenid(String openid) {
        return repository.findByOpenId(openid);
    }

    public AuthCodeBean findOneByCodeId(String code) {
        return repository.findByCodeId(code);
    }

    public AuthCodeBean save(AuthCodeBean user) {
        return repository.save(user);
    }

    public List<AuthCodeBean> findAll(){
        return repository.findAll();
    }
}
