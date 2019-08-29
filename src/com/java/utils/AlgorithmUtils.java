package com.java.utils;

import java.math.BigDecimal;

public class AlgorithmUtils {
	/**
	 * @提供精确加法计算的add方法
	 * 
	 * @param value1 被加数
	 * @param value2 加数
	 * @return 两个参数的和
	 */
	public static String add(String value1, String value2) {
		BigDecimal b1 = new BigDecimal(value1);
		BigDecimal b2 = new BigDecimal(value2);
		return b1.add(b2).toString();
	}

	/**
	 * @提供精确减法运算的sub方法
	 * 
	 * @param value1 被减数
	 * @param value2 减数
	 * @return 两个参数的差
	 */
	public static double sub(double value1, double value2) {
		BigDecimal b1 = new BigDecimal(Double.valueOf(value1));
		BigDecimal b2 = new BigDecimal(Double.valueOf(value2));
		return b1.subtract(b2).doubleValue();
	}

	/**
	 * @提供精确乘法运算的mul方法
	 * 
	 * @param value1 被乘数
	 * @param value2 乘数
	 * @return 两个参数的积
	 */
	public static double mul(double value1, double value2) {
		BigDecimal b1 = new BigDecimal(Double.valueOf(value1));
		BigDecimal b2 = new BigDecimal(Double.valueOf(value2));
		return b1.multiply(b2).doubleValue();
	}

	/**
	 * @提供精确的除法运算方法div
	 * 
	 * @param value1 被除数
	 * @param value2 除数
	 * @param scale  精确范围
	 * @return 两个参数的商
	 * @type BigDecimal舍入模式  1:BigDecimal.ROUND_HALF_UP(四舍五入)
	 * @throws IllegalAccessException
	 */
	public static String div(String value1, String value2, int scale) throws IllegalAccessException {
		if (scale < 0) {
			throw new IllegalAccessException("Accuracy cannot be less than zero !");
		}
		if ("0".equals(value2)) {
			throw new RuntimeException("The divisor cannot be zero !");
		}
		BigDecimal b1 = new BigDecimal(value1);
		BigDecimal b2 = new BigDecimal(value2);
		return String.valueOf(b1.divide(b2, scale, BigDecimal.ROUND_HALF_UP));
	}
	
	public static int getBigDecimalType(int type) {
		int res = -1;
		switch (type) {
		case 1:
			res = BigDecimal.ROUND_HALF_UP;
			break;
		default:
			break;
		}
		return res;
	}
	public static void main(String[] args) throws IllegalAccessException {
		//String format = String.format("%.50f", result);	
		/***
		 * @加
		 */
		/*
		 * String add = add("1.11", "100000000.00000000000000003");
		 * System.out.println(add);
		 */
		/***
		 * @除
		 */
		int scale = 1;
		String div = div("10.111", "2", scale);
		System.out.println("10.111/2的结果保留 " + scale + "位, result = " + div);
		long a = (long) 1234567894578123419.11;
		long b = (long) 1235565321856567828.22;
		              //9223372036854775807
		System.out.println(a);
		System.out.println(b);
		long c = a+b;
		System.out.println(" a + b =  " + c);
		String add = add(String.valueOf(a), String.valueOf(b));
		System.out.println("add = " + add);
		//BigDecimal setScale = new BigDecimal(div).setScale(5, BigDecimal.ROUND_HALF_UP);
		//System.out.println("aaaa :" + setScale.toString());
	}
	
}
