package com.qcl.dataobject;

import com.qcl.enums.OrderStatusEnum;
import com.qcl.enums.PayStatusEnum;

import org.hibernate.annotations.DynamicUpdate;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;

import lombok.Data;

/**
 * Created by qcl on 2018/3/13.
 * 订单信息
 */
@Entity
@Data
@DynamicUpdate//自动更新时间
public class OrderMaster {

    @Id//主键
    private String orderId;
    //买家名
    private String buyerName;
    private String buyerPhone;
    private String buyerAdderss;
    //买家微信id
    private String buyerOpenid;
    //订单总金额
    private BigDecimal orderAmount;
    //订单状态，默认为新下单
    private Integer orderStatus = OrderStatusEnum.NEW.getCode();
    //支付状态，默认是0等待支付
    private Integer payStatus = PayStatusEnum.WAIT.getCode();
    private Date createTime;
    private Date updateTime;

}
