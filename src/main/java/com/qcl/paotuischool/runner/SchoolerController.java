package com.qcl.paotuischool.runner;

import com.qcl.api.ResultApi;
import com.qcl.enums.ResultEnum;
import com.qcl.exception.SellException;
import com.qcl.paotuischool.bean.SchoolRunner;
import com.qcl.paotuischool.form.RunnerForm;
import com.qcl.utils.ResultApiUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

import lombok.extern.slf4j.Slf4j;

/**
 * 跑腿
 */

@RestController
@RequestMapping("/schooler")
@Slf4j
public class SchoolerController {

    @Autowired
    private SchoolerService service;


    //根据openid获取跑腿员的用户信息
    @GetMapping("/openid")
    public ResultApi findOne(@RequestParam(name = "openid") String openid) {
        SchoolRunner bean = service.findOneOpenid(openid);
        return ResultApiUtil.success(bean);
    }


    /**
     * 用户注册或重置信息
     * 使用BindingResult校验参数
     *
     * @param runnerForm
     * @param bindingResult
     * @return
     */
    @PostMapping("/register")
    public ResultApi save(@Valid RunnerForm runnerForm, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            log.error("[注册用户] 参数不正确，runnerForm={}", runnerForm);
            throw new SellException(ResultEnum.PARAM_ERROR.getCode()
                    , bindingResult.getFieldError().getDefaultMessage());
        }



        SchoolRunner bean = new SchoolRunner();
        bean.setOpenId(runnerForm.getOpenid());
        bean.setName(runnerForm.getName());
        bean.setPhone(runnerForm.getPhone());
        bean.setCardId(runnerForm.getCardid());
        bean.setAddress(runnerForm.getAddress());
        bean.setType(1);//默认新注册用户都处于审核中的状态

        SchoolRunner bean1 = service.save(bean);
        return ResultApiUtil.success(bean1);
    }


}
