package com.qcl.controller;

import com.qcl.api.ResultApi;
import com.qcl.dataobject.Pv2048;
import com.qcl.service.impl.PVServiceImpl;
import com.qcl.utils.ResultApiUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Created by qcl on 2017/12/17.
 * 2048页面访问量统计
 */

@RestController
@RequestMapping("/pv/2048")
public class Pv2048Controller {

    @Autowired
    private PVServiceImpl service;

    @GetMapping("/list")
    public ResultApi list() {
        //1，查询数据
        List<Pv2048> lists = service.findAll();
        int total = -1;
        if (lists != null) {
            total = lists.size();
        }
        //返回访问pv总数
        return ResultApiUtil.success(total);
    }

    @PostMapping("/add")
    public Pv2048 add(@RequestParam(name = "ip") String ip,
                      @RequestParam(name = "path") String path) {
        Pv2048 info = new Pv2048();
        info.setPvIp(ip);
        info.setPathname(path);

        return service.save(info);
    }

}
