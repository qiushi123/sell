package com.qcl.ad;

import javax.persistence.Entity;
import javax.persistence.Id;

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

//    @CreatedDate
//    @Temporal(TemporalType.TIMESTAMP)
//    @JsonSerialize(using = Date2StringSerializer.class)//用于把date类型转换为string类型
//    private Date createTime;

    private String weekTime;
    private Float tongchengMoney;
    private Float huishouMoney;
    private Float xiaoyuanMoney;
    private Float bianchengMoney;
    private Float daqiqiuMoney;//打泡泡和飞机大战
    private int fengexian;
    //    private Float allMoney;//总计

}
