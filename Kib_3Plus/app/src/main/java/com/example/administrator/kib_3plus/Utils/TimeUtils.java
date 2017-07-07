package com.example.administrator.kib_3plus.Utils;

import android.net.ParseException;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by cui on 2017/7/3.
 */

public class TimeUtils {
    private static TimeUtils mTimeUtils;
    public static TimeUtils getInstance(){
        if(mTimeUtils==null){
            mTimeUtils=new TimeUtils();
        }
        return mTimeUtils;
    }

    private TimeUtils() {
    }

    /**
     * 获取当前时间的date
     * @return
     */
    public Date getTimeDate(){
        Date curDate = new Date(System.currentTimeMillis());//获取当前时间

        return curDate;
    }

    /**
     * 根据传进来的额时间格式返回对应的字符串
     * @param type
     * @return
     */
    public String getTimeType(String type){
        SimpleDateFormat formatter    =   new    SimpleDateFormat(type);
        String    str    =    formatter.format(getTimeDate());
        return str;
    }
    public String getTimeForzone(Locale aLocale) {
        DateFormat df= DateFormat.getDateTimeInstance(DateFormat.FULL,DateFormat.FULL, aLocale);
        System.out.println(df.format(new Date()));
        return df.format(new Date());
    }
    //
    /**
     * 将时间戳转为字符串

     * @param cc_time 时间戳
     * @param type"yyyy年MM月dd日HH时mm分ss秒"
     * @return
     */
    public String getStrTime(long cc_time,String type) {
        String re_StrTime = null;
        SimpleDateFormat sdf = new SimpleDateFormat(type);
        // 例如：
        re_StrTime = sdf.format(new Date(cc_time * 1000L));
        return re_StrTime;
    }

    // 将字符串转为时间戳

    /**
     *  将字符串转为时间戳
     * @param user_time 时间格式
     * @param type "yyyy年MM月dd日HH时mm分ss秒"
     * @return
     */
    public static long getTime(String user_time,String type) {
        String re_time = null;
        SimpleDateFormat sdf = new SimpleDateFormat(type);
        Date d;
        try {
            d = sdf.parse(user_time);
            long l = d.getTime();
            String str = String.valueOf(l);
            re_time = str.substring(0, 10);
        }catch (Exception e) {
            // TODO Auto-generated catch block e.printStackTrace();
        }
        long lcc_time = Long.valueOf(re_time);
        return lcc_time;
    }

}
