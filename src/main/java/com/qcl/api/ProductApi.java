package com.qcl.api;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

import lombok.Data;

/**
 * 商品包含类目的返回bean
 * Created by qcl on 2017/12/17.
 */
@Data
public class ProductApi {
    @JsonProperty("name")
    private String categoryName;
    @JsonProperty("type")
    private Integer categoryType;
    @JsonProperty("foods")
    private List<ProductInfoApi> categoryData;
}
