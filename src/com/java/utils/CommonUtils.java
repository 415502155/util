package com.java.utils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Random;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
/***
 * 通用工具类
 * @author sjwy-0001
 *
 */
public class CommonUtils {

	public static String getUUID() {
		return UUID.randomUUID().toString().replaceAll("-", "");
	}

	/**
	 * 格式化日期
	 * 
	 * @param date
	 * @param pattern 如果为null则默认格式为yyyy-MM-dd HH:mm:ss
	 * @return
	 */
	public static String dateFormat(Date date, String pattern) {
		if (pattern == null) {
			pattern = "yyyy-MM-dd HH:mm:ss";
		}
		return new SimpleDateFormat(pattern).format(date);
	}

	/**
	 * 格式化日期
	 * 
	 * @param date
	 * @param pattern 如果为null则默认格式为yyyy-MM-dd HH:mm:ss
	 * @return
	 */
	public static String dateFormat(Timestamp date, String pattern) {
		if (pattern == null) {
			pattern = "yyyy-MM-dd HH:mm:ss";
		}
		return new SimpleDateFormat(pattern).format(date);
	}

	/**
	 * 格式化日期
	 * 
	 * @param date
	 * @param pattern 如果为null则默认格式为yyyy-MM-dd HH:mm:ss
	 * @return
	 */
	public static String dateFormat(String date, String pattern) throws ParseException {
		if (pattern == null) {
			pattern = "yyyy-MM-dd HH:mm:ss";
		}
		Date d = stringToDate(date, pattern);
		return new SimpleDateFormat(pattern).format(d);
	}

	/**
	 * 将String类型的日期格式化为Date类型
	 * 
	 * @param date
	 * @param pattern 如果为null则默认格式为yyyy-MM-dd HH:mm:ss
	 * @return
	 * @throws ParseException
	 */
	public static Date stringToDate(String date, String pattern) throws ParseException {
		if (pattern == null) {
			pattern = "yyyy-MM-dd HH:mm:ss";
		}
		DateFormat formater = new SimpleDateFormat(pattern);
		return formater.parse(date);
	}

	/**
	 * 格式化数字 自动省去末尾0
	 * 
	 * @param num
	 * @param value 保留的小数位
	 * @return
	 */
	public static String numberFormat(double num, int value) {
		NumberFormat formater = DecimalFormat.getInstance();
		formater.setMaximumFractionDigits(value);
		formater.setGroupingUsed(false);
		return formater.format(num);
	}

	/**
	 * 格式化数字 自动省去末尾0
	 * 
	 * @param str
	 * @param value 保留的小数位
	 * @return
	 */
	public static String numberFormat(String str, int value) {
		double num = Double.parseDouble(str);
		NumberFormat formater = DecimalFormat.getInstance();
		formater.setMaximumFractionDigits(value);
		formater.setGroupingUsed(false);
		return formater.format(num);
	}

	/**
	 * 格式化数字 自动省去末尾0
	 * 
	 * @param str
	 * @param value 保留的小数位
	 * @return
	 */
	public static String numberFormat(Object str, int value) {
		NumberFormat formater = DecimalFormat.getInstance();
		formater.setMaximumFractionDigits(value);
		formater.setGroupingUsed(false);
		return formater.format(str);
	}

	/**
	 * 格式化数字
	 * 
	 * @param str
	 * @param formatStyle 传入null时默认为0.00
	 * @return
	 */
	public static String numberFormat(String str, String formatStyle) {
		if (str == null || ("").equals(str)) {
			return "";
		}
		if (formatStyle == null) {
			formatStyle = "0.00";
		}
		Double num = Double.parseDouble(str);
		DecimalFormat formater = new DecimalFormat(formatStyle);
		formater.setRoundingMode(RoundingMode.HALF_UP);
		return formater.format(num);
	}

