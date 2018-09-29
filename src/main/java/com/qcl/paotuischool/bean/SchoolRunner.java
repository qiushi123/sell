package com.qcl.paotuischool.bean;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.qcl.utils.serializer.Date2StringSerializer;

import org.hibernate.annotations.DynamicUpdate;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import lombok.Data;

/**
 * Created by qcl on 2018/6/26.
 * 跑腿员
 */
@Entity
@Data
@DynamicUpdate//自动更新时间
@EntityListeners(AuditingEntityListener.class)
public class SchoolRunner {
    @Id//主键
    private String openId;
    private String name;
    private String phone;
    private String city;//城市很重要，跑腿员只能看到同城市的订单
    private String cardId;//身份证号
    private String address;//常住地址
    private int type;//-1审核失败，1审核中的跑腿员，2审核通过普通跑腿员，3培训过可以寄件跑腿员
    private String refuseDesc;//审核失败时的文案：如果是审核失败请给出失败原因
    private String formIds;//保存跑腿员的formid数组，用来做小程序推送用
    private int adminType;//66是管理员，其余都不是管理员

    @CreatedDate
    @Temporal(TemporalType.TIMESTAMP)
    @JsonSerialize(using = Date2StringSerializer.class)//用于把date类型转换为string类型
    private Date createTime;
}
