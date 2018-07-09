package com.qcl.paotui.bean;

import com.google.gson.Gson;
import com.qcl.paotui.form.RunOrderForm;
import com.qcl.utils.KeyUtil;

import lombok.extern.slf4j.Slf4j;

/**
 * Created by qcl on 2018/3/18.
 */
@Slf4j
public class RunOrderForm2DTOConverter {
    public static RunOrder converter(RunOrderForm orderForm) {
//        Gson gson = new Gson();
        RunOrder orderDTO = new RunOrder();
        orderDTO.setOrderId(KeyUtil.genUniqueKey());
        orderDTO.setBuyerOpenid(orderForm.getOpenid());
        orderDTO.setBuyerName(orderForm.getName());
        orderDTO.setBuyerPhone(orderForm.getPhone());
        orderDTO.setBuyerAdderss(orderForm.getAddress());
        orderDTO.setFromAddress(orderForm.getFrom_address());
        orderDTO.setOrderAmount(orderForm.getOrder_amount());

        return orderDTO;
    }
}
