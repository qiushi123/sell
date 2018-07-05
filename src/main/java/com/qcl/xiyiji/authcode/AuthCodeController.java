package com.qcl.xiyiji.authcode;

import com.qcl.api.ResultApi;
import com.qcl.enums.ResultEnum;
import com.qcl.exception.SellException;
import com.qcl.utils.ResultApiUtil;
import com.qcl.xiyiji.form.AuthCodeForm;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Valid;

import lombok.extern.slf4j.Slf4j;

/**
 * 文章
 */

@RestController
@RequestMapping("/code")
@Slf4j
public class AuthCodeController {

    @Autowired
    private AuthCodeService service;


    //根据openid获取用户信息
    @GetMapping("/openid")
    public ResultApi findOne(@RequestParam(name = "openid") String openid) {
        AuthCodeBean bean = service.findOneOpenid(openid);
        boolean isNull;
        if (bean == null || StringUtils.isEmpty(bean.getName())
                || StringUtils.isEmpty(bean.getCodeId()
        )) {
            isNull = false;
        } else {
            isNull = true;
        }
        return ResultApiUtil.success(isNull);
    }


    /**
     * 用户注册或重置信息
     * 使用BindingResult校验参数
     *
     * @param codeForm
     * @param bindingResult
     * @return
     */
    @PostMapping("/checkCode")
    public ResultApi save(@Valid AuthCodeForm codeForm, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            log.error("[注册用户] 参数不正确，orderForm={}", codeForm);
            throw new SellException(ResultEnum.PARAM_ERROR.getCode()
                    , bindingResult.getFieldError().getDefaultMessage());
        }

        AuthCodeBean codeBean = service.findOneByCodeId(codeForm.getCodeid());
        if (codeBean == null) {
            throw new SellException(ResultEnum.CODE_NOT_EXIST);
        }
        AuthCodeBean codeBean2 = service.findOneOpenid(codeForm.getOpenid());
        if (codeBean2 != null) {
            throw new SellException(ResultEnum.USER_HAVE_EXIST);
        }


        AuthCodeBean bean = new AuthCodeBean();
        bean.setCodeId(codeForm.getCodeid());
        bean.setOpenId(codeForm.getOpenid());
        bean.setName(codeForm.getName());
        bean.setPhone(codeForm.getPhone());

        AuthCodeBean bean1 = service.save(bean);
        return ResultApiUtil.success(bean1);
    }

    @GetMapping("/canUseList")
    public ResultApi list() {
        List<AuthCodeBean> allList = service.findAll();
        List<AuthCodeBean> collect = allList.stream()
                .filter(p -> (p.getOpenId() == null || "".equals(p.getOpenId())))//获取所有未使用过的验证码
                .collect(Collectors.toList());
        return ResultApiUtil.success(collect);
    }
}
