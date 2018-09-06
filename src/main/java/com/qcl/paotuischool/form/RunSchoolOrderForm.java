package com.qcl.paotuischool.form;

import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import lombok.Data;

/**
 * Created by qcl on 2018/3/18.
 * 用来验证用户注册提交的表单字段
 */
@Data
public class RunSchoolOrderForm {
    private Integer orderType;//0代取快递，1代寄快递
    @NotEmpty(message = "微信id必传")
    private String openid;
    @NotEmpty(message = "姓名不能为空")
    private String name;
    @Pattern(regexp = "^(13[0-9]|14[579]|15[0-3,5-9]|16[6]|17[0135678]|18[0-9]|19[89])\\d{8}$", message = "手机号码格式错误")
    @NotEmpty(message = "手机号不能为空")
    @Size(min = 11, max = 11, message = "手机号必须是11位")
    private String phone;

    @NotEmpty(message = "送到哪儿不能为空")
    private String address;
    @NotEmpty(message = "请选择校区")
    private String school;//城市很重要，跑腿员只能看到同城市的订单
    @NotEmpty(message = "去哪儿取和物品描述不能为空")
    private String fromAddress;
    @NotEmpty(message = "请复制取件短信")
    private String noteContent;
    @NotNull(message = "请选择物品重量")
    private Integer expressType;
    @NotNull(message = "代取费用不能为空")
    private Float totalMoney;

    private boolean isJiaJi;//是否加急
    private boolean isShangLou;//是否送上楼
    private String yundanhao;//运单号
    private String songdaTime;//期望送达时间
    private String beizhu;
}
