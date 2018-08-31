package com.qcl.wb_dami_ad.ad;

import com.qcl.ad.AdClickWeekNumRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.criteria.Predicate;

/**
 * Created by qcl on 2018/4/27.
 */
@Service
public class WbADService {

    //每天点广告统计（旧系统）
    @Autowired
    private WbAdClickNumRepository wbAdClickNumRepository;

    //每周点广告统计（新系统）
    @Autowired
    private AdClickWeekNumRepository adClickWeekNumRepository;


    //查询每天的点击排名
    public List<WbAdClickBean> findClickList(String dateTime, Sort sort) {
        return wbAdClickNumRepository.findAllByDateTime(dateTime, sort);
    }


    //查询用户今天是否已经点击过了
    public List<WbAdClickBean> findHasExist(String dateTime, String name) {
        //查询条件构造
        Specification<WbAdClickBean> spec = (Specification<WbAdClickBean>) (root, query, cb) -> {
            List<Predicate> list = new ArrayList<>();
            list.add(cb.equal(root.get("dateTime"), dateTime));
            list.add(cb.equal(root.get("name"), name));

            Predicate[] p = new Predicate[list.size()];
            return cb.and(list.toArray(p));
        };
        return wbAdClickNumRepository.findAll(spec);

    }


    //更新用户点击量，或者保存用户第一次点击量
    public WbAdClickBean save(WbAdClickBean bean) {
        return wbAdClickNumRepository.save(bean);

    }


}
