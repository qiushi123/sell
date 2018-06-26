package com.qcl.shuidaxia.service;

import com.qcl.dataobject.User;

/**
 * Created by qcl on 2018/4/27.
 */
public interface UserService {
    //获取所有文章
    //    List<Article> findAll();

    //根据openid获取用户信息
    User findOne(String openid);

    User save(User user);
}
