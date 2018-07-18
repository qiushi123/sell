package com.qcl.shuidaxia.liushui;

import com.qcl.api.ResultApi;
import com.qcl.enums.ResultEnum;
import com.qcl.exception.SellException;
import com.qcl.shuidaxia.bean.ShuiLiuShui;
import com.qcl.shuidaxia.form.ShuiLiuShuiForm;
import com.qcl.utils.KeyUtil;
import com.qcl.utils.ResultApiUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import javax.validation.Valid;

import lombok.extern.slf4j.Slf4j;

/**
 * Created by qcl on 2018/7/10.
 */
@RestController
@RequestMapping("/shuidaxia")
@Slf4j
public class ShuiLiushuiController {
    @Autowired
    private ShuiLiushuiService service;

    //创建单个订单
    @PostMapping("/addliushui")
    public ResultApi create(@Valid ShuiLiuShuiForm form,
                            BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            log.error("[创建订单] 参数不正确，orderForm={}", form);
            throw new SellException(ResultEnum.PARAM_ERROR.getCode()
                    , bindingResult.getFieldError().getDefaultMessage());
        }

        ShuiLiuShui order = new ShuiLiuShui();
        if (form.getOrderId() == null || StringUtils.isEmpty(form.getOrderId())) {//流水已经存在就更新
            order.setOrderId(KeyUtil.genUniqueKey());
        } else {
            order = service.findOne(form.getOrderId());
        }
        order.setStaffId(form.getStaffId());
        order.setStaffName(form.getStaffName());
        order.setTime(form.getTime());
        order.setShoukuanleibie(form.getShoukuanleibie());//收款类别
        order.setShoukuanjine(form.getShoukuanjine());//收款金额
        order.setZhichuleibie(form.getZhichuleibie());
        order.setZhichujine(form.getZhichujine());
        order.setYewutichengxinxi(form.getYewutichengxinxi());
        order.setTichengjine(form.getTichengjine());
        order.setQingjia(form.getQingjia());
        order.setFakuan(form.getFakuan());
        order.setDixin(form.getDixin());

        ShuiLiuShui result = service.create(order);
        return ResultApiUtil.success(result);
    }

    /**
     * 根据员工id查询员工的所有流水
     */
    @PostMapping("/liushuiList")
    public ResultApi list(
            @RequestParam("staffId") String staffId) {
        if (StringUtils.isEmpty(staffId)) {
            log.error("[查询订单列表] staffId为空");
            throw new SellException(ResultEnum.PARAM_ERROR);
        }
        Long staffId2 = Long.parseLong(staffId);

        //按创建时间到排序
        Sort sort = new Sort(Sort.Direction.DESC, "createTime");
        List<ShuiLiuShui> orderList = service.findListByStaffId(staffId2, sort);
        return ResultApiUtil.success(orderList);
    }


}
