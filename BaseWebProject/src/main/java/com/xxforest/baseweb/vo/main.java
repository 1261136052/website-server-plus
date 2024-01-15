package com.xxforest.baseweb.vo;

import com.xxforest.baseweb.core.IdTool;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class main {
    public static void main(String[] args) {
        File file = new File("D:\\myJava\\myJava.doc"); //实例化File对象
        boolean dr = file.getParentFile().mkdirs(); //创建目录
        try {
            boolean fr = file.createNewFile(); //创建文件
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    public int[] arrayChange(int[] nums, int[][] operations) {
        Map<Integer,Integer> map = new HashMap<>();
        for(int i = 0;i<nums.length;i++){
            map.put(nums[i],i);
        }

        for (int[] operation : operations) {
            if (map.containsKey(operation[0])){
               int temp0 =  operation[0];
               int temp1 = operation[1];
               int index = map.get(temp0);

                nums[index] = temp1;

                map.remove(temp0,index);
                map.put(temp1,index);
            }
        }
        return nums;
    }

}
