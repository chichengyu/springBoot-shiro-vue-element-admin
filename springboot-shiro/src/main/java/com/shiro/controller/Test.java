package com.shiro.controller;

import java.util.Arrays;

public class Test {
    public static void main(String[] args) {
        String str = "aaabbabbb";
        System.out.println(strCount(str));// a3b2ab3
    }


    private static String strCount(String str){
        String[] strs = str.split("");
        String newStr = strs[0],index = strs[0];
        int count = 0;
        for (int i = 0,len = strs.length; i < len; i++){
            if (strs[i].equals(index)){
                count++;
            }else{
                newStr += count == 1 ? strs[i] : count + strs[i];
                count = 1;
            }
            index = strs[i];
        }
        return newStr + count;
    }



    private static String yasuo(String str) {
        String[] strs = str.split("");
        String newStr = strs[0];
        String index = "";
        int count = 0;
        index = strs[0];
        for (int i = 0; i < strs.length; i ++) {
            if (!strs[i].equals(index)) {
                if (count != 1) {
                    newStr += count + strs[i];
                } else {
                    newStr += strs[i];
                }
                count = 1;
            } else {
                count ++;
            }
            index = strs[i];
        }
        return newStr;
    }
}