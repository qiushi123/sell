package com.qcl.paotui.bean;

import javax.persistence.Entity;
import javax.persistence.Id;

import lombok.Data;

/**
 * Created by qcl on 2018/6/26.
 * 跑腿员
 */
@Entity
@Data
public class Runner {
    @Id//主键
    private String openId;
    private String name;
    private String phone;
    private String city;//城市很重要，跑腿员只能看到同城市的订单
    private String cardId;//身份证号
    private String address;//常住地址
    private int type;//-1审核失败，1审核中的跑腿员，2审核通过
    private String refuseDesc;//审核失败时的文案：如果是审核失败请给出失败原因
}
