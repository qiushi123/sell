package com.qcl.paotuischool.wechat;

import com.google.gson.Gson;
import com.qcl.userwechat.bean.AccessToken;
import com.qcl.utils.ConstantUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

import lombok.extern.slf4j.Slf4j;

/**
 * Created by qcl on 2018/9/11.
 * 微信小程序推送服务，
 * 包含获取access_token的服务
 */
@Service
@Slf4j
public class WxPushServiceQcl {
    //用来请求微信的get和post
    @Autowired
    private RestTemplate restTemplate;


    /*
    * 微信小程序推送单个用户
    * */
    public String pushOneUser(String openid, String formid) {


        //获取access_token
        String access_token = getAccess_token(ConstantUtils.SCHOOL_APPID, ConstantUtils.SCHOOL_APPSECRET);
        String url = "https://api.weixin.qq.com/cgi-bin/message/wxopen/template/send" +
                "?access_token=" + access_token;

        //拼接推送的模版
        WxMssVo wxMssVo = new WxMssVo();
        wxMssVo.setTouser(openid);//用户openid
        wxMssVo.setTemplate_id("LzeDP0G5PLgHoOjCMfhu44wfUluhW11Zeezu3r_dC24");//模版id
        wxMssVo.setForm_id(formid);//formid


        Map<String, TemplateData> m = new HashMap<>(5);

        //keyword1：订单类型，keyword2：下单金额，keyword3：配送地址，keyword4：取件地址，keyword5备注
        TemplateData keyword1 = new TemplateData();
        keyword1.setValue("新下单待抢单");
        m.put("keyword1", keyword1);

        TemplateData keyword2 = new TemplateData();
        keyword2.setValue("这里填下单金额的值");
        m.put("keyword2", keyword2);
        wxMssVo.setData(m);

        TemplateData keyword3 = new TemplateData();
        keyword3.setValue("这里填配送地址");
        m.put("keyword3", keyword3);
        wxMssVo.setData(m);

        TemplateData keyword4 = new TemplateData();
        keyword4.setValue("这里填取件地址");
        m.put("keyword4", keyword4);
        wxMssVo.setData(m);

        TemplateData keyword5 = new TemplateData();
        keyword5.setValue("这里填备注");
        m.put("keyword5", keyword5);
        wxMssVo.setData(m);

        ResponseEntity<String> responseEntity =
                restTemplate.postForEntity(url, wxMssVo, String.class);
        log.error("小程序推送结果={}", responseEntity.getBody());
        return responseEntity.getBody();
    }

    /*
    * 获取access_token
    * appid和appsecret到小程序后台获取，当然也可以让小程序开发人员给你传过来
    * */
    public String getAccess_token(String appid, String appsecret) {
        //获取access_token
        String url = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential" +
                "&appid=" + appid + "&secret=" + appsecret;
        String json = restTemplate.getForObject(url, String.class);
        AccessToken accessToken = new Gson().fromJson(json, AccessToken.class);
        return accessToken.getAccess_token();
    }

}
