package com.qcl.converter;

import com.qcl.dataobject.OrderMaster;
import com.qcl.dto.OrderDTO;

import org.springframework.beans.BeanUtils;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by qcl on 2018/3/16.
 */
public class OrderMaster2OrderDTOConverter {
    public static OrderDTO converter(OrderMaster orderMaster) {
        OrderDTO orderDTO = new OrderDTO();
        BeanUtils.copyProperties(orderMaster, orderDTO);
        return orderDTO;
    }

    public static List<OrderDTO> converter(List<OrderMaster> orderMasters) {
        List<OrderDTO> orderDTOS = orderMasters.stream()
                .map(e -> converter(e))
                .collect(Collectors.toList());
        return orderDTOS;
    }
}
