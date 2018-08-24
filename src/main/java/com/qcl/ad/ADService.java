package com.qcl.ad;

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
public class ADService {
    @Autowired
    private InComeRepository repository;

    //每天点广告统计（旧系统）
    @Autowired
    private AdClickNumRepository adClickNumRepository;

    //每周点广告统计（新系统）
    @Autowired
    private AdClickWeekNumRepository adClickWeekNumRepository;

    /**
     * 查询广告收入列表
     */
    public List<IncomeBean> findAll(Sort sort) {
        return repository.findAll(sort);
    }

    /**
     * 查询某一周的广告收入
     */
    public IncomeBean findShouruOneWeek(String weekTime) {
        return repository.findByWeekTime(weekTime);
    }

    //查询每天的点击排名
    public List<AdClickBean> findClickList(String dateTime, Sort sort) {
        return adClickNumRepository.findAllByDateTime(dateTime, sort);

    }

    //查询每周的点击排名
    public List<AdClickWeekBean> findClickWeekList(String weekTime, Sort sort) {
        return adClickWeekNumRepository.findAllByWeekTime(weekTime, sort);

    }

    //查询用户今天是否已经点击过了
    public List<AdClickBean> findHasExist(String dateTime, String name, String openid) {
        //查询条件构造
        Specification<AdClickBean> spec = (Specification<AdClickBean>) (root, query, cb) -> {
            List<Predicate> list = new ArrayList<>();
            list.add(cb.equal(root.get("dateTime"), dateTime));
            list.add(cb.equal(root.get("name"), name));
            //            list.add(cb.equal(root.get("openid"), openid));

            Predicate[] p = new Predicate[list.size()];
            return cb.and(list.toArray(p));
        };
        return adClickNumRepository.findAll(spec);

    }

    //查询用户本周是否已经点击过了
    public List<AdClickWeekBean> findWeekHasExist(String weekTime, String name) {
        //查询条件构造
        Specification<AdClickWeekBean> spec = (Specification<AdClickWeekBean>) (root, query, cb) -> {
            List<Predicate> list = new ArrayList<>();
            list.add(cb.equal(root.get("weekTime"), weekTime));
            list.add(cb.equal(root.get("name"), name));
            //            list.add(cb.equal(root.get("openid"), openid));

            Predicate[] p = new Predicate[list.size()];
            return cb.and(list.toArray(p));
        };
        return adClickWeekNumRepository.findAll(spec);

    }


    //更新用户点击量，或者保存用户第一次点击量
    public AdClickBean save(AdClickBean bean) {
        return adClickNumRepository.save(bean);

    }

    //更新用户每周点击量，或者保存用户第一次点击量
    public AdClickWeekBean saveWeek(AdClickWeekBean bean) {
        return adClickWeekNumRepository.save(bean);

    }


}
