package com.qcl.dataobject;

import java.math.BigDecimal;

import javax.persistence.Entity;
import javax.persistence.Id;

import lombok.Data;

/**
 * Created by qcl on 2018/3/13.
 */
@Entity
@Data
public class OrderDetail {
    @Id//主键
    private String detailId;
    private String orderId;
    private String productId;
    private String productName;
    private BigDecimal productPrice;
    //商品数量
    private Integer productQuantity;
    private String productIcon;
}
