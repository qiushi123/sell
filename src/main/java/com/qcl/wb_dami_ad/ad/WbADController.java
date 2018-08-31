package com.qcl.wb_dami_ad.ad;

import com.qcl.api.ResultApi;
import com.qcl.utils.ResultApiUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import lombok.extern.slf4j.Slf4j;

/**
 * 跑腿
 */

@RestController
@RequestMapping("/damise")
@Slf4j
public class WbADController {

    @Autowired
    private WbADService service;


    //获取某一天的广告点击排名
    @PostMapping("/clickList")
    public ResultApi clikcList(@RequestParam(name = "dateTime") String dateTime) {
        Sort sort = new Sort(Sort.Direction.DESC, "clickNum");
        List<WbAdClickBean> beans = service.findClickList(dateTime, sort);
        return ResultApiUtil.success(beans);
    }

    //某一天广告点击增加
    @PostMapping("/clickAdd")
    public ResultApi clikcAdd(@RequestParam(name = "dateTime") String dateTime,
                              @RequestParam(name = "name") String name,
                              @RequestParam(name = "city") String city) {
        List<WbAdClickBean> existList = service.findHasExist(dateTime, name);

        WbAdClickBean newBean = new WbAdClickBean();
        if (existList != null && existList.size() > 0) {//今天已经点击过
            newBean = existList.get(0);//如果存在多个用户，只取第一个
            newBean.setClickNum(newBean.getClickNum() + 1);
        } else {
            newBean.setName(name);
            newBean.setCity(city);
            newBean.setClickNum(0);
            newBean.setDateTime(dateTime);
        }
        return ResultApiUtil.success(service.save(newBean));
    }

}
