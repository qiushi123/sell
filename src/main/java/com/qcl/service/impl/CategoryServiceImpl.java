package com.qcl.service.impl;

import com.qcl.dataobject.ProductCategory;
import com.qcl.repository.ProductCategoryRepository;
import com.qcl.service.CategoryService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 类目
 * Created by qcl on 2017/12/16.
 */
@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private ProductCategoryRepository repository;

    @Override
    public ProductCategory findOne(Integer categoryId) {
        return repository.findById(categoryId).get();
    }

    @Override
    public List<ProductCategory> findAll() {
        return repository.findAll();
    }

    /*
    * 查询指定类库信息
    * */
    @Override
    public List<ProductCategory> findByCategoryTypeIn(List<Integer> type) {
        return repository.findByCategoryTypeIn(type);
    }

    @Override
    public ProductCategory save(ProductCategory productCategory) {
        return repository.save(productCategory);
    }
}
