package com.qcl.api;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;
import java.util.Date;

import lombok.Data;

/**
 * Created by qcl on 2017/12/17.
 * 这里决定返回给前端的api格式
 */
@Data
public class ProductInfoApi {
    @JsonProperty("id")
    private String productId;
    @JsonProperty("name")
    private String productName;
    @JsonProperty("price")
    private BigDecimal productPrice;
    @JsonProperty("desc")
    private String productDescription;
    @JsonProperty("icon")
    private String productIcon;
    @JsonProperty("createTime")
    private Date createTime;
}
