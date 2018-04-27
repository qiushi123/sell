package com.qcl;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import lombok.extern.slf4j.Slf4j;

/**
 * Created by qcl on 2018/4/13.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class GlobalConstantTest {
    @Test
    public void test1() {
        log.error("port httpPort={}", GlobalConstant.httpPort);
        log.error("port httpsPort={}", GlobalConstant.httpsPort);
    }
}