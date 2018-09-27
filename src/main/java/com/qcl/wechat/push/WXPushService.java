package com.qcl.wechat.push;

import com.google.gson.Gson;
import com.qcl.userwechat.bean.AccessToken;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.criteria.Predicate;

import lombok.extern.slf4j.Slf4j;

/**
 * Created by qcl on 2018/9/11.
 * 微信小程序推送服务，
 * 包含获取access_token的服务
 */
@Service
@Slf4j
public class WXPushService {
    //用来请求微信的get和post
    @Autowired
    private RestTemplate restTemplate;


    @Autowired
    private PushRepository pushRepository;

    /*
   * 获取所有通过审核的跑腿员，用户推送用
   * */
    //查询条件构造

    private List<PushBean> findAllRunner(String appid) {
        //查询条件构造
        Specification<PushBean> spec = (Specification<PushBean>) (root, query, cb) -> {
            List<Predicate> list = new ArrayList<>();
            //大于等于2，代表审核通过的跑腿员
            list.add(cb.greaterThanOrEqualTo(root.get("type"), 2));
            list.add(cb.isNotNull(root.get("formids")));//formids不为空
            list.add(cb.equal(root.get("appid"), appid));
            Predicate[] p = new Predicate[list.size()];
            return cb.and(list.toArray(p));
        };
        return pushRepository.findAll(spec);
    }


    /*
   * 微信推送多个用户
   * */
    public void pushAllRunner(String appid) {
        List<PushBean> runnerList = findAllRunner(appid);
        for (PushBean runner : runnerList) {
            String[] formIds = runner.getFormids();
            log.error("[推送服务]推送 runner={}", runner);
            if (formIds == null || formIds.length < 1) {
                return;
            }
            log.error("[推送服务]推送 formIds数组={}", formIds);
            //formId用一个减一个
            int lastIndex = formIds.length - 1;
            if (lastIndex < 0) {
                return;
            }
            String formid = formIds[lastIndex];
            String[] newformids = new String[lastIndex];
            for (int i = 0; i < lastIndex; i++) {
                newformids[i] = formIds[i];
            }
            //            CompletableFuture.runAsync(() -> {
            //需要异步处理的方法
            pushOneUser(runner, formid);
            //            });
            log.error("[推送服务]推送成功");
            runner.setFormids(newformids);
            pushRepository.save(runner);

        }

    }


    /*
    * 微信小程序推送单个用户
    * */
    private void pushOneUser(PushBean pushBean, String formid) {
        if (pushBean == null) {
            return;
        }

        //获取access_token
        String access_token = getAccess_token(pushBean.getAppid(), pushBean.getAppsecret());
        String url = "https://api.weixin.qq.com/cgi-bin/message/wxopen/template/send" +
                "?access_token=" + access_token;

        //拼接推送的模版
        WxMssVo wxMssVo = new WxMssVo();
        wxMssVo.setTouser(pushBean.getOpenid());//用户openid
        wxMssVo.setTemplate_id(pushBean.getMobanID());//模版id
        wxMssVo.setForm_id(formid);//formid


        Map<String, TemplateData> m = new HashMap<>(2);

        //keyword1：订单类型，keyword2：下单金额，keyword3：配送地址，keyword4：取件地址，keyword5备注
        TemplateData keyword1 = new TemplateData();
        keyword1.setValue("新下单待抢单");
        m.put("keyword1", keyword1);

        TemplateData keyword2 = new TemplateData();
        keyword2.setValue(pushBean.getMobanBeiZhu());
        m.put("keyword2", keyword2);
        wxMssVo.setData(m);

        //        TemplateData keyword3 = new TemplateData();
        //        keyword3.setValue("这里填配送地址");
        //        m.put("keyword3", keyword3);
        //        wxMssVo.setData(m);
        //
        //        TemplateData keyword4 = new TemplateData();
        //        keyword4.setValue("这里填取件地址");
        //        m.put("keyword4", keyword4);
        //        wxMssVo.setData(m);
        //
        //        TemplateData keyword5 = new TemplateData();
        //        keyword5.setValue("这里填备注");
        //        m.put("keyword5", keyword5);
        //        wxMssVo.setData(m);

        ResponseEntity<String> responseEntity =
                restTemplate.postForEntity(url, wxMssVo, String.class);
        log.error("小程序推送结果={}", responseEntity.getBody());
        //        responseEntity.getBody();
    }

    /*
    * 获取access_token
    * appid和appsecret到小程序后台获取，当然也可以让小程序开发人员给你传过来
    * */
    private String getAccess_token(String appid, String appsecret) {
        //获取access_token
        String url = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential" +
                "&appid=" + appid + "&secret=" + appsecret;
        String json = restTemplate.getForObject(url, String.class);
        AccessToken accessToken = new Gson().fromJson(json, AccessToken.class);
        return accessToken.getAccess_token();
    }

}
