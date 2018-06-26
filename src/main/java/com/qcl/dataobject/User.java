package com.qcl.dataobject;

import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.Entity;
import javax.persistence.Id;

import lombok.Data;

/**
 * Created by qcl on 2018/6/26.
 * 用户相关的bean
 */
@Entity
@DynamicUpdate
@Data//避免重复写get和set，tostring
public class User {
    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String openid;

    private String name;
    private String password;
    private String phone;
}
