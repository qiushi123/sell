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
public class ShuiOrder {
    @Id//主键
    private String orderId;
    //买家名
    private String buyerName;
    private String buyerPhone;
    private String buyerAdderss;
    private Long buyerId;//用户id
    //配送信息
    private String songshuixia;//送水侠
    private Integer songjitong;//送几桶
    private Integer huikongtong;//回空桶
    private Integer gongyongtong;//共用桶
    private Integer yushui;//余水
    private Integer xinmaishui;//新买水
    private String shoukuanren;//收款人
    private String beizhu;//备注
    private String time;//手动输入配送时间
    @CreatedDate
    @Temporal(TemporalType.TIMESTAMP)
    @JsonSerialize(using = Date2StringSerializer.class)//用于把date类型转换为string类型
    private Date createTime;//配送时间
    @LastModifiedDate
    @Temporal(TemporalType.TIMESTAMP)
    @JsonSerialize(using = Date2StringSerializer.class)//用于把date类型转换为string类型
    private Date updateTime;


}
