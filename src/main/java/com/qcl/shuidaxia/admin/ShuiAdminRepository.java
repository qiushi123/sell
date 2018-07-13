package com.qcl.shuidaxia.admin;

import com.qcl.shuidaxia.bean.ShuiAdmin;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by qcl on 2018/7/10.
 */
public interface ShuiAdminRepository extends JpaRepository<ShuiAdmin, Long> {
    //查询管理员信息
    ShuiAdmin findByAdminPhone(String adminPhone);
}
