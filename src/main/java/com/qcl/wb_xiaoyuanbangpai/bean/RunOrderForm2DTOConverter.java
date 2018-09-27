package com.qcl.wb_xiaoyuanbangpai.bean;

import com.qcl.utils.KeyUtil;
import com.qcl.wb_xiaoyuanbangpai.form.BangPaiOrderForm;

import lombok.extern.slf4j.Slf4j;

/**
 * Created by qcl on 2018/3/18.
 */
@Slf4j
public class RunOrderForm2DTOConverter {
    public static BangPaiOrder converter(BangPaiOrderForm orderForm) {
        BangPaiOrder orderDTO = new BangPaiOrder();
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
        orderDTO.setShangLou(orderForm.getIsShangLou() == 1);
        orderDTO.setYundanhao(orderForm.getYundanhao());
        orderDTO.setSongdaTime(orderForm.getSongdaTime());
        orderDTO.setBeizhu(orderForm.getBeizhu());
        orderDTO.setCompany(orderForm.getCompany());//新加快递公司

        return orderDTO;
    }
}
