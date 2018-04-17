package com.qcl.global;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Optional;

import javax.annotation.PostConstruct;


@Component
public class AppConfig {
    //通过注解@value来获取配置文件的值
    @Value("${project.baseUrl}")
    private String baseUrl;

    @Value("${project.http-port}")
    private String httpPort;
    @Value("${project.https-port}")
    private String httpsPort;


    @PostConstruct
    public void adminConfig() {
        GlobalConstant.httpPort = Integer.parseInt(Optional.ofNullable(httpPort).orElse("80"));
        GlobalConstant.httpsPort = Integer.parseInt(Optional.ofNullable(httpsPort).orElse("443"));
        GlobalConstant.baseUrlHtpp = baseUrl + ":" + httpPort;
    }
}