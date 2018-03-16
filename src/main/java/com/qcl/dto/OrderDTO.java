package com.qcl.dto;

import com.qcl.dataobject.OrderDetail;
import com.qcl.enums.OrderStatusEnum;
import com.qcl.enums.PayStatusEnum;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import lombok.Data;

/**
 * Created by qcl on 2018/3/13.
 * OrderMaster对应的dto
 */
@Data
public class OrderDTO {

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
    private List<OrderDetail> orderDetailList;
}
