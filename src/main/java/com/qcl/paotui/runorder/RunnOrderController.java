package com.qcl.paotui.runorder;

import com.qcl.api.ResultApi;
import com.qcl.enums.ResultEnum;
import com.qcl.exception.SellException;
import com.qcl.paotui.bean.RunOrder;
import com.qcl.paotui.bean.RunOrderForm2DTOConverter;
import com.qcl.paotui.bean.Runner;
import com.qcl.paotui.form.RobOrderForm;
import com.qcl.paotui.form.RunOrderForm;
import com.qcl.paotui.runner.RunnerService;
import com.qcl.utils.ProtectUserUtils;
import com.qcl.utils.ResultApiUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import lombok.extern.slf4j.Slf4j;

/**
 * 跑腿
 */

@RestController
@RequestMapping("/runOrder")
@Slf4j
public class RunnOrderController {

    @Autowired
    private RunnOrderService service;

    //查询跑腿员信息
    @Autowired
    private RunnerService runnerService;

    //创建单个订单
    @PostMapping("/create")
    public ResultApi<Map<String, String>> create(@Valid RunOrderForm orderForm,
                                                 BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            log.error("[创建订单] 参数不正确，orderForm={}", orderForm);
            throw new SellException(ResultEnum.PARAM_ERROR.getCode()
                    , bindingResult.getFieldError().getDefaultMessage());
        }

        RunOrder orderDTO = RunOrderForm2DTOConverter.converter(orderForm);
        RunOrder result = service.create(orderDTO);
        return ResultApiUtil.success(result);
    }

    /**
     * 跑腿员抢单
     */
    @PostMapping("/robOrder")
    public ResultApi<RunOrder> robOneOrder(@Valid RobOrderForm orderForm,
                                           BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            log.error("[创建订单] 参数不正确，orderForm={}", orderForm);
            throw new SellException(ResultEnum.PARAM_ERROR.getCode()
                    , bindingResult.getFieldError().getDefaultMessage());
        }

        RunOrder order = service.findOne(orderForm.getOrderid());
        if (order==null) {
            throw new SellException(ResultEnum.ORDER_NOT_EXIST);
        }
        //订单状态，-1取消订单，0新下单，1已抢单，2订单完成
        if (order.getOrderStatus() != 0) {
            throw new SellException(ResultEnum.ORDER_HAS_ROBBED);
        }

        order.setRunnerId(orderForm.getOpenid());
        order.setRunnerName(orderForm.getName());
        order.setRunnerPhone(orderForm.getPhone());
        RunOrder result = service.create(order);
        return ResultApiUtil.success(result);
    }

    /**
     * 某一个用户的所有订单
     */
    @PostMapping("/myOrderList")
    public ResultApi<List<RunOrder>> list(
            @RequestParam("openid") String openid,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size) {
        if (StringUtils.isEmpty(openid)) {
            log.error("[查询订单列表] openid为空");
            throw new SellException(ResultEnum.PARAM_ERROR);
        }

        //按订单创建时间到排序，新订单在最前面
        Sort sort = new Sort(Sort.Direction.DESC, "updateTime");
        PageRequest request = new PageRequest(page, size, sort);

        Page<RunOrder> orderPage = service.findList(openid, request);

        return ResultApiUtil.success(orderPage.getContent());
    }


    /**
     * 查询某一个订单
     */
    @PostMapping("/oneOrder")
    public ResultApi<RunOrder> oneOrder(
            @RequestParam("orderid") String orderid) {
        if (StringUtils.isEmpty(orderid)) {
            log.error("[查询订单列表] 订单号不能为空");
            throw new SellException(ResultEnum.ORDER_NOT_EXIST);
        }
        RunOrder order = service.findOne(orderid);
        return ResultApiUtil.success(order);
    }

    /**
     * 某一个跑腿员抢的所有订单
     */
    @PostMapping("/ruunerOrderList")
    public ResultApi<List<RunOrder>> runerOrderlist(
            @RequestParam("runnerOpenid") String runnerOpenid,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size) {
        if (StringUtils.isEmpty(runnerOpenid)) {
            log.error("[查询订单列表] openid为空");
            throw new SellException(ResultEnum.PARAM_ERROR);
        }

        //按订单创建时间到排序，新订单在最前面
        Sort sort = new Sort(Sort.Direction.DESC, "updateTime");
        PageRequest request = new PageRequest(page, size, sort);

        Page<RunOrder> orderPage = service.findRunnerList(runnerOpenid, request);

        return ResultApiUtil.success(orderPage.getContent());
    }

    /**
     * 查询所有可以被抢的订单
     */
    @PostMapping("/canRobbedOrders")
    public ResultApi<List<RunOrder>> canRobbedOrders(
            @RequestParam("runnerOpenid") String runnerOpenid,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size) {
        if (StringUtils.isEmpty(runnerOpenid)) {
            log.error("[查询订单列表] 跑腿员openid为空");
            throw new SellException(ResultEnum.PARAM_ERROR);
        }
        //只有注册了跑腿员，才能查看可以抢的订单
        Runner runner = runnerService.findOneOpenid(runnerOpenid);
        if (runner.getType() != 2) {
            log.error("[查询可抢订单列表] 跑腿员没有审核通过");
            throw new SellException(ResultEnum.PARAM_ERROR);
        }

        //按订单创建时间到排序，新订单在最前面
        Sort sort = new Sort(Sort.Direction.DESC, "updateTime");
        PageRequest request = new PageRequest(page, size, sort);
        Page<RunOrder> orderPage = service.canRobbedOrders(request);

        List<RunOrder> orderList = ProtectUserUtils.protectUserOrders(orderPage.getContent());
        return ResultApiUtil.success(orderList);
    }

}
