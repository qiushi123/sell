package com.qcl.repository;

import com.qcl.dataobject.ProductCategory;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * 类目
 * jpa用来操作bean进行数据库操作的dao
 * Created by qcl on 2017/12/16.
 */
public interface ProductCategoryRepository extends JpaRepository<ProductCategory, Integer> {

    List<ProductCategory> findByCategoryTypeIn(List<Integer> categoryTypeList);
}
