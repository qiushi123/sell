package com.qcl.shuidaxia.bean;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.qcl.utils.serializer.Date2StringSerializer;

import org.hibernate.annotations.DynamicUpdate;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import lombok.Data;

/**
 * Created by qcl on 2018/7/10.
 * 管理员
 */
@Entity
@Data
@DynamicUpdate//自动更新时间
@EntityListeners(AuditingEntityListener.class)
public class ShuiAdmin {
    //买家信息相关
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long adminId;//主键

    private String adminName;
    private String adminPhone;
    private String adminPassword;
    private Integer adminType;//-1不具备管理员权限，0正常管理员

    @CreatedDate
    @Temporal(TemporalType.TIMESTAMP)
    @JsonSerialize(using = Date2StringSerializer.class)//用于把date类型转换为string类型
    private Date createTime;//配送时间
    @LastModifiedDate
    @Temporal(TemporalType.TIMESTAMP)
    @JsonSerialize(using = Date2StringSerializer.class)//用于把date类型转换为string类型
    private Date updateTime;


}
