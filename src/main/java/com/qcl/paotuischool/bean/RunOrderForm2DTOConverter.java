package com.qcl.paotuischool.bean;

import com.qcl.paotuischool.form.RunSchoolOrderForm;
import com.qcl.utils.KeyUtil;

import lombok.extern.slf4j.Slf4j;

/**
 * Created by qcl on 2018/3/18.
 */
@Slf4j
public class RunOrderForm2DTOConverter {
    public static RunSchoolOrder converter(RunSchoolOrderForm orderForm) {
        RunSchoolOrder orderDTO = new RunSchoolOrder();
        orderDTO.setOrderId(KeyUtil.genUniqueKey());
        orderDTO.setOrderType(orderForm.getOrderType());
        orderDTO.setOpenid(orderForm.getOpenid());
        orderDTO.setName(orderForm.getName());
        orderDTO.setPhone(orderForm.getPhone());
        orderDTO.setSchool(orderForm.getSchool());
        orderDTO.setAddress(orderForm.getAddress());
        orderDTO.setFromAddress(orderForm.getFromAddress());
        orderDTO.setNoteContent(orderForm.getNoteContent());
        orderDTO.setExpressType(orderForm.getExpressType());
        orderDTO.setTotalMoney(orderForm.getTotalMoney());
        orderDTO.setJiaJi(orderForm.getIsJiaJi() == 1);
        orderDTO.setShangLou(orderForm.getIsShangLou()==1);
        orderDTO.setYundanhao(orderForm.getYundanhao());
        orderDTO.setSongdaTime(orderForm.getSongdaTime());
        orderDTO.setBeizhu(orderForm.getBeizhu());

        return orderDTO;
    }
}
