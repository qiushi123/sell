package com.qcl.huishou.huishouer;

import com.qcl.huishou.bean.Huishouer;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * 类目
 * jpa用来操作bean进行数据库操作的dao
 * Created by qcl on 2017/12/16.
 */
public interface HuishouerRepository extends JpaRepository<Huishouer, String> {
    Huishouer findByOpenId(String openid);
}
