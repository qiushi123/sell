package com.qcl.shuidaxia.liushui;

import com.qcl.shuidaxia.bean.ShuiLiuShui;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by qcl on 2018/7/10.
 */
@Service
public class ShuiLiushuiService {
    @Autowired
    private ShuiLiushuiRepository repository;

    /**
     * 创建流水
     *
     * @param liuShui
     * @return
     */
    public ShuiLiuShui create(ShuiLiuShui liuShui) {
        return repository.save(liuShui);
    }

    public ShuiLiuShui findOne(String orderId){
        return repository.findByOrderId(orderId);
    }


    public List<ShuiLiuShui> findListByStaffId(Long staffId, Sort sort) {
        return repository.findByStaffId(staffId, sort);
    }


}
