package com.qcl.repository;

import com.qcl.dataobject.OrderDetail;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created by qcl on 2018/3/13.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class OrderDetailRepositoryTest {

    @Autowired
    private OrderDetailRepository repository;
    @Test
    public void saveTest() throws Exception {
        OrderDetail detail=new OrderDetail();
        detail.setDetailId("1");
        detail.setOrderId("1");
        detail.setProductId("1122");
        detail.setProductName("南瓜粥");
        detail.setProductPrice(new BigDecimal(3.3));
        detail.setProductId("http://134.jpg");
        detail.setProductQuantity(2);
        OrderDetail save = repository.save(detail);
        Assert.assertNotNull(save);

    }

    @Test
    public void findByOrderId() throws Exception {
        List<OrderDetail> result = repository.findByOrderId("1");
        Assert.assertNotEquals(0,result.size());

    }

}