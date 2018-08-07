package com.qcl.utils;

import com.qcl.huishou.bean.HuishouOrder;
import com.qcl.paotui.bean.RunOrder;

import java.util.List;

/**
 * Created by qcl on 2018/7/9.
 * 保护用户隐私信息的工具类
 */
public class ProtectUserUtils {

    //跑腿保护用户订单信息
    public static List<RunOrder> protectUserOrders(List<RunOrder> orders) {
        orders.stream().forEach(runOrder -> {
            runOrder.setBuyerName(protectUserName(runOrder.getBuyerName()));
            runOrder.setBuyerPhone(protectUserPhone(runOrder.getBuyerPhone()));
            runOrder.setBuyerAdderss(protectUserAddress(runOrder.getBuyerAdderss()));
        });
        return orders;
    }

    //回收保护用户订单信息
    public static List<HuishouOrder> protectHuishouUserOrders(List<HuishouOrder> orders) {
        orders.stream().forEach(runOrder -> {
            runOrder.setBuyerName(protectUserName(runOrder.getBuyerName()));
            runOrder.setBuyerPhone(protectUserPhone(runOrder.getBuyerPhone()));
            runOrder.setBuyerAdderss(protectUserAddress(runOrder.getBuyerAdderss()));
        });
        return orders;
    }


    //保护用户姓名
    public static String protectUserName(String name) {
        if (name == null) {
            return "";
        }
        String realname = "";
        char[] r = name.toCharArray();
        if (r.length == 1) {
            realname = name;
        }
        if (r.length == 2) {
            realname = name.replaceFirst(name.substring(1), "*");
        }
        if (r.length > 2) {
            realname = name.replaceFirst(name.substring(1, r.length - 1), "*");
        }
        return realname;
    }


    //保护用户手机号
    public static String protectUserPhone(String mobile) {
        if (mobile == null) {
            return "";
        }
        char[] m = mobile.toCharArray();
        for (int i = 0; i < m.length; i++) {
            if (i > 2 && i < 7) {
                m[i] = '*';
            }
        }
        return String.valueOf(m);
    }

    //保护用户地址
    public static String protectUserAddress(String address) {
        if (address == null) {
            return "";
        }
        char[] m = address.toCharArray();
        for (int i = 0; i < m.length; i++) {
            if (i > m.length - 6) {//隐藏最后6为
                m[i] = '*';
            }
        }
        return String.valueOf(m);
    }
}
