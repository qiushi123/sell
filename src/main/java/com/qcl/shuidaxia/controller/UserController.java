package com.qcl.shuidaxia.controller;

import com.qcl.api.ResultApi;
import com.qcl.dataobject.User;
import com.qcl.shuidaxia.service.UserService;
import com.qcl.utils.ResultApiUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import lombok.extern.slf4j.Slf4j;

/**
 * 文章
 */

@RestController
@RequestMapping("/user")
@Slf4j
public class UserController {

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private UserService service;


    //更加openid获取用户信息
    @GetMapping("/login")
    public ResultApi findOne(@RequestParam(name = "openid") String openid) {
        User user = service.findOne(openid);
        return ResultApiUtil.success(user);
    }

    //用户注册
    @GetMapping("/save")
    public ResultApi save(
                          @RequestParam(name = "phone") String phone,
                          @RequestParam(name = "name") String name,
                          @RequestParam(name = "password") String password,
                          @RequestParam(name = "openid") String openid) {
        User user = new User();
        user.setPhone(phone);
        user.setPassword(password);
        user.setName(name);
        user.setOpenid(openid);
        User user1 = service.save(user);
        return ResultApiUtil.success(user1);
    }

    @GetMapping("/wechat")
    public String getWeChatUserInfo(@RequestParam(name = "code") String code) {
        //https://api.weixin.qq.com/sns/jscode2session?appid=APPID
        // &secret=SECRET&js_code=JSCODE&grant_type=authorization_code
        String url = "https://api.weixin.qq.com/sns/jscode2session?appid=wx7c54942dfc87f4d8&secret=" +
                "79f737d1d2f9473b0a659f52ff404067&js_code=" + code + "&grant_type=authorization_code";
        String user = restTemplate.getForObject(url, String.class);
        //        return ResultApiUtil.success(user);
        //        JSONObject json = restTemplate.getForEntity(url, JSONObject.class).getBody();
        return user;
    }

}
