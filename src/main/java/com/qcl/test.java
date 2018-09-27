package com.qcl;

import com.qcl.utils.Utils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by qcl on 2018/7/15.
 */
public class test {
    public static void main(String [] strings){
//        System.out.println("MTIzNDU2".equals("MTIzNDU2"));

//        List<String> list=new ArrayList<String>();
//        list.add("111111");
//        list.add("21111111");
//        System.out.println(Arrays.toString(Utils.list2Array(list)));
        String [] array=new String[1];
        array[0]="sagdhghdgh";
        List<String> strings1 = Utils.array2List(array);
        List arrList = new ArrayList(strings1);
        arrList.add("two");
        System.out.println(arrList);

//        String json="{\"access_token\":\"13_d3b3jNcUy2L6PsK31oqra2wxkSzirBVBm84_GvptXJWFExSooZ6jGxDUK9ETH2asIF-JmPS8xUBb3Mjk3HUqnnj6kDq4kEV0sB5kefo83zOYj5gg6rcoAfF2olkJNshzjB8YztDlohp5DyULYLGiAGACPK\",\"expires_in\":7200}"
//
//            ;
//        AccessToken json2=new Gson().fromJson(json, AccessToken.class);
//        System.out.println(json2.getAccess_token());
    }
}
