package com.qcl.paotuischool.bean;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.qcl.enums.OrderStatusEnum;
import com.qcl.enums.OrderTypeEnum;
import com.qcl.enums.PayStatusEnum;
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
 * 校园跑腿订单
 */
@Entity
@Data
@DynamicUpdate//自动更新时间
@EntityListeners(AuditingEntityListener.class)
public class RunSchoolOrder {
    @Id//主键
    private String orderId;


    //买家微信id
    private String openid;
    //必传
    private String school;//那个学校
    private String name;
    private String phone;
    private String address;
    private String fromAddress; //去哪儿取和物品描述
    private String noteContent;// 取件短信
    private Integer expressType;//物品类型，1小件，2中件，3大件，4寄快递
    private Float totalMoney;//总费用


    private boolean isJiaJi;//是否加急
    private boolean isShangLou;//是否送上楼
    private String yundanhao;//运单号
    private String songdaTime;//期望送达时间
    private String beizhu;

    //0代取快递，1代寄快递
    private Integer orderType = OrderTypeEnum.TAKE.getCode();
    //订单状态，-1取消订单并退款，0新下单，1已抢单，2已送达，3订单完成
    private Integer orderStatus = OrderStatusEnum.NEW.getCode();
    //-1已退款，0等待支付，1支付完成
    private Integer payStatus = PayStatusEnum.WAIT.getCode();

    
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
