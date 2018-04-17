package com.qcl.controller;

import com.qcl.api.ResultApi;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import lombok.extern.slf4j.Slf4j;

/**
 * Created by qcl on 2018/3/12.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class BuyerProductControllerTest {
    @Autowired
    private BuyerProductController controller;

    @Test
    public void list() throws Exception {
        ResultApi result = controller.list();
        log.debug("单元测试请求到的结果" + result.toString());
        Assert.assertNotNull(result);
    }

}