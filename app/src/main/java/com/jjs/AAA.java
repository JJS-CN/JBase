package com.jjs;

/**
 * 说明：
 * Created by aa on 2017/7/24.
 */

public class AAA {
    public static void main(String[] args) {
        String idcardNo="362429199302222514";
        StringBuffer buffer=new StringBuffer(idcardNo);
        buffer.replace(4,idcardNo.length()-4,"**********");
        System.out.print(buffer.toString());
    }
}
