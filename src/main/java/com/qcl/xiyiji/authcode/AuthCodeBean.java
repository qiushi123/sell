package com.qcl.xiyiji.authcode;

import javax.persistence.Entity;
import javax.persistence.Id;

import lombok.Data;

/**
 * Created by qcl on 2018/6/26.
 * 验证码／优惠券bean
 */
@Entity
@Data
public class AuthCodeBean {
    @Id//主键
    private String codeId;

    private String openId;
    private String name;
    private String phone;
}
