package com.qcl.form;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import lombok.Data;

/**
 * Created by qcl on 2018/3/18.
 * 用来验证用户注册提交的表单字段
 */
@Data
public class UserForm {
    //微信id
    @NotEmpty(message = "微信id必传")
    private String openid;
    @NotEmpty(message = "姓名不能为空")
    private String name;
    @NotEmpty(message = "手机号不能为空")
    @Size(min = 11, max = 11, message = "手机号必须是11位")
    @Pattern(regexp = "^((13[0-9])|(14[5|7])|(15([0-3]|[5-9]))|(18[0,5-9]))\\d{8}$", message = "手机号码格式错误")
    private String phone;
    @NotEmpty(message = "请填写密码")
    @Length(min = 6, message = "密码长度不能小于6位")
    private String password;
    //    @NotEmpty(message = "收货地址必传")
    //    private String address;
}
