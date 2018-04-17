package com.qcl.service.impl;

import com.qcl.dataobject.Pv2048;
import com.qcl.repository.PV2048Repository;
import com.qcl.service.PVService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 类目
 * Created by qcl on 2017/12/16.
 */
@Service
public class PVServiceImpl implements PVService {

    @Autowired
    private PV2048Repository repository;

    @Override
    public List<Pv2048> findAll() {
        return repository.findAll();
    }

    @Override
    public Pv2048 save(Pv2048 bean) {
        return repository.save(bean);
    }
}
