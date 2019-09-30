package com.java.utils;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import com.alibaba.fastjson.JSONObject;

public class DateUtils {
	/***
	 * yyyy-MM-dd HH:mm:ss 中HH代表24小时制，hh为12小时制
	 */
	private static String ymd_hms = "yyyy-MM-dd HH:mm:ss";
	private static String ymd = "yyyy-MM-dd";
	private static String ms = "mm:ss";
	
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
	
	private static final ThreadLocal<DateFormat> ms_date_format = new ThreadLocal<DateFormat>() {
		protected DateFormat initialValue() {
			return new SimpleDateFormat(ms);
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
    
    public static String msFormatDate(Date date) {
        return ms_date_format.get().format(date);
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
     *@ 返回指定日期时间段的所有时间
     *@param start
     *@param end
     *@return ["2019-02-25","2019-02-26","2019-02-27","2019-02-28","2019-03-01","2019-03-02","2019-03-03"]
     */
    public static List<String> betweenStartAndEndDate(Date start, Date end) {
    	List<String> stringDate = new ArrayList<String>();
        stringDate.add(ymdFormatDate(start));
        Calendar calBegin = Calendar.getInstance();
        // 使用给定的 Date 设置此 Calendar 的时间     
        calBegin.setTime(start);
        Calendar calEnd = Calendar.getInstance();
        // 使用给定的 Date 设置此 Calendar 的时间     
        calEnd.setTime(end);
        // 测试此日期是否在指定日期之后     
        while (end.after(calBegin.getTime())) {
            // 根据日历的规则，为给定的日历字段添加或减去指定的时间量     
            calBegin.add(Calendar.DAY_OF_MONTH, 1);
            stringDate.add(ymdFormatDate(calBegin.getTime()));
        }
    	return stringDate;
    }
    /***
     * @返回当前时间前一天
     * @return
     */
    public static String yesterdayYMD() {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, -1);
        String yesterday = ymdFormatDate(cal.getTime());
        return yesterday;
    }
    /***
     * 
     * @param start
     * @param end
     * @return
     * @throws ParseException
     */
    public static int daysBetween(Date start, Date end) throws ParseException {
        //年月日格式的date
        String startDate = ymdFormatDate(start);
        String endDate = ymdFormatDate(end);
        Calendar cal = Calendar.getInstance();
        cal.setTime(start);
        long time1 = cal.getTimeInMillis();
        cal.setTime(end);
        long time2 = cal.getTimeInMillis();
        long between_days = (time2 - time1) / (1000 * 3600 * 24);
        return Integer.parseInt(String.valueOf(between_days));
    }

    /**
     * 设置cal日期为该日开始时刻
     *
     * @param cal
     * @return
     */
    public static String setStartOfDay(Date day) {
    	Calendar cal = Calendar.getInstance();
        cal.setTime(day);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        Date time = cal.getTime();
        return ymdhmsFormatDate(time);
    }

    /**
     * @设置cal日期为该日最后时刻
     *
     * @param day
     * @return
     */
    public static String setEndOfDay(Date day) {
    	Calendar cal = Calendar.getInstance();
        cal.setTime(day);
        cal.set(Calendar.HOUR_OF_DAY, 23);
        cal.set(Calendar.MINUTE, 59);
        cal.set(Calendar.SECOND, 59);
        Date time = cal.getTime();
        return ymdhmsFormatDate(time);
    }
    
    /**
     * @date转Calendar实例
     *
     * @param date
     * @return
     */
    public static Calendar calendarOf(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return cal;
    }

    /**
     * @毫秒转Calendar实例
     *
     * @param millseconds
     * @return
     */
    public static Calendar calendarOf(long millseconds) {
        return calendarOf(new Date(millseconds));
    }
    
    /***
     * @测试
     * @param args
     * @throws ParseException 
     */
    @SuppressWarnings("static-access")
	public static void main(String[] args) throws ParseException {
    	//設置某時間當天最晚時間
    	String setEndOfDay = setEndOfDay(new Date());
    	System.out.println("今天的最後時間 ：" + setEndOfDay);
    	
    	//設置某時間當天開始時間
    	String setStartOfDay = setStartOfDay(new Date());
    	System.out.println("今天的開始時間 ：" + setStartOfDay);
    	
    	//返回指定日期时间段的所有时间
    	String start = "2019-02-25";
    	String end = "2019-03-03";
    	Date startYMD = stringFormatDateYMD(start);
    	Date endYMD = stringFormatDateYMD(end);
    	List<String> betweenStartAndEndDate = betweenStartAndEndDate(startYMD, endYMD);
    	JSONObject jsonObject = new JSONObject();
    	System.out.println(jsonObject.toJSONString(betweenStartAndEndDate));
    	System.out.println(yesterdayYMD());
    	System.out.println(daysBetween(startYMD, endYMD));
    	//字符串 yyyy-mm-dd HH:mm:ss转时间 Date
		/*
		 * Date stringFormatDate = stringFormatDateYMDHMS("2018-09-09 10:10:10");
		 * System.out.println(stringFormatDate);
		 */
    	
    	//时间 yyyy-mm-dd HH:mm:ss转 字符串 String
		/*
		 * String ymdhmsFormatDate = ymdhmsFormatDate(stringFormatDate);
		 * System.out.println(ymdhmsFormatDate);
		 */
    	
    	//某时间之前两天的时间
		/*
		 * String afewDaysBeforeOrAfter = afewDaysBeforeOrAfter("2019-08-26 10:00:00",
		 * 1, -2); System.out.println(afewDaysBeforeOrAfter);
		 */
    	
    	//某时间之后两月的时间
		/*
		 * String afewMonthsBeforeOrAfter =
		 * afewMonthsBeforeOrAfter("2019-08-26 10:00:00", 1, 2);
		 * System.out.println(afewMonthsBeforeOrAfter);
		 */
	}
}
