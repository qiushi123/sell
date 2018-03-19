package com.qcl.dataobject;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;

import lombok.Data;

/**
 * 商品
 * Created by qcl on 2017/12/17.
 */
@Entity
@Data
public class ProductInfo {
    @Id
    private String productId;

    private String productName;
    private BigDecimal productPrice;
    //    库存数量
    private Integer productStock;
    private String productDescription;
    private String productIcon;
    //商品类型
    private Integer productType;
    //类目编号
    private Integer categoryType;
    //商品状态 0正常1下架
    private Integer productStatus;
    //商品创建时间
    private Date createTime;

}
