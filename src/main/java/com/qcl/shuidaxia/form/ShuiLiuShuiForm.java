package com.qcl.shuidaxia.form;

import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;

import lombok.Data;

/**
 * Created by qcl on 2018/3/18.
 * 用来验证用户注册提交的表单字段
 */
@Data
public class ShuiLiuShuiForm {
    private String orderId;//如果订单已经存在就更新订单
    @NotNull(message = "员工编号不能为空")
    private Long staffId;
    @NotEmpty(message = "员工姓名不能为空")
    private String staffName;
    @NotEmpty(message = "流水时间不能为空")
    private String time;
    private String shoukuanleibie;//收款类别
    private Integer shoukuanjine;//收款金额
    private String zhichuleibie;//支出类别
    private Integer zhichujine;//支出金额
    private String yewutichengxinxi;//业务提成信息
    private Integer tichengjine;//提成金额
    private String qingjia;//请假
    private Integer fakuan;//交罚款
    private Integer dixin;//底薪
}