	/**
	 * 格式化数字
	 * 
	 * @param num
	 * @param formatStyle 传入null时默认为0.00
	 * @return
	 */
	public static String numberFormat(double num, String formatStyle) {
		if (formatStyle == null) {
			formatStyle = "0.00";
		}
		DecimalFormat formater = new DecimalFormat(formatStyle);
		formater.setRoundingMode(RoundingMode.HALF_UP);
		return formater.format(num);
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

	/**
	 * 返回传入的日期的前N天或后N天的日期
	 * 
	 * @param appDate
	 * @param format
	 * @param days
	 * @return
	 */
	public static String getFutureDay(String appDate, String format, int days) {
		String future = "";
		try {
			Calendar calendar = GregorianCalendar.getInstance();
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
			Date date = simpleDateFormat.parse(appDate);
			calendar.setTime(date);
			calendar.add(Calendar.DATE, days);
			date = calendar.getTime();
			future = simpleDateFormat.format(date);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return future;
	}

	/**
	 * 返回传入的日期的前N个月或后N个月的日期
	 * 
	 * @param appDate
	 * @param format
	 * @param months
	 * @return
	 */
	public static String getFutureMonth(String appDate, String format, int months) {
		String future = "";
		try {
			Calendar calendar = GregorianCalendar.getInstance();
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
			Date date = simpleDateFormat.parse(appDate);
			calendar.setTime(date);
			calendar.add(Calendar.MONTH, months);
			date = calendar.getTime();
			future = simpleDateFormat.format(date);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return future;
	}

	/**
	 * 返回当前的时间戳
	 */
	public static Timestamp getCurrentTimestamp() {
		return new Timestamp(System.currentTimeMillis());
	}

	/**
	 * 取得随机字符串
	 * 
	 * @param len  长度
	 * @param type 类型 1:数字+字母混合 2:字母 3:数字
	 * @return
	 */
	public static String getRandomStr(int len, int type) {
		String str = "";
		Random random = new Random();
		for (int i = 0; i < len; i++) {
			if (type == 1) {
				String charOrNum = random.nextInt(2) % 2 == 0 ? "char" : "num";
				int choice = random.nextInt(2) % 2 == 0 ? 65 : 97; // 取得大写字母还是小写字母
				if (("char").equals(charOrNum)) {
					str += (char) (choice + random.nextInt(26));
				} else if (("num").equals(charOrNum)) {
					str += String.valueOf(random.nextInt(10));
				}
			} else if (type == 2) {
				int choice = random.nextInt(2) % 2 == 0 ? 65 : 97;
				str += (char) (choice + random.nextInt(26));
			} else if (type == 3) {
				str += String.valueOf(random.nextInt(10));
			} else {
				str = "";
			}
		}
		return str;
	}

	/**
	 * 根据长度和给定的字符数组生成随机字符串
	 * 
	 * @param len     字符串长度
	 * @param charStr 字符数据组
	 * @return
	 */
	public static String getRandomStr(int len, String charStr) {
		String result = "";
		if (charStr == null) {
			return result;
		}
		int max = charStr.length();
		if (max == 0) {
			return result;
		}

		Random random = new Random();
		for (int i = 0; i < len; i++) {
			int choice = random.nextInt(max) % max;
			result += charStr.charAt(choice);
		}
		return result;
	}

	/**
	 * 将字符串的指定位置替换成相同的字符
	 * 
	 * @param str
	 * @param startindex 开始替换的位置
	 * @param endindex   结束替换的位置
	 * @param newChar    替换成的字符
	 * @return
	 * @throws Exception
	 */
	public static String strReplace(String str, int startindex, int endindex, String newChar) throws Exception {
		String s1 = "";
		String s2 = "";
		try {
			s1 = str.substring(0, startindex - 1);
			s2 = str.substring(endindex, str.length());
		} catch (Exception ex) {
			throw new Exception("替换的位数大于字符串的位数");
		}
		Integer length = endindex - startindex;
		String charTmp = newChar;
		for (int i = 0; i < length; i++) {
			newChar += charTmp;
		}
		return s1 + newChar + s2;
	}

	/**
	 * 判断浏览器版本
	 * 
	 * @param agent
	 * @return
	 */
	public static String getBrowserName(String agent) {
		agent = agent.toLowerCase();
		String browser = "other";
		if (agent.indexOf("msie 6") > -1) {
			browser = "ie6";
		} else if (agent.indexOf("msie 7") > -1) {
			browser = "ie7";
		} else if (agent.indexOf("msie 8") > -1) {
			browser = "ie8";
		} else if (agent.indexOf("msie 9") > -1) {
			browser = "ie9";
		} else if (agent.indexOf("msie 10") > -1) {
			browser = "ie10";
		} else if (agent.indexOf("gecko") > -1 && agent.indexOf("rv:11") > -1) {
			browser = "ie11";
		} else if (agent.indexOf("chrome") > -1) {
			browser = "chrome";
		} else if (agent.indexOf("firefox") > -1) {
			browser = "firefox";
		}
		return browser;
	}

	/**
	 * 判断是否为空
	 * 
	 * <pre>
	 * isBlank(null)      = true
	 * isBlank("")        = true
	 * isBlank(" ")       = true
	 * isBlank("bob")     = false
	 * isBlank("  bob  ") = false
	 * </pre>
	 */
	public static boolean isBlank(final CharSequence cs) {
		int strLen;
		if (cs == null || (strLen = cs.length()) == 0) {
			return true;
		}
		for (int i = 0; i < strLen; i++) {
			if (Character.isWhitespace(cs.charAt(i)) == false) {
				return false;
			}
		}
		return true;
	}

	/**
	 * 判断是否为空
	 * 
	 * <pre>
	 * StringUtils.isEmpty(null)      = true
	 * StringUtils.isEmpty("")        = true
	 * StringUtils.isEmpty(" ")       = false
	 * StringUtils.isEmpty("bob")     = false
	 * StringUtils.isEmpty("  bob  ") = false
	 * </pre>
	 */
	public static boolean isEmpty(final CharSequence cs) {
		return cs == null || cs.length() == 0;
	}

	/**
	 * 是否正整数
	 * 
	 * @param number
	 * @return
	 */
	public static boolean isInt(String number) {
		String regex = "^[1-9][0-9]*$";
		return number.matches(regex);
	}

	/**
	 * 取得最小值和最大值之间的随机整数
	 * 
	 * @param min
	 * @param max
	 * @return
	 */
	public static int getRandomInteger(int min, int max) {
		Random rand = new Random();
		return rand.nextInt(max - min + 1) + min;
	}

	/**
	 * @param regex 正则表达式字符串
	 * @param str   要匹配的字符串
	 * @return 如果str 符合 regex的正则表达式格式,返回true, 否则返回 false;
	 */
	public static boolean match(String regex, String str) {
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(str);
		return matcher.matches();
	}

	public static BigDecimal jia(double v1, double v2) {
		BigDecimal b1 = new BigDecimal(Double.toString(v1));
		BigDecimal b2 = new BigDecimal(Double.toString(v2));
		return b1.add(b2);
	}

	public static BigDecimal jia(String v1, String v2) {
		BigDecimal b1 = new BigDecimal(v1);
		BigDecimal b2 = new BigDecimal(v2);
		return b1.add(b2);
	}

	public static BigDecimal jia(BigDecimal v1, BigDecimal v2) {
		return v1.add(v2);
	}

	public static BigDecimal jian(double v1, double v2) {
		BigDecimal b1 = new BigDecimal(Double.toString(v1));
		BigDecimal b2 = new BigDecimal(Double.toString(v2));
		return b1.subtract(b2);
	}

	public static BigDecimal jian(String v1, String v2) {
		BigDecimal b1 = new BigDecimal(v1);
		BigDecimal b2 = new BigDecimal(v2);
		return b1.subtract(b2);
	}

	public static BigDecimal jian(BigDecimal v1, BigDecimal v2) {
		return v1.subtract(v2);
	}

	public static BigDecimal cheng(double v1, double v2) {
		BigDecimal b1 = new BigDecimal(Double.toString(v1));
		BigDecimal b2 = new BigDecimal(Double.toString(v2));
		return b1.multiply(b2);
	}

	public static BigDecimal cheng(String v1, String v2) {
		BigDecimal b1 = new BigDecimal(v1);
		BigDecimal b2 = new BigDecimal(v2);
		return b1.multiply(b2);
	}

	public static BigDecimal cheng(BigDecimal v1, BigDecimal v2) {
		return v1.multiply(v2);
	}

	public static BigDecimal chu(double v1, double v2, int scale) {
		BigDecimal b1 = new BigDecimal(Double.toString(v1));
		BigDecimal b2 = new BigDecimal(Double.toString(v2));
		return b1.divide(b2, scale, BigDecimal.ROUND_HALF_UP);
	}

	public static BigDecimal chu(double v1, double v2) {
		BigDecimal b1 = new BigDecimal(Double.toString(v1));
		BigDecimal b2 = new BigDecimal(Double.toString(v2));
		return b1.divide(b2, 10, BigDecimal.ROUND_HALF_UP);
	}

	public static BigDecimal chu(String v1, String v2, int scale) {
		BigDecimal b1 = new BigDecimal(v1);
		BigDecimal b2 = new BigDecimal(v2);
		return b1.divide(b2, scale, BigDecimal.ROUND_HALF_UP);
	}

	public static BigDecimal chu(String v1, String v2) {
		BigDecimal b1 = new BigDecimal(v1);
		BigDecimal b2 = new BigDecimal(v2);
		return b1.divide(b2, 10, BigDecimal.ROUND_HALF_UP);
	}

	public static BigDecimal chu(BigDecimal v1, BigDecimal v2) {
		return v1.divide(v2, 10, BigDecimal.ROUND_HALF_UP);
	}

	public static long getDaySub(String beginDateStr, String endDateStr) {
		long day = 0;
		java.text.SimpleDateFormat format = new java.text.SimpleDateFormat("yyyy-MM-dd");
		java.util.Date beginDate;
		java.util.Date endDate;
		try {
			beginDate = format.parse(beginDateStr);
			endDate = format.parse(endDateStr);
			day = (endDate.getTime() - beginDate.getTime()) / (24 * 60 * 60 * 1000);
			// System.out.println("相隔的天数="+day);
		} catch (ParseException e) {
			// TODO 自动生成 catch 块
			e.printStackTrace();
		}
		return day;
	}

	public static String roundMath(String num) {
		BigDecimal setScale = new BigDecimal(num).setScale(2, BigDecimal.ROUND_HALF_UP);
		return setScale.toString();
	}
	
	public static String roundMath(String num, int digit) {
		BigDecimal setScale = new BigDecimal(num).setScale(digit, BigDecimal.ROUND_HALF_UP);
		return setScale.toString();
	}
	
	public static void main(String[] args) {
		try {
			long daySub = getDaySub("2019-08-20","2019-08-21");
			String num = "10.445";
			roundMath(num);
			//System.out.println(daySub);
			//System.out.println(setScale.toString());
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

}
