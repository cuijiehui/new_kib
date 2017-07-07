package com.example.administrator.kib_3plus;

import android.content.Context;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by cui on 2017/6/10.
 */

public class PublicData {
    /**
     * 初始化一些数据，如身高，体重
     * @param context
     */
    public static void InitDragListData(Context context) {
        left_height_imperilal_Int=new ArrayList<>();
        for(int i=0;i<LEFT_IMPERILAL_INT_NUM;i++){
            left_height_imperilal_Int.add(LEFT_IMPERILAL_INT_START+i+"'");
        }
        right_height_imperilal_Int=new ArrayList<>();
        for(int i=0;i<RIGHT_IMPERILAL_INT_NUM;i++){
            right_height_imperilal_Int.add(RIGHT_IMPERILAL_INT_START+i+"\"");
        }

        left_height_metric_Int=new ArrayList<>();
        for(int i=0;i<LEFT_METRIC_INT_NUM;i++){
            left_height_metric_Int.add(LEFT_METRIC_INT_START+i+"");
        }
        height_metric_Int=new ArrayList<>();
        for(int i=0;i<RIGHT_METRIC_INT_NUM;i++){
            int num=RIGHT_METRIC_INT_START+i;
            height_metric_Int.add("."+num);
        }

        left_weight_imperilal_Int=new ArrayList<>();
        for(int i=0;i<LEFT_WEIGHT_IMPERILAL_INT_NUM;i++){
            left_weight_imperilal_Int.add(LEFT_WEIGHT_IMPERILAL_INT_START+i+"");
        }
        left_weight_metric_Int=new ArrayList<>();
        for(int i=0;i<LEFT_WEIGHT_METRIC_INT_NUM;i++){
            left_weight_metric_Int.add(LEFT_WEIGHT_METRIC_INT_START+i+"");
        }

    }
    public static boolean weatherPrint=true;
/***********************体重身高*********************************/
    public static List<String> left_height_imperilal_Int;
    public final static int LEFT_IMPERILAL_INT_START = 3;
    public final static int LEFT_IMPERILAL_INT_NUM = 5;
    public static List<String> right_height_imperilal_Int;
    public final static int RIGHT_IMPERILAL_INT_START = 0;
    public final static int RIGHT_IMPERILAL_INT_NUM = 12;

    public static List<String> left_height_metric_Int;
    public final static int LEFT_METRIC_INT_START = 90;
    public final static int LEFT_METRIC_INT_NUM = 151;

    //这个是可以共用的
    public static List<String> height_metric_Int;
    public final static int RIGHT_METRIC_INT_START = 0;
    public final static int RIGHT_METRIC_INT_NUM = 10;

    public static List<String> left_weight_imperilal_Int;
    public final static int LEFT_WEIGHT_IMPERILAL_INT_START = 70;
    public final static int LEFT_WEIGHT_IMPERILAL_INT_NUM = 901;

    public static List<String> left_weight_metric_Int;
    public final static int LEFT_WEIGHT_METRIC_INT_START = 30;
    public final static int LEFT_WEIGHT_METRIC_INT_NUM = 371;


/**********************************************************************/
    public static boolean sleepState=false;                                                         //睡眠状态，默认为false
}
