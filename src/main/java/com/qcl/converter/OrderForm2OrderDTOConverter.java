package com.qcl.converter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.qcl.dataobject.OrderDetail;
import com.qcl.dto.OrderDTO;
import com.qcl.enums.ResultEnum;
import com.qcl.exception.SellException;
import com.qcl.form.OrderForm;

import java.util.ArrayList;
import java.util.List;

import lombok.extern.slf4j.Slf4j;

/**
 * Created by qcl on 2018/3/18.
 */
@Slf4j
public class OrderForm2OrderDTOConverter {
    public static OrderDTO converter(OrderForm orderForm) {
        Gson gson = new Gson();
        OrderDTO orderDTO = new OrderDTO();
        orderDTO.setBuyerOpenid(orderForm.getOpenid());
        orderDTO.setBuyerName(orderForm.getName());
        orderDTO.setBuyerPhone(orderForm.getPhone());
        orderDTO.setBuyerAdderss(orderForm.getAddress());

        List<OrderDetail> orderDetailList = new ArrayList<>();
        try {
            orderDetailList = gson.fromJson(orderForm.getItems(),
                    new TypeToken<List<OrderDetail>>() {
                    }.getType());
        } catch (Exception e) {
            log.error("[对象转换] 错误 errorstr={}", orderForm.getItems());
            throw new SellException(ResultEnum.PARAM_ERROR);
        }
        orderDTO.setOrderDetailList(orderDetailList);
        return orderDTO;
    }
}
