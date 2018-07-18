package com.qcl.shuidaxia.form;

import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.Pattern;

import lombok.Data;

/**
 * Created by qcl on 2018/3/18.
 * 用来验证用户注册提交的表单字段
 */
@Data
public class ShuiAdminForm {
    private Long adminId;
    @NotEmpty(message = "员工姓名不能为空")
    private String adminName;
    //    @NotEmpty(message = "员工密码不能为空")
    private String adminPassword;
    @NotEmpty(message = "员工联系方式不能为空")
    //    @Size(min = 11, max = 11, message = "手机号必须是11位")
    private String adminPhone;
    @Pattern(regexp = "(^[1-9]\\d{5}(18|19|20)\\d{2}((0[1-9])|(10|11|12))(([0-2][1-9])|10|20|30|31)\\d{3}[0-9Xx]$)|" +
            "(^[1-9]\\d{5}\\d{2}((0[1-9])|(10|11|12))(([0-2][1-9])|10|20|30|31)\\d{3}$)", message = "身份证格式错误")
    @NotEmpty(message = "身份证号必传")
    private String adminCardId;

    private String adminPhoneBeiYong;//员工备用手机
    private String adminFromTime;
    private String adminOutTime;
    private String beizhu;

    private Integer adminType;//-1离职管理员，0普通会员不是管理员，1管理员，2超级管理员（可以管理管理员和员工）

}
