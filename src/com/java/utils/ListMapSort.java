package com.java.utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSON;

public class ListMapSort {
	
	public static void main(String[] args) {
		List<Map<String, Object>> list = new ArrayList<Map<String,Object>>(16);
		for (int i = 0; i < 6; i++) {
			Map<String, Object> map = new HashMap<String, Object>();
			if (i == 0) {
				map.put("date", "2019-09-30 10:00:08");
				map.put("count", 2);
			} else if (i == 1) {
				map.put("date", "2019-09-30 10:11:01");
				map.put("count", 8);
			} else if (i == 2) {
				map.put("date", "2019-09-30 10:11:01");
				map.put("count", 7);
			} else if (i == 3) {
				map.put("date", "2019-09-30 10:11:00");
				map.put("count", 1);
			} else {
				map.put("date", "2019-09-30 10:00:02");
				map.put("count", 2);
			}
			list.add(map);
		}
		/***
		 * 按照count升序，date降序, 1为升序-1为降序
		 */
		resultOrder(list, "count", 1, "date", -1);
		System.out.println(JSON.toJSONString(list));
	}
	/**
	 * 支持两个字段排序
	 * @param result
	 * @param order
	 * @param orderType
	 * @param twoOrder 第二排序字段
	 * @param twoType 第二排序顺序
	 * @return	
	 */
	public static List<Map<String, Object>> resultOrder(List<Map<String, Object>> result, String order, Integer orderType, 
	                                                    final String twoOrder, final Integer twoType){
	    if(result == null || orderType == null){
	        return result;
	    }
	    if(orderType != -1){
	        orderType = 1;
	    }
	    final String orderKey = order;
	    final Integer oType = orderType;
	    Collections.sort(result, new Comparator<Map<String, Object>>() {
	        @Override
	        public int compare(Map<String, Object> o1, Map<String, Object> o2) {
	            //这里加了个判空，原则上，空元素（map）就不该进来
	            if(o1 == null && o2 == null){
	                return 0;
	            }
	            if (o1 == null) {
	                if(oType < 0){
	                    return -oType;
	                }
	                return oType;
	            }
	            if (o2 == null) {
	                if(oType < 0){
	                    return oType;
	                }
	                return -oType;
	            }
	            return commonOrder(orderKey, oType, twoOrder, twoType, o1, o2);
	        }
	    });
	    return result;
	}
	/**
	 * 对结果集进行排序，目前支持日期、字符串、各种整形、各种浮点型
	 * @param result 结果集
	 * @param order
	 * @param orderType -1降序 1升序, 下面代码假设orderType为1
	 * @return
	 */
	public static List<Map<String, Object>> resultOrder(List<Map<String, Object>> result, String order, Integer orderType){
	    return resultOrder(result, order, orderType, null, null);
	}
	/**
	 * 公共的排序部分
	 * @param orderKey
	 * @param oType
	 * @param obj1
	 * @param obj2
	 * @param twoOrder
	 * @param twoType
	 * @return
	 */
	public static Integer commonOrder(final String orderKey, final Integer oType, String twoOrder, Integer twoType, 
	        Map<String, Object> o1, Map<String, Object> o2) {
	    Object obj1 = o1.get(orderKey);
	    Object obj2 = o2.get(orderKey);
	    if(obj1 == null && obj2 == null){
	        return 0;
	    }
	    if (obj1 == null) {
	        if(oType < 0){
	            return -oType;
	        }
	        return oType;
	    }
	    if (obj2 == null) {
	        if(oType < 0){
	            return oType;
	        }
	        return -oType;
	    }
	    if(obj1 instanceof Date && obj2 instanceof Date){
	        //日期排序
	        Date date1 = (Date)obj1;
	        Date date2 = (Date)obj2;
	        return longCompare(oType, date1.getTime(), date2.getTime(), twoOrder, twoType,o1, o2);
	    }else if(obj1 instanceof String && obj2 instanceof String){
	        //字符串排序
	        String str1 = obj1.toString();
	        String str2 = obj2.toString();

	        if(str1.compareTo(str2) < 0){
	            return -oType;
	        }else if(str1.compareTo(str2) == 0){
	            return 0;
	        }else if(str1.compareTo(str2) > 0){
	            return oType;
	        }
	    }else if((obj1 instanceof Double || obj1 instanceof Float) && (obj2 instanceof Double || obj2 instanceof Float)){
	        //浮点型排序
	        return doubleCompare(oType, obj1, obj2, twoOrder, twoType,o1, o2);
	    }else if((obj1 instanceof Long || obj1 instanceof Integer || obj1 instanceof Short || obj1 instanceof Byte) && 
	             (obj2 instanceof Long || obj2 instanceof Integer || obj2 instanceof Short || obj2 instanceof Byte)){
	        //整数型排序
	        return longCompare(oType, obj1, obj2, twoOrder, twoType, o1, o2);
	    }else if((obj1.getClass() != obj2.getClass()) && (obj1 instanceof Number && obj2 instanceof Number)){
	        //这种情况可能是，既有整数又有浮点数
	        return doubleCompare(oType, obj1, obj2, twoOrder, twoType,o1, o2);
	    }
	    return 0;
	}

	/**
	 * 整形比较大小
	 * @param oType
	 * @param obj1
	 * @param obj2
	 * @param twoOrder
	 * @param twoType
	 * @return
	 */
	private static int longCompare(final Integer oType, Object obj1, Object obj2, String twoOrder, Integer twoType, 
	                               Map<String, Object> o1, Map<String, Object> o2) {
	    long d1 = Long.parseLong(obj1.toString());
	    long d2 = Long.parseLong(obj2.toString());
	    if(d1 < d2){
	        return -oType;
	    }else if(d1 == d2){

	        if(twoOrder != null && twoType != null){
	            //相等就使用第二字段排序
	            return commonOrder(twoOrder, twoType, null, null, o1, o2);
	        }
	        //相同的是否进行交互
	        return 0;
	    }else if(d1 > d2){
	        return oType;
	    }
	    return 0;
	}
	/**
	 * 浮点型比较大小
	 * @param oType
	 * @param obj1
	 * @param obj2
	 * @return
	 */
	private static int doubleCompare(final Integer oType, Object obj1, Object obj2, String twoOrder, Integer twoType, 
	                                 Map<String, Object> o1, Map<String, Object> o2) {
	    double d1 = Double.parseDouble(obj1.toString());
	    double d2 = Double.parseDouble(obj2.toString());
	    if(d1 < d2){
	        return -oType;
	    }else if(d1 == d2){
	        if(twoOrder != null && twoType != null){
	            //相等就使用第二字段排序
	            return commonOrder(twoOrder, twoType, null, null, o1, o2);
	        }
	        return 0;
	    }else if(d1 > d2){
	        return oType;
	    }
	    return 0;
	}
}
