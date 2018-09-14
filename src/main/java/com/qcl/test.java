package com.qcl;

import com.google.gson.Gson;
import com.qcl.userwechat.bean.AccessToken;

/**
 * Created by qcl on 2018/7/15.
 */
public class test {
    public static void main(String [] strings){
//        System.out.println("MTIzNDU2".equals("MTIzNDU2"));

//        ArrayList list=new ArrayList();
//        list.add("111111");
//        list.add("21111111");
//        System.out.println(Utils.List2String(list));

        String json="{\"access_token\":\"13_d3b3jNcUy2L6PsK31oqra2wxkSzirBVBm84_GvptXJWFExSooZ6jGxDUK9ETH2asIF-JmPS8xUBb3Mjk3HUqnnj6kDq4kEV0sB5kefo83zOYj5gg6rcoAfF2olkJNshzjB8YztDlohp5DyULYLGiAGACPK\",\"expires_in\":7200}"

            ;
        AccessToken json2=new Gson().fromJson(json, AccessToken.class);
        System.out.println(json2.getAccess_token());
    }
}
