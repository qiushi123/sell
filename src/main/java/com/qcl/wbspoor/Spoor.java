package com.qcl.wbspoor;

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
 * Created by qcl on 2018/9/7.
 * 足迹对应的数据库bean
 */
@Entity
@Data
@DynamicUpdate//自动更新时间
@EntityListeners(AuditingEntityListener.class)
public class Spoor {
    @Id//主键
    @GeneratedValue(strategy = GenerationType.IDENTITY)//自动生成
    private Long spoorid;//足迹id

    private Long userid;//用户id

    @JsonSerialize(using = Date2StringSerializer.class)//用于把date类型转换为string类型
    private Date startTime;//开始时间
    @JsonSerialize(using = Date2StringSerializer.class)//用于把date类型转换为string类型
    private Date endTime;//结束时间

    private String startLatitude;
    private String startlongitude;

    private String endLatitude;
    private String endlongitude;


    private String duration;//时长

    private String lenght;//长度
    private String city;//所在城市
    private String message;//附加信息（文字描述）
    private String year;//某一年

    @CreatedDate
    @Temporal(TemporalType.TIMESTAMP)
    @JsonSerialize(using = Date2StringSerializer.class)//用于把date类型转换为string类型
    private Date createTime;
    @LastModifiedDate
    @Temporal(TemporalType.TIMESTAMP)
    @JsonSerialize(using = Date2StringSerializer.class)//用于把date类型转换为string类型
    private Date updateTime;

}
