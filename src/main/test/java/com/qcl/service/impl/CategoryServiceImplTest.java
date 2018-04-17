package com.qcl.service.impl;

import com.qcl.dataobject.ProductCategory;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.List;

/**
 * Created by qcl on 2017/12/16.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class CategoryServiceImplTest {
    @Autowired
    private CategoryServiceImpl service;

    @Test
    public void findOne() throws Exception {
        ProductCategory productCategory = service.findOne(7);
        Assert.assertEquals(new Integer(7), productCategory.getCategoryId());
    }

    @Test
    public void findAll() throws Exception {
        List<ProductCategory> productCategories = service.findAll();
        Assert.assertNotEquals(0, productCategories.size());
    }

    @Test
    public void findByCategoryTypeIn() throws Exception {
        List<ProductCategory> productCategories = service.findByCategoryTypeIn(Arrays.asList(2, 3, 4));
        Assert.assertNotEquals(0, productCategories.size());
    }

    @Test
    public void save() throws Exception {
        ProductCategory productCategory = new ProductCategory("那是15", 15);
        ProductCategory save = service.save(productCategory);
        Assert.assertNotNull(save);
    }

}