package com.qcl.paotuischool.wechat;

import com.google.gson.Gson;
import com.qcl.paotuischool.bean.RunSchoolOrder;
import com.qcl.paotuischool.bean.SchoolRunner;
import com.qcl.paotuischool.runner.SchoolerService;
import com.qcl.userwechat.bean.AccessToken;
import com.qcl.utils.ConstantUtils;
import com.qcl.utils.Utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

import lombok.extern.slf4j.Slf4j;

/**
 * Created by qcl on 2018/9/11.
 * 微信小程序推送服务，
 * 包含获取access_token的服务
 */
@Service
@Slf4j
public class WxPushService {
    //用来请求微信的get和post
    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private SchoolerService schoolerService;

    /*
    * 微信推送多个用户
    * */
    public void pushAll(RunSchoolOrder schoolOrder) {
        if (schoolOrder == null) {
            return;
        }

        List<SchoolRunner> runnerList = schoolerService.findAll();
        for (SchoolRunner runner : runnerList) {
            String formIds = runner.getFormIds();
            List formIdList = Utils.String2List(formIds);
            if (formIdList == null || formIdList.size() < 1) {
                return;
            }
            //formId用一个减一个
            String formid = (String) formIdList.get(0);
            formIdList.remove(0);
            runner.setFormIds(Utils.List2String(formIdList));
            schoolerService.save(runner);

            CompletableFuture.runAsync(() -> {
                //需要异步处理的方法
                pushOneUser(runner.getOpenId(), formid, schoolOrder);
            });

        }

    }

    /*
    * 微信小程序推送单个用户
    * */
    public String pushOneUser(String openid, String formid,
                              RunSchoolOrder schoolOrder) {

        if (schoolOrder == null) {
            return "订单信息为空，推送失败";
        }
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
        //first.setColor("#000000");
        keyword1.setValue("新下单待抢单");
        m.put("keyword1", keyword1);

        TemplateData keyword2 = new TemplateData();
        keyword2.setColor("#FF3030");
        keyword2.setValue(schoolOrder.getTotalMoney() + "元");
        m.put("keyword2", keyword2);
        wxMssVo.setData(m);

        TemplateData keyword3 = new TemplateData();
        keyword3.setValue(schoolOrder.getSchool());
        m.put("keyword3", keyword3);
        wxMssVo.setData(m);

        TemplateData keyword4 = new TemplateData();
        keyword4.setValue(schoolOrder.getSchool());
        m.put("keyword4", keyword4);
        wxMssVo.setData(m);

        TemplateData keyword5 = new TemplateData();
        keyword5.setValue(schoolOrder.getBeizhu());
        m.put("keyword5", keyword5);
        wxMssVo.setData(m);

        ResponseEntity<String> responseEntity =
                restTemplate.postForEntity(url, wxMssVo, String.class);
        log.error("小程序推送结果={}", responseEntity.getBody());
        return responseEntity.getBody();
    }

    /*
    * 获取access_token
    * */
    public String getAccess_token(String appid, String secret) {
        //获取access_token
        String url = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential" +
                "&appid=" + appid + "&secret=" + secret;
        String json = restTemplate.getForObject(url, String.class);
        AccessToken accessToken = new Gson().fromJson(json, AccessToken.class);
        return accessToken.getAccess_token();
    }

}
