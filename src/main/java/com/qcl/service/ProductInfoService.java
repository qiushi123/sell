package com.qcl.service;

import com.qcl.dataobject.ProductInfo;
import com.qcl.dto.CartDTO;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;


/**
 * 操作商品的service
 * Created by qcl on 2017/12/17.
 */
public interface ProductInfoService {

    //查询单个商品
    ProductInfo findOne(String productId);

    //查询所有在架商品
    List<ProductInfo> findAll();

    //带分页的查询
    Page<ProductInfo> findAll(Pageable pageable);

    //上架商品
    ProductInfo save(ProductInfo info);

    //加库存
    void addcreaseStock(List<CartDTO> cartDTOList);

    //减库存
    void deletecreaseStock(List<CartDTO> cartDTOList);
}
