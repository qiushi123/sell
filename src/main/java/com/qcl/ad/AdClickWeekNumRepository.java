package com.qcl.ad;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

/**
 * 类目
 * jpa用来操作bean进行数据库操作的dao
 * Created by qcl on 2017/12/16.
 */
public interface AdClickWeekNumRepository extends JpaRepository<AdClickWeekBean, Long>
        , JpaSpecificationExecutor<AdClickWeekBean> {

    //点击排名列表
    List<AdClickWeekBean> findAllByWeekTime(String weekTime, Sort sort);

}
