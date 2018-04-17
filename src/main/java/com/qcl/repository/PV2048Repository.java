package com.qcl.repository;

import com.qcl.dataobject.Pv2048;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * 类目
 * jpa用来操作bean进行数据库操作的dao
 * Created by qcl on 2017/12/16.
 */
public interface PV2048Repository extends JpaRepository<Pv2048, Long> {

}
