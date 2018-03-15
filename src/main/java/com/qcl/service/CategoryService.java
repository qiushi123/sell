package com.qcl.service;

import com.qcl.dataobject.ProductCategory;

import java.util.List;

/**
 * 类目表的数据库操作服务
 * Created by qcl on 2017/12/16.
 */
public interface CategoryService {
    ProductCategory findOne(Integer categoryId);

    List<ProductCategory> findAll();

    List<ProductCategory> findByCategoryTypeIn(List<Integer> type);

    ProductCategory save(ProductCategory productCategory);
}
