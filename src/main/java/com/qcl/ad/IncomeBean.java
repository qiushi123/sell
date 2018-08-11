package com.qcl.ad;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.qcl.utils.serializer.Date2StringSerializer;

import org.springframework.data.annotation.CreatedDate;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import lombok.Data;

/**
 * Created by qcl on 2018/6/26.
 * 广告收入及红包分配bean
 */
@Entity
@Data
public class IncomeBean {
    @Id//主键
    private Long id;

    @CreatedDate
    @Temporal(TemporalType.TIMESTAMP)
    @JsonSerialize(using = Date2StringSerializer.class)//用于把date类型转换为string类型
    private Date createTime;

    private Float tongchengMoney;
    private Float huishouMoney;
    private Float daqiqiuMoney;

    private String bigGroup;
    private String smallGroup;
    private String pingjun;

}
