package com.qcl.shuidaxia.order;

import com.qcl.api.ResultApi;
import com.qcl.enums.ResultEnum;
import com.qcl.exception.SellException;
import com.qcl.shuidaxia.bean.ShuiOrder;
import com.qcl.shuidaxia.form.ShuiOrderForm;
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
public class ShuiOrderController {
    @Autowired
    private ShuiOrderService service;

    //创建单个订单
    @PostMapping("/add")
    public ResultApi create(@Valid ShuiOrderForm orderForm,
                            BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            log.error("[创建订单] 参数不正确，orderForm={}", orderForm);
            throw new SellException(ResultEnum.PARAM_ERROR.getCode()
                    , bindingResult.getFieldError().getDefaultMessage());
        }

        ShuiOrder order = new ShuiOrder();
        if (orderForm.getOrderId() == null || StringUtils.isEmpty(orderForm.getOrderId())) {//流水已经存在就更新
            order.setOrderId(KeyUtil.genUniqueKey());
        } else {
            order = service.findByOrderid(orderForm.getOrderId());
        }
        order.setBuyerId(orderForm.getUserId());
        order.setBuyerName(orderForm.getName());
        order.setBuyerPhone(orderForm.getPhone());
        order.setBuyerAdderss(orderForm.getAddress());
        order.setTime(orderForm.getTime());
        order.setSongshuixia(orderForm.getSongshuixia());
        order.setSongjitong(orderForm.getSongjitong());
        order.setHuikongtong(orderForm.getHuikongtong());
        order.setGongyongtong(orderForm.getGongjitong());
        order.setYushui(orderForm.getYushui());
        order.setXinmaishui(orderForm.getXinmaishui());
        order.setShoukuanren(orderForm.getShoukuanren());
        order.setShouru(orderForm.getShouru());
        order.setZhichu(orderForm.getZhichu());
        order.setBeizhu(orderForm.getBeizhu());

        ShuiOrder result = service.create(order);
        return ResultApiUtil.success(result);
    }

    /**
     * 根据手机号查询某一个用户的所有订单
     */
    @PostMapping("/ordersByUserId")
    public ResultApi list(
            @RequestParam("userid") String userid) {
        if (StringUtils.isEmpty(userid)) {
            log.error("[查询订单列表] userid为空");
            throw new SellException(ResultEnum.PARAM_ERROR);
        }
        Long userid2=Long.parseLong(userid);

        //按创建时间到排序
        Sort sort = new Sort(Sort.Direction.DESC, "createTime");
        List<ShuiOrder> orderList = service.findListByBuyerUserId(userid2, sort);
        return ResultApiUtil.success(orderList);
    }


}
