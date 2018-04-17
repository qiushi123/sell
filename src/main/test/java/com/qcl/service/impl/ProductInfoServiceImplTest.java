package com.qcl.service.impl;

import com.qcl.dataobject.ProductInfo;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created by qcl on 2017/12/17.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class ProductInfoServiceImplTest {

    @Autowired
    private ProductInfoServiceImpl service;

    @Test
    public void findOne() throws Exception {
        ProductInfo one = service.findOne("1");
        Assert.assertNotNull(one);

    }

    @Test
    public void findAll() throws Exception {
        List<ProductInfo> infos=service.findAll();
        Assert.assertNotEquals(0,infos.size());
    }

    @Test
    public void findAll1() throws Exception {
        //查询第0页的10条数据
        PageRequest request=new PageRequest(0,10);
        Page<ProductInfo> all = service.findAll(request);
        System.out.println(all.getTotalElements());

    }

    @Test
    public void save() throws Exception {
        ProductInfo info = new ProductInfo();
        info.setProductId("2");
        info.setProductName("小米粥2");
        info.setProductPrice(new BigDecimal(3.2));
        info.setProductStock(100);
        info.setProductDescription("很好喝2");
        info.setProductIcon("http://xxxxx.jpg");
        info.setCategoryType(2);
        info.setProductStatus(0);
        ProductInfo result = service.save(info);
        Assert.assertNotNull(result);
    }

}