package com.ike.o2o.until;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;

@Configuration
public class DateUtil {
    //时区
    private static String availableID;
    //日期格式
    private static String dateFormat;

    @Value("${availableID}")
    public void setAvailableID(String availableID) {
        DateUtil.availableID = availableID;
    }

    @Value("${dateFormat}")
    public void setDateFormat(String dateFormat) {
        DateUtil.dateFormat = dateFormat;
    }

    /**
     * 获取当前时间
     *
     * @return 东八区时间
     */
    public static Date getLocalDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        //设置为东八区
        sdf.setTimeZone(TimeZone.getTimeZone(availableID));
        Date date = new Date();
        String dateStr = sdf.format(date);

        //将字符串转成时间
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date newDate = null;
        try {
            newDate = df.parse(dateStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return newDate;
    }

    /**
     * 获取格式化后的日期格式
     *
     * @return 当前日期的字符串
     */
    public static String getLocalDateStr() {
        return new SimpleDateFormat(dateFormat).format(getLocalDate());
    }

    /**
     * 获取当前的时间
     *
     * @return 当前时间
     */
    public static String getLocalTimeStr() {
        return new SimpleDateFormat("HH:mm:ss").format(Calendar.getInstance().getTime());
    }

    /**
     * @param date 截取指定日期对象的时间
     * @return 字符串格式
     */
    public static String getLocalTimeStr(Date date) {
        return new SimpleDateFormat("HH:mm:ss").format(date);
    }

    /**
     * 获取当前日期
     *
     * @return 日期
     */
    public static String getLocalDayStr() {
        return new SimpleDateFormat("yyyy-MM-dd").format(Calendar.getInstance().getTime());
    }

    /**
     * 截取指定时间的日期
     *
     * @param date 日期
     * @return 日期
     */
    public static String getLocalDayStr(Date date) {
        return new SimpleDateFormat("yyyy-MM-dd").format(date);
    }
}
