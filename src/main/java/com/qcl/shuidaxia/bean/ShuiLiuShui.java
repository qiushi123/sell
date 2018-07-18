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
 * 员工流水表
 */
@Entity
@Data
@DynamicUpdate//自动更新时间
@EntityListeners(AuditingEntityListener.class)
public class ShuiLiuShui {
    @Id//主键
    private String orderId;
    //买家名
    private Long staffId;//员工id
    private String staffName;//员工姓名
    //流水信息
    private String time;//手动输入配送时间
    private String shoukuanleibie;//收款类别
    private Integer shoukuanjine;//收款金额
    private String zhichuleibie;//支出类别
    private Integer zhichujine;//支出金额
    private String yewutichengxinxi;//业务提成信息
    private Integer tichengjine;//提成金额
    private String qingjia;//请假
    private Integer fakuan;//交罚款
    private Integer dixin;//底薪
    @CreatedDate
    @Temporal(TemporalType.TIMESTAMP)
    @JsonSerialize(using = Date2StringSerializer.class)//用于把date类型转换为string类型
    private Date createTime;//配送时间
    @LastModifiedDate
    @Temporal(TemporalType.TIMESTAMP)
    @JsonSerialize(using = Date2StringSerializer.class)//用于把date类型转换为string类型
    private Date updateTime;


}
