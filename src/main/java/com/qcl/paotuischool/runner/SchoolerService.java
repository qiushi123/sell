package com.qcl.paotuischool.runner;

import com.qcl.paotuischool.bean.SchoolRunner;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.criteria.Predicate;

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
     * 查询跑腿员列表
     */
    public List<SchoolRunner> findAllRunner(int runnerType) {
        //查询条件构造
        Specification<SchoolRunner> spec = (Specification<SchoolRunner>) (root, query, cb) -> {
            List<Predicate> list = new ArrayList<>();
            if (runnerType == 1) {//待审核
                list.add(cb.equal(root.get("type"), 1));
            } else {//已审核
                list.add(cb.notEqual(root.get("type"), 1));
            }

            Predicate[] p = new Predicate[list.size()];
            return cb.and(list.toArray(p));
        };
        return repository.findAll(spec);
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

    /*
    * 获取所有通过审核的跑腿员，用户推送用
    * */
    //查询条件构造
    public List<SchoolRunner> findAll() {
        //查询条件构造
        Specification<SchoolRunner> spec = (Specification<SchoolRunner>) (root, query, cb) -> {
            List<Predicate> list = new ArrayList<>();
            //大于等于2，代表审核通过的跑腿员
            list.add(cb.greaterThanOrEqualTo(root.get("type"), 2));
            list.add(cb.isNotNull(root.get("formIds")));//formids不为空


            Predicate[] p = new Predicate[list.size()];
            return cb.and(list.toArray(p));
        };
        return repository.findAll(spec);
    }

}
