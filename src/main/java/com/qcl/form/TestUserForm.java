package com.qcl.form;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

import lombok.Data;

/**
 * Created by qcl on 2018/3/18.
 * 用来验证用户注册提交的表单字段----测试学习用
 */
@Data
public class TestUserForm {
    @NotEmpty(message = "用户名名不能为空")
    private String name;
    @NotEmpty(message = "请填写密码")
    @Length(min = 6, message = "密码长度不能小于6位")
    private String password;
}
