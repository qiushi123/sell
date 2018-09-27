package com.qcl.wb_xiaoyuanbangpai.bean;

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
 * Created by qcl on 2018/09/27.
 * 校园帮派订单（每年900元使用费）
 */
@Entity
@Data
@DynamicUpdate//自动更新时间
@EntityListeners(AuditingEntityListener.class)
public class BangPaiOrder {
    @Id//主键
    private String orderId;


    //买家微信id
    private String openid;
    //支付时，微信返回的用于推送的id，7天内可以推送3次
    private String prepay_id;
    //必传
    private String school;//那个学校
    private String name;
    private String phone;
    private String address;
    private String fromAddress; //去那家快递公司取，如果是寄就是上门地址
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
    private String company;//用那家快递公司寄件

    //订单状态 -1取消订单，0新下单待抢单，1已被抢单，2已取到，3已送达，4客户确认收货
    private Integer orderStatus = OrderStatusEnum.NEW.getCode();
    //支付状态 -2已退款，-1已申请退款，0等待支付，1支付完成
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
