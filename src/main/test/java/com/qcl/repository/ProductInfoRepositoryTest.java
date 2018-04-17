package com.qcl.repository;

import com.qcl.dataobject.ProductInfo;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created by qcl on 2017/12/17.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class ProductInfoRepositoryTest {

    @Autowired
    private ProductInfoRepository repository;

    @Test
    public void findByProductStatus() throws Exception {
        List<ProductInfo> infos = repository.findByProductStatus(0);
        Assert.assertNotEquals(0, infos.size());
    }

    @Test
    public void saveTest() {
        ProductInfo info = new ProductInfo();
        info.setProductId("1");
        info.setProductName("小米粥");
        info.setProductPrice(new BigDecimal(3.2));
        info.setProductStock(100);
        info.setProductDescription("很好喝");
        info.setProductIcon("http://xxxxx.jpg");
        info.setCategoryType(2);
        info.setProductStatus(0);
        ProductInfo result = repository.save(info);
        Assert.assertNotNull(result);
    }

}