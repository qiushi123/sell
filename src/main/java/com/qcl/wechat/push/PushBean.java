package com.qcl.wechat.push;

import javax.persistence.Entity;
import javax.persistence.Id;

import lombok.Data;

/**
 * Created by qcl on 2018/09/27.
 * 校园帮派订单（每年900元使用费）
 * 推送给谁
 */
@Entity
@Data
public class PushBean {
    @Id//主键
    private String openid;//跑腿员的微信id
    private int type;//只有type为2和3的跑腿员才能接受推送
    private String[] formids;

    private String appid;
    private String appsecret;
    private String mobanID;//模版id
    private String mobanBeiZhu;//备注信息


}
