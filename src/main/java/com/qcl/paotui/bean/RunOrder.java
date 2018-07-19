package com.qcl.paotui.bean;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.qcl.enums.OrderStatusEnum;
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
 * Created by qcl on 2018/7/7.
 * 跑腿订单
 */
@Entity
@Data
@DynamicUpdate//自动更新时间
@EntityListeners(AuditingEntityListener.class)
public class RunOrder {
    @Id//主键
    private String orderId;
    //买家名
    private String buyerName;
    private String buyerPhone;
    private String buyerAdderss;
    private String city;//城市很重要，跑腿员只能看到同城市的订单
    //去哪儿取
    private String fromAddress;
    //买家微信id
    private String buyerOpenid;
    //买家愿意出的价钱
    private Float orderAmount;
    //订单状态，-1取消订单，0新下单，1已抢单，2已送达，3订单完成
    private Integer orderStatus = OrderStatusEnum.NEW.getCode();

    @CreatedDate
    @Temporal(TemporalType.TIMESTAMP)
    @JsonSerialize(using = Date2StringSerializer.class)//用于把date类型转换为string类型
    private Date createTime;
    @LastModifiedDate
    @Temporal(TemporalType.TIMESTAMP)
    @JsonSerialize(using = Date2StringSerializer.class)//用于把date类型转换为string类型
    private Date updateTime;

    //抢单跑腿小哥信息
    private String runnerId;
    private String runnerName;
    private String runnerPhone;


}
