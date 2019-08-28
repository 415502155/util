package com.java.utils;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class DateUtils {
	/***
	 * yyyy-MM-dd HH:mm:ss 中HH代表24小时制，hh为12小时制
	 */
	private static String ymd_hms = "yyyy-MM-dd HH:mm:ss";
	private static String ymd = "yyyy-MM-dd";
	
	private static final ThreadLocal<DateFormat> ymd_hms_date_format = new ThreadLocal<DateFormat>() {
		protected DateFormat initialValue() {
			return new SimpleDateFormat(ymd_hms);
		};
	};
	
	private static final ThreadLocal<DateFormat> ymd_date_format = new ThreadLocal<DateFormat>() {
		protected DateFormat initialValue() {
			return new SimpleDateFormat(ymd);
		};
	};
	
    /**
     * 格式化日期 年-月-日  时:分:秒
     * 
     * @param date
     * @return yyyy-MM-dd HH:mm:ss格式的字符串
     */
    public static String ymdhmsFormatDate(Date date) {
        return ymd_hms_date_format.get().format(date);
    }

    /**
     * 格式化日期 年-月-日
     * 
     * @param date
     * @return yyyy-MM-dd格式的字符串
     */
    public static String ymdFormatDate(Date date) {
        return ymd_date_format.get().format(date);
    }
    
    /***
     * 字符串转时间 yyyy-mm-dd HH:mm:ss
     * 
     * @param dateStr
     * @return
     */
    public static Date stringFormatDateYMDHMS(String dateStr) {
    	DateFormat dateFormat = ymd_hms_date_format.get();
    	Date date = null;
    	try {
    		date = dateFormat.parse(dateStr);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return date;
    }
    /***
     * 字符串转时间 yyyy-mm-dd
     * 
     * @param dateStr
     * @return
     */
    public static Date stringFormatDateYMD(String dateStr) {
    	DateFormat dateFormat = ymd_date_format.get();
    	Date date = null;
    	try {
    		date = dateFormat.parse(dateStr);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return date;
    }
    
    /**
     * util.Date转换为sql.Date
     * 
     * @param date
     * @return
     */
    public static java.sql.Date utilToSql(Date date) {
        return new java.sql.Date(date.getTime());
    }
    /***
     * 某日期（date）的前几天（days）或后几天（days）
     * 
     * @param date 日期
     * @param type 0:加   1:减
     * @param days (5:5天后， -5:5天前)
     * @return
     */
    public static String afewDaysBeforeOrAfter(String date, int type, int days) {
    	Calendar calendar = GregorianCalendar.getInstance();
    	String beforeOrAfterDate = null;
    	try {
			//Date parse = ymd_hms_date_format.get().parse(date);
			Date parse = ymd_date_format.get().parse(date);
			calendar.setTime(parse);
			if (type == 0) {
				calendar.add(Calendar.DATE, days);
			} else if (type == 1) {
				calendar.add(Calendar.DATE, days);
			}
			Date time = calendar.getTime();
			beforeOrAfterDate = ymdhmsFormatDate(time);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return beforeOrAfterDate;
    }
    /***
     * 某日期（date）的前几月（months）或后几月（months）
     * 
     * @param date 日期
     * @param type 0:加   1:减
     * @param days (5:5月后， -5:5月天前)
     * @return
     */
    public static String afewMonthsBeforeOrAfter(String date, int type, int months) {
    	Calendar calendar = GregorianCalendar.getInstance();
    	String beforeOrAfterDate = null;
    	try {
			//Date parse = ymd_hms_date_format.get().parse(date);
			Date parse = ymd_date_format.get().parse(date);
			calendar.setTime(parse);
			if (type == 0) {
				calendar.add(Calendar.MONTH, months);
			} else if (type == 1) {
				calendar.add(Calendar.MONTH, months);
			}
			Date time = calendar.getTime();
			beforeOrAfterDate = ymdhmsFormatDate(time);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return beforeOrAfterDate;
    }
    
    /**
     * 返回当前的时间戳
     */
    public static Timestamp getCurrentTimestamp() {
        return new Timestamp(System.currentTimeMillis());
    }
    
    /***
     * 测试
     * @param args
     */
    public static void main(String[] args) {
    	//字符串 yyyy-mm-dd HH:mm:ss转时间 Date
    	Date stringFormatDate = stringFormatDateYMDHMS("2018-09-09 10:10:10");
    	System.out.println(stringFormatDate);
    	//时间 yyyy-mm-dd HH:mm:ss转 字符串 String
    	String ymdhmsFormatDate = ymdhmsFormatDate(stringFormatDate);
    	System.out.println(ymdhmsFormatDate);
    	//某时间之前两天的时间
    	String afewDaysBeforeOrAfter = afewDaysBeforeOrAfter("2019-08-26 10:00:00", 1, -2);
    	System.out.println(afewDaysBeforeOrAfter);
    	//某时间之后两月的时间
    	String afewMonthsBeforeOrAfter = afewMonthsBeforeOrAfter("2019-08-26 10:00:00", 1, 2);
    	System.out.println(afewMonthsBeforeOrAfter);
	}
}
