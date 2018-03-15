package com.qcl.repository;

import com.qcl.dataobject.OrderMaster;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;

/**
 * Created by qcl on 2018/3/13.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class OrderMasterRepositoryTest {
    @Autowired
    private OrderMasterRepository repository;

    @Test
    public void saveTest() {
        OrderMaster orderMaster = new OrderMaster();
        orderMaster.setOrderId("1");
        orderMaster.setBuyerName("宫毛宁222");
        orderMaster.setBuyerPhone("15711823564");
        orderMaster.setBuyerAdderss("杭州");
        orderMaster.setBuyerOpenid("123123");
        orderMaster.setOrderAmount(new BigDecimal(2.3));
        OrderMaster result = repository.save(orderMaster);
        Assert.assertNotNull(result);
    }

    @Test
    public void findByBuyerOpenid() throws Exception {
        //查询第0页的10条数据
        PageRequest request = new PageRequest(0, 10);
        Page<OrderMaster> result = repository.findByBuyerOpenid("123123", request);
        System.out.println(result.getTotalElements());
        Assert.assertNotEquals(0, result.getTotalElements());
    }

}