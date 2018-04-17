package com.qcl.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by qcl on 2018/4/13.
 */
@RestController
public class HomeController {
    @RequestMapping("/home")
    public String home(){
        return "{name:qcl,wechat:2501902696}";
    }
}
