package com.qcl.shuidaxia.user;

import com.qcl.api.ResultApi;
import com.qcl.enums.ResultEnum;
import com.qcl.exception.SellException;
import com.qcl.shuidaxia.admin.ShuiAdminService;
import com.qcl.shuidaxia.bean.ShuiAdmin;
import com.qcl.shuidaxia.bean.ShuiUser;
import com.qcl.shuidaxia.form.ShuiVipForm;
import com.qcl.utils.Base64Utils;
import com.qcl.utils.ProtectUserUtils;
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
public class ShuiUserController {
    @Autowired
    private ShuiUserService service;
    @Autowired
    private ShuiAdminService adminService;

    /**
     * 添加会员信息
     *
     * @param vipForm
     * @param bindingResult
     * @return
     */
    @PostMapping("/addVip")
    public ResultApi create(@Valid ShuiVipForm vipForm,
                            BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            log.error("[创建订单] 参数不正确，orderForm={}", vipForm);
            throw new SellException(ResultEnum.PARAM_ERROR.getCode()
                    , bindingResult.getFieldError().getDefaultMessage());
        }

        ShuiUser user = new ShuiUser();
        if (vipForm.getUserId() != null) {
            user.setUserId(vipForm.getUserId());
        }
        user.setUserName(vipForm.getUserName());
        user.setUserPhone(vipForm.getUserPhone());
        user.setUserAdderss(vipForm.getUserAdderss());
        user.setUserFrom(vipForm.getUserFrom());

        user.setBuyTime(vipForm.getBuyTime());
        user.setBuyType(vipForm.getBuyType());
        user.setShoukuanren(vipForm.getShoukuanren());
        user.setShoukuanType(vipForm.getShoukuanType());
        user.setYanjin(vipForm.getYanjin());
        user.setYinshuijiNum(vipForm.getYinshuijiNum());
        user.setShouciPeiSongNum(vipForm.getShouciPeiSongNum());


        user.setTuidingTime(vipForm.getTuidingTime());
        user.setTuidingyuanyin(vipForm.getTuidingyuanyin());
        user.setTuidingjieguo(vipForm.getTuidingjieguo());
        user.setBeizhu(vipForm.getBeizhu());


        ShuiUser result = service.create(user);
        return ResultApiUtil.success(result);
    }

    /**
     * 查询某一个用户的用户信息
     */
    @PostMapping("/getVipInfo")
    public ResultApi list(
            @RequestParam("type") int type,
            @RequestParam("input") String input) {
        if (StringUtils.isEmpty(input)) {
            log.error("[查询用户] 输入内容为空");
            throw new SellException(ResultEnum.PARAM_ERROR);
        }

        ShuiUser user = null;
        //0手机号，1会员编号，2地址
        if (type == 0) {//
            user = service.findByUserPhone(input);
        } else if (type == 1) {
            user = service.findByUserId(Long.parseLong(input));
        } else if (type == 2) {
            user = service.findByUserAdderss(input);
        }
        ShuiUser shuiUser = ProtectUserUtils.protectShuiDaXiaUserInfo(user);
        return ResultApiUtil.success(user);
    }

    /**
     * 管理员查询某一个用户的用户信息，
     * 查询前需要校验管理员身份
     */
    @PostMapping("/getVipInfoByAdmin")
    public ResultApi vipinfo(
            @RequestParam("type") int type,
            //            @RequestParam("adminPhone") String adminPhone,
            //            @RequestParam("adminPassword") String adminPassword,
            @RequestParam("input") String input) {
        if (StringUtils.isEmpty(input)) {
            log.error("[查询用户] 输入内容为空");
            throw new SellException(ResultEnum.PARAM_ERROR);
        }

        //先不校验管理员
        //        ShuiAdmin admin = adminService.findByUserPhone(adminPhone);
        //        if (admin == null || !StringUtils.pathEquals(adminPassword, admin.getAdminPassword())) {
        //            throw new SellException(ResultEnum.USER_NO_AUTHORITY);
        //        }

        ShuiUser user = null;
        //0手机号，1会员编号，2地址
        if (type == 0) {//
            user = service.findByUserPhone(input);
        } else if (type == 1) {
            user = service.findByUserId(Long.parseLong(input));
        } else if (type == 2) {
            user = service.findByUserAdderss(input);
        }

        return ResultApiUtil.success(user);
    }

    /**
     * 管理员登陆，
     * 查询前需要校验管理员身份
     */
    @PostMapping("/adminLogin")
    public ResultApi adminLogin(
            @RequestParam("adminPhone") String adminPhone,
            @RequestParam("adminPassword") String adminPassword) {

        if (StringUtils.isEmpty(adminPhone) || StringUtils.isEmpty(adminPassword)) {
            throw new SellException(ResultEnum.PARAM_ERROR);
        }
        //校验管理员
        ShuiAdmin admin = adminService.findByAdminPhone(adminPhone);
        if (admin == null) {
            throw new SellException(ResultEnum.USER_ADMIN_NO);
        }

        String password = "";
        try {
            password = Base64Utils.encryptBASE64(admin.getAdminPassword().getBytes());
            password = password.trim();
        } catch (Exception e) {
            e.printStackTrace();
        }
        log.error("[查询用户] 加密 password={}", password);
        log.error("[查询用户] h5加密 adminPassword={}", adminPassword);
        boolean equals = password.equals(adminPassword);
        log.error("" + equals);
        if (!adminPassword.equals(password)) {
            throw new SellException(ResultEnum.USER_PASSWORD_ERROR);
        }
        return ResultApiUtil.success(admin);
    }


}
