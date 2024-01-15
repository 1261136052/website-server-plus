package com.xxforest.baseweb.core;

import java.util.ArrayList;
import java.util.List;

public class Filter {
    private static  List<String> data;
    private Filter(){}
    public static List<String> getInstance(){

        if (data==null){
            data = new ArrayList<>();
            data.add("过滤");
        }
        return data;
    }


    public static boolean isContentValid(String content) {
        if (data==null){
            getInstance();
        }
        for (String word : data) {
            if (content.contains(word)) {
                return false; // 输入内容包含敏感词，不合法
            }
        }
        return true; // 输入内容合法
    }



}
