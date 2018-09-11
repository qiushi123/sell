package com.qcl.userwechat.controller;

import com.google.gson.Gson;
import com.qcl.api.ResultApi;
import com.qcl.dataobject.User;
import com.qcl.enums.ResultEnum;
import com.qcl.exception.SellException;
import com.qcl.form.TestUserForm;
import com.qcl.form.UserForm;
import com.qcl.paotuischool.wechat.TemplateData;
import com.qcl.paotuischool.wechat.WxMssVo;
import com.qcl.userwechat.bean.AccessToken;
import com.qcl.userwechat.service.UserService;
import com.qcl.utils.ResultApiUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

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

    @GetMapping("/access_token")
    public String AcessTokenTimer(
            @RequestParam(name = "appid") String appid,
            @RequestParam(name = "secret") String secret) {

        String url = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential" +
                "&appid=" + appid + "&secret=" + secret;
        String json = restTemplate.getForObject(url, String.class);
        return json;
    }

    /**
     * 微信小程序模版推送
     *
     * @return
     */
    @PostMapping("/wxpush")
    public String book3(
            @RequestParam(name = "openid") String openid,
            @RequestParam(name = "formid") String formid,
            @RequestParam(name = "appid") String appid,
            @RequestParam(name = "secret") String secret) {

        //获取access_token
        String url = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential" +
                "&appid=" + appid + "&secret=" + secret;
        String json = restTemplate.getForObject(url, String.class);


        WxMssVo wxMssVo = new WxMssVo();
        wxMssVo.setTouser(openid);
        wxMssVo.setTemplate_id("LzeDP0G5PLgHoOjCMfhu44wfUluhW11Zeezu3r_dC24");
        wxMssVo.setForm_id(formid);
        wxMssVo.setPage("index");

        Map<String, TemplateData> m = new HashMap<String, TemplateData>();
        TemplateData first = new TemplateData();
//        first.setColor("#000000");
        first.setValue("***标题***");
        m.put("keyword1", first);

        TemplateData name = new TemplateData();
//        name.setColor("#000000");
        name.setValue("***名称***");
        m.put("keyword2", name);
        wxMssVo.setData(m);

        AccessToken accessToken = new Gson().fromJson(json, AccessToken.class);
        ResponseEntity<String> responseEntity = restTemplate.postForEntity(
                "https://api.weixin.qq.com/cgi-bin/message/wxopen/template/send" +
                        "?access_token=" + accessToken.getAccess_token(),
                wxMssVo,
                String.class);

        return responseEntity.getBody();
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
