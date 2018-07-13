package com.qcl.shuidaxia.form;

import org.hibernate.validator.constraints.NotEmpty;

import lombok.Data;

/**
 * Created by qcl on 2018/3/18.
 * 用来验证用户注册提交的表单字段
 */
@Data
public class ShuiVipForm {

    private Long userId;
    @NotEmpty(message = "姓名不能为空")
    private String userName;
    @NotEmpty(message = "联系方式不能为空")
    //    @Size(min = 11, max = 11, message = "手机号必须是11位")
    private String userPhone;
    @NotEmpty(message = "配送地址不能为空")
    private String userAdderss;
    @NotEmpty(message = "购买时间不能为空")
    private String buyTime;
    @NotEmpty(message = "套餐类型不能为空")
    private String buyType;
    @NotEmpty(message = "收款类型不能为空")
    private String shoukuanType;
    @NotEmpty(message = "收款人不能为空")
    private String shoukuanren;
    private String userFrom;

    private String tuidingTime;
    private String tuidingjieguo;
    private String tuidingyuanyin;
    private String beizhu;

    private Integer yinshuijiNum;
    private Integer yanjin;
    private Integer shouciPeiSongNum;

}
