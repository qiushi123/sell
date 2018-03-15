package com.qcl.utils;

import java.util.Random;

/**
 * Created by qcl on 2018/3/14.
 */
public class KeyUtil {
    /*
    * 生成唯一的主键
    * 格式：时间+随机数
    * synchronized在多线程时避免重复
    * */
    public static synchronized String genUniqueKey() {
        Random random = new Random();
        Integer number = random.nextInt(900000) + 100000;
        return System.currentTimeMillis() + String.valueOf(number);
    }
}
