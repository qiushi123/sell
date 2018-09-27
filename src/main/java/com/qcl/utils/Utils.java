package com.qcl.utils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by qcl on 2018/9/10.
 */
public class Utils {

    //strnig转集合
    public static List String2List(String jsonList) {
        List<String> list = new Gson().fromJson(jsonList, new TypeToken<List<String>>() {
        }.getType
                ());
        return list;
    }

    //集合转string
    public static String List2String(List list) {
        return new Gson().toJson(list);
    }


    /*
    * 数组和集合之间相互转化
    * */
    public static String[] list2Array(List<String> list) {
        String[] array = new String[list.size()];
        array = list.toArray(array);
        return array;
    }

    public static List<String> array2List(String[] strArray) {
        return Arrays.asList(strArray);
    }


    /*
    * 足迹星球用到
    * */
    //获取两个时间的时间差
    public static String getDatePoor(Date endDate, Date nowDate) {

        long nd = 1000 * 24 * 60 * 60;
        long nh = 1000 * 60 * 60;
        long nm = 1000 * 60;
        // long ns = 1000;
        // 获得两个时间的毫秒时间差异
        long diff = endDate.getTime() - nowDate.getTime();
        // 计算差多少天
        long day = diff / nd;
        // 计算差多少小时
        long hour = diff % nd / nh;
        // 计算差多少分钟
        long min = diff % nd % nh / nm;
        // 计算差多少秒//输出结果
        // long sec = diff % nd % nh % nm / ns;
        return day + "天" + hour + "小时" + min + "分钟";
    }

    //获取当前时间所在年份
    public static String getSysYear() {
        Calendar date = Calendar.getInstance();
        String year = String.valueOf(date.get(Calendar.YEAR));
        return year;
    }

}
