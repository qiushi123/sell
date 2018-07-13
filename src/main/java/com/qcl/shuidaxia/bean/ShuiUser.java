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
 */
@Entity
@Data
@DynamicUpdate//自动更新时间
@EntityListeners(AuditingEntityListener.class)
public class ShuiUser {
    //买家信息相关
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private long userId;//主键

    private String userName;
    private String userPhone;
    private String userAdderss;
    private String userFrom;//客户来源
    //套餐信息
    private String buyTime;//首次购买时间
    private String buyType;//套餐类型
    private String shoukuanren;//首次收款人
    private String shoukuanType;//收款方式
    private Integer yanjin;//水桶押金
    private Integer yinshuijiNum;//用饮水机数
    private Integer shouciPeiSongNum;//首次配送几桶
    //退订相关
    private String tuidingTime;//退订时间
    private String tuidingyuanyin;//退订原因
    private String tuidingjieguo;//退订结果
    private String beizhu;//备注

    @CreatedDate
    @Temporal(TemporalType.TIMESTAMP)
    @JsonSerialize(using = Date2StringSerializer.class)//用于把date类型转换为string类型
    private Date createTime;//配送时间
    @LastModifiedDate
    @Temporal(TemporalType.TIMESTAMP)
    @JsonSerialize(using = Date2StringSerializer.class)//用于把date类型转换为string类型
    private Date updateTime;


}
