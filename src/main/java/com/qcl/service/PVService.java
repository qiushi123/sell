package com.qcl.service;

import com.qcl.dataobject.Pv2048;

import java.util.List;

/**
 * Created by qcl on 2018/4/13.
 * pv访问量操作
 */
public interface PVService {
    //获取当前页的访问量
    List<Pv2048> findAll();

    // 保存一次访问信息
    Pv2048 save(Pv2048 bean);
}
