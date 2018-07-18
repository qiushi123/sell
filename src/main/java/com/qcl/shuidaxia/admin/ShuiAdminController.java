package com.qcl.shuidaxia.admin;

import com.qcl.api.ResultApi;
import com.qcl.enums.ResultEnum;
import com.qcl.exception.SellException;
import com.qcl.shuidaxia.bean.ShuiAdmin;
import com.qcl.shuidaxia.form.ShuiAdminForm;
import com.qcl.utils.ResultApiUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

import lombok.extern.slf4j.Slf4j;

/**
 * Created by qcl on 2018/7/10.
 */
@RestController
@RequestMapping("/shuidaxia")
@Slf4j
public class ShuiAdminController {
    @Autowired
    private ShuiAdminService service;

    /**
     * 添加员工信息
     *
     * @param adminForm
     * @param bindingResult
     * @return
     */
    @PostMapping("/addStaff")
    public ResultApi create(@Valid ShuiAdminForm adminForm,
                            BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            log.error("[创建订单] 参数不正确，orderForm={}", adminForm);
            throw new SellException(ResultEnum.PARAM_ERROR.getCode()
                    , bindingResult.getFieldError().getDefaultMessage());
        }

        ShuiAdmin admin = new ShuiAdmin();
        if (adminForm.getAdminId() != null) {//员工已经存在就更新
            admin.setAdminId(adminForm.getAdminId());
        }
        //初始密码：123456
        if (adminForm.getAdminPassword() == null || StringUtils.isEmpty(adminForm.getAdminPassword())) {
            admin.setAdminPassword("123456");
        } else {
            admin.setAdminPassword(adminForm.getAdminPassword());
        }
        //超级权限只能经理一个人有
        //-1离职管理员，0普通会员不是管理员，1管理员，2超级管理员（可以管理管理员和员工）
        if (adminForm.getAdminType() > 1) {
            admin.setAdminType(1);
        } else {
            admin.setAdminType(adminForm.getAdminType());
        }


        admin.setAdminName(adminForm.getAdminName());
        admin.setAdminCardId(adminForm.getAdminCardId());
        admin.setAdminPhone(adminForm.getAdminPhone());
        admin.setAdminPhoneBeiYong(adminForm.getAdminPhoneBeiYong());

        admin.setAdminFromTime(adminForm.getAdminFromTime());
        admin.setAdminOutTime(adminForm.getAdminOutTime());
        admin.setBeizhu(adminForm.getBeizhu());


        ShuiAdmin result = service.create(admin);
        return ResultApiUtil.success(result);
    }


    /**
     * 查询某一个员工的信息
     */
    @PostMapping("/getStaffInfo")
    public ResultApi getStaffInfo(
            @RequestParam("type") int type,
            @RequestParam("input") String input,
            @RequestParam("adminType") int adminType) {
        if (StringUtils.isEmpty(input)) {
            log.error("[查询用户] 输入内容为空");
            throw new SellException(ResultEnum.PARAM_ERROR);
        }
        //-1离职管理员，0普通会员不是管理员，1管理员，2超级管理员（可以管理管理员和员工）
        if (adminType < 2) {
            log.error("[查询用户] 不是超级管理员");
            throw new SellException(ResultEnum.USER_NO_AUTHORITY);
        }

        ShuiAdmin user = null;
        //0员工姓名,1员工身份证,2员工手机号
        if (type == 0) {//
            user = service.findByAdminName(input);
        } else if (type == 1) {
            user = service.findByAdminCardId(input);
        } else if (type == 2) {
            user = service.findByAdminPhone(input);
        }
        return ResultApiUtil.success(user);
    }


}
