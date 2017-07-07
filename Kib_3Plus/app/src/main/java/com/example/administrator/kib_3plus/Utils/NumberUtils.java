package com.example.administrator.kib_3plus.Utils;

import android.content.Context;

import java.security.MessageDigest;
import java.text.SimpleDateFormat;
import java.util.Date;


import static java.lang.Integer.parseInt;

/**
 * Created by cui on 2017/6/19.
 */

public class NumberUtils {
    private static NumberUtils numberUtils;
    public static NumberUtils getInstance(){
        if(numberUtils==null){
            numberUtils=new NumberUtils();
        }
        return numberUtils;

    }
    public int dip2px(Context context, float dipValue) {
        if(context!=null){
            final float scale = context.getResources().getDisplayMetrics().density;
            return (int) (dipValue * scale + 0.5f);
        }else {
            return 0;
        }
    }

    /**
     * @param date
     * @return 返回当前年String格式：yyyy
     */
    public static String getCurYear(Date date) {

        return formatDate2(new Date()).substring(0, 4);
    }

    /**
     * @param date
     * @return 返回String格式：yyyy-MM-dd
     */
    public static String formatDate2(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        return sdf.format(date);
    }
    /**
     * 获得当前年份所在的月份
     *
     * @param date
     * @return MM
     */
    public static String getCurMonthOfYear(Date date) {
        return formatDate2(new Date()).substring(5, 7);
    }
    /**
     * 获得当前月份所在的天
     *
     * @param date
     * @return dd
     */
    public static String getCurDayOfMonth(Date date) {
        return formatDate2(new Date()).substring(8, 10);
    }
    //1尺=12 寸、1寸=2.54cm
    public static double ftToCm(String ftNum){
       int cm= Integer.parseInt(ftNum.split("'")[0])*12;
        int indexS=ftNum.indexOf("'");
        int indexN=ftNum.indexOf("\"");
        LogUtils.i("indexS="+indexS+"--indexN="+indexN+"--cm="+cm);
        cm=Integer.parseInt(ftNum.substring(indexS+1,indexN))+cm;
        LogUtils.i("cm="+cm);
        return cm*2.54;
    }
    public static String ftToCmLeft(String ftNum){
        int cm= Integer.parseInt(ftNum.split("'")[0])*12;
        int indexS=ftNum.indexOf("'");
        int indexN=ftNum.indexOf("\"");
        LogUtils.i("indexS="+indexS+"--indexN="+indexN+"--cm="+cm);
        cm=Integer.parseInt(ftNum.substring(indexS+1,indexN))+cm;
        LogUtils.i("cm="+cm);
        String leftCm=((int)(cm*2.54))+"";
        return leftCm;
    }
    public static String ftToCmRight(String ftNum){
        int cm= Integer.parseInt(ftNum.split("'")[0])*12;
        int indexS=ftNum.indexOf("'");
        int indexN=ftNum.indexOf("\"");
        LogUtils.i("indexS="+indexS+"--indexN="+indexN+"--cm="+cm);
        cm=Integer.parseInt(ftNum.substring(indexS+1,indexN))+cm;
        LogUtils.i("cm="+cm);
        String rightCm=(cm*2.54)+"";
        int index=rightCm.indexOf(".");
        rightCm= rightCm.substring(index,index+1);
        return rightCm;
    }
    public static String cmToFt(String cmNum){
        double cm=new Double(cmNum);
        int ft= (int)(double)(cm * 0.3937008);
        int a1 = ft/12;
        int a2 = ft%12;
        String ftt=a1 + "'" + a2 + "\" ";
        return ftt;
    }
    public static String cmToFtRight(String cmNum){
        double cm=new Double(cmNum);

        int ft= (int)(double)(cm * 0.3937008);
        int a2 = ft%12;
        String ftt= a2 + "\" ";
        return ftt;
    }
    public static String cmToFtLeft(String cmNum){
        double cm=new Double(cmNum);

        int ft= (int)(double)(cm * 0.3937008);
        int a1 = ft/12;
        String ftt=a1 + "'" ;
        return ftt;
    }

    public static double lbsToKg(String lbsNum){
        double lbss=new Double(lbsNum);
        double lbs= (double)(lbss * 0.4532);
        return lbs;
    }
    public static String lbsToKgLeft(String lbsNum){
        double lbss=new Double(lbsNum);
        double lbs= (double)(lbss * 0.4532);
        int index=(lbs+"").indexOf(".");
        String leftLbs=(lbs+"").substring(0,index);
        return leftLbs;
    }
    public static String lbsToKgRight(String lbsNum){
        double lbss=new Double(lbsNum);
        double lbs= (double)(lbss * 0.4532);
        int index=(lbs+"").indexOf(".");
        String leftLbs=(lbs+"").substring(index,index+1);
        return leftLbs;
    }

    public static double kgToLbs(String kgNum){
        double kgg=new Double(kgNum);
        double kg= (int)(double)(kgg * 2.2065);
        return kg;
    }
    public static String kgToLbsLeft(String kgNum){
        double kgg=new Double(kgNum);
        double kg= (int)(double)(kgg * 2.2065);
        int index=(kg+"").indexOf(".");
        String leftKg=(kg+"").substring(0,index);
        return leftKg;
    }
    public static String kgToLbsRight(String kgNum){
        double kgg=new Double(kgNum);
        double kg= (int)(double)(kgg * 2.2065);
        int index=(kg+"").indexOf(".");
        String rightKg=(kg+"").substring(index,index+1);
        return rightKg;
    }

    /* MD5进行密码加密*/
    public static String MD5(String inStr) {
        MessageDigest md5 = null;
        try {
            md5 = MessageDigest.getInstance("MD5");
        } catch (Exception e) {
            System.out.println(e.toString());
            e.printStackTrace();
            return "";
        }
        char[] charArray = inStr.toCharArray();
        byte[] byteArray = new byte[charArray.length];

        for (int i = 0; i < charArray.length; i++)
            byteArray[i] = (byte) charArray[i];

        byte[] md5Bytes = md5.digest(byteArray);

        StringBuffer hexValue = new StringBuffer();

        for (int i = 0; i < md5Bytes.length; i++) {
            int val = ((int) md5Bytes[i]) & 0xff;
            if (val < 16)
                hexValue.append("0");
            hexValue.append(Integer.toHexString(val));
        }

        return hexValue.toString();
    }
    // int 类型转byte[] 2进制数组
    public static byte[] intToByteArray(int i) {
        byte[] result = new byte[4];
        result[0] = (byte) ((i >> 24) & 0xFF);
        result[1] = (byte) ((i >> 16) & 0xFF);
        result[2] = (byte) ((i >> 8) & 0xFF);
        result[3] = (byte) (i & 0xFF);
        return result;
    }
}
