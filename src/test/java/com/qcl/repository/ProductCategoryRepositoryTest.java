package com.qcl.repository;

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
public class ProductCategoryRepositoryTest {
    @Test
    public void findByCategoryTypeIn() throws Exception {
        List<Integer> list = Arrays.asList(2, 3, 4);
        List<ProductCategory> result = productCategoryRepository.findByCategoryTypeIn(list);
        Assert.assertEquals(0, result.size());
    }

    @Autowired
    private ProductCategoryRepository productCategoryRepository;

    @Test
    public void test() {
        ProductCategory productCategory = new ProductCategory("女士最爱", 5);
        //        productCategory.setCategoryId(1);
        //        productCategory.setCategoryName("热销1");
        //        productCategory.setCategoryType(2);
        ProductCategory save = productCategoryRepository.save(productCategory);
        Assert.assertNotNull(save);
    }

    @Test
    public void findAll() {
        ProductCategory one = productCategoryRepository.findOne(1);
        System.out.println(one.toString());
    }

    @Test
    public void saveTest() {
        ProductCategory productCategory = new ProductCategory();
        ProductCategory result = productCategoryRepository.save(productCategory);
        Assert.assertNotNull(result);
    }


}