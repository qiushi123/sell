package com.qcl.repository;

import com.qcl.dataobject.ProductInfo;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * 商品的dao
 * Created by qcl on 2017/12/17.
 */
public interface ProductInfoRepository extends JpaRepository<ProductInfo, String> {

    //查询上架商品,0上架1下架
    List<ProductInfo> findByProductStatus(Integer productStatus);
}
