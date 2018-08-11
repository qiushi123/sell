package com.qcl.userwechat.controller;

import com.qcl.api.ResultApi;
import com.qcl.dataobject.User;
import com.qcl.enums.ResultEnum;
import com.qcl.exception.SellException;
import com.qcl.form.TestUserForm;
import com.qcl.form.UserForm;
import com.qcl.userwechat.service.UserService;
import com.qcl.utils.ResultApiUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import javax.validation.Valid;

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


    /**
     * 用户注册或重置信息
     * 使用BindingResult校验参数
     *
     * @param userForm
     * @param bindingResult
     * @return
     */
    @PostMapping("/save")
    public ResultApi save(@Valid UserForm userForm, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            log.error("[注册用户] 参数不正确，orderForm={}", userForm);
            throw new SellException(ResultEnum.PARAM_ERROR.getCode()
                    , bindingResult.getFieldError().getDefaultMessage());
        }
        User user = new User();
        user.setPhone(userForm.getPhone());
        user.setPassword(userForm.getPassword());
        user.setName(userForm.getName());
        user.setOpenid(userForm.getOpenid());
        User user1 = service.save(user);
        return ResultApiUtil.success(user1);
    }

    /**
     * @param appid  小程序appid
     * @param secret 小程序密匙
     * @param code   wx.login返回的临时code
     * @return
     */
    @GetMapping("/wechat")
    public String getWeChatUserInfo(@RequestParam(name = "appid") String appid,
                                    @RequestParam(name = "secret") String secret,
                                    @RequestParam(name = "code") String code) {
        //https://api.weixin.qq.com/sns/jscode2session?appid=APPID
        // &secret=SECRET&js_code=JSCODE&grant_type=authorization_code
        String url = "https://api.weixin.qq.com/sns/jscode2session?appid=" + appid + "&secret=" + secret +
                "&js_code=" + code + "&grant_type=authorization_code";
        String json = restTemplate.getForObject(url, String.class);
        //        return ResultApiUtil.success(user);
        //        JSONObject json = restTemplate.getForEntity(url, JSONObject.class).getBody();
        return json;
    }


    /*
    * 学习小程序相关接口
    * */
    //测试注册
    @PostMapping("/testSave")
    public ResultApi testSave(@Valid TestUserForm userForm, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            log.error("[注册用户] 参数不正确，orderForm={}", userForm);
            throw new SellException(ResultEnum.PARAM_ERROR.getCode()
                    , bindingResult.getFieldError().getDefaultMessage());
        }
        User user = new User();
        user.setPhone("15611823666");
        user.setPassword(userForm.getPassword());
        user.setName(userForm.getName());
        user.setOpenid(userForm.getName());
        User user1 = service.save(user);
        return ResultApiUtil.success(user1);
    }

    //测试登陆
    @PostMapping("/testLogin")
    public ResultApi testLogin(@Valid TestUserForm userForm, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            log.error("[注册用户] 参数不正确，orderForm={}", userForm);
            throw new SellException(ResultEnum.PARAM_ERROR.getCode()
                    , bindingResult.getFieldError().getDefaultMessage());
        }
        User user = service.findOne(userForm.getName());
        if (user == null) {
            throw new SellException(ResultEnum.USER_NO_HAVE);
        }
        if (!StringUtils.pathEquals(userForm.getPassword(), user.getPassword())) {
            throw new SellException(ResultEnum.USER_PASSWORD_ERROR);
        }
        return ResultApiUtil.success(user);
    }
}
