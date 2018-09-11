package com.qcl.wbspoor;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * 类目
 * jpa用来操作bean进行数据库操作的dao
 * Created by qcl on 2017/12/16.
 */
public interface Repository extends JpaRepository<Spoor, Long>, JpaSpecificationExecutor<Spoor> {
        Spoor findBySpoorid(Long spoorid);

}
