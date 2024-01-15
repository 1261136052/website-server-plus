package com.xxforest.baseweb.dto;

import java.util.Scanner;

public class Main {
    public static void main(String[] args){
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        int[] arr = new int [n];
        for(int i = 0; i < n; i ++) {
            arr[i] = scanner.nextInt();
        }
        scanner.close();
        quickSort(arr, 0, n - 1);
        print(arr);
    }

    /* 请在这里填写方法 */
    public static int quickSort(int[] arr, int low, int high) {
        int pivot = arr[low]; // 选取第一个元素为枢轴元素
        while (low < high) {
            while (low < high && arr[high] >= pivot) {
                high--;
            }
            arr[low] = arr[high];
            while (low < high && arr[low] <= pivot) {
                low++;
            }
            arr[high] = arr[low];
        }
        arr[low] = pivot; // 将pivot放到最终的位置
        return low;
    }

    public static void print(int[] arr) {
        for (int i : arr) {
            System.out.print(i + " ");
        }
        System.out.println("");
    }
}