package com.qcl.shuidaxia.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.slf4j.Slf4j;

/**
 * Created by qcl on 2018/7/10.
 */
@RestController
@RequestMapping("/shuidaxia")
@Slf4j
public class ShuiAdminController {
    @Autowired
    private ShuiAdminService service;



}
