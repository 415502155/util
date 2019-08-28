package com.java.utils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.ParseException;
import java.util.Date;

import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.java.annotation.UserAnnotation;
import com.java.annotation.UserAnnotations;
import com.java.constant.Constant;
import com.java.entity.User;

public class ReflectUtil2 {
	
	public static void reflect() throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException, ParseException {
		User user = new User();
		Class<? extends User> clazz = user.getClass();
		Annotation[] declaredAnnotations = clazz.getDeclaredAnnotations();
		for (Annotation annotation : declaredAnnotations) {
			System.out.println(annotation.toString());
			Class<? extends Annotation> annotationType = annotation.annotationType();
			System.out.println(annotationType.toString());
		}
		/***
		 * <li>获取某个方法注解(getUserInfo方法)</li>
		 */
		Method[] declaredMethods = clazz.getDeclaredMethods();
		for (Method method : declaredMethods) {
			String methodName = method.getName();
			if (methodName.equals("getUserInfo")) {
				//UserAnnotations annotations = declaredMethods[4].getAnnotation(UserAnnotations.class);
				UserAnnotations annotations = method.getAnnotation(UserAnnotations.class);
				UserAnnotation[] list = annotations.value();
				UserAnnotation annotation = list[0];
				String name = annotation.name();
				String value = annotation.value();
				int type = annotation.type();
				System.out.println(name);
				System.out.println(value);
				System.out.println(type);
			}
		}
		/***
		 * <li>获取类注解（@UserAnnotations）</li>
		 */
		UserAnnotations annotation2 = clazz.getAnnotation(UserAnnotations.class);
		UserAnnotation[] list2 = annotation2.value();
		UserAnnotation userAnnotation = list2[0];
 		String name2 = userAnnotation.name();
  		String value2 = userAnnotation.value();
 		int type2 = userAnnotation.type();
		System.out.println(name2);
  		System.out.println(value2);
 		System.out.println(type2);
 		/***
 		 * 获取所有属性，并赋值
 		 */

 		Field[] declaredFields = clazz.getDeclaredFields();
 		for (Method method : declaredMethods) {
 			for (Field field : declaredFields) {
 				String fieldName = field.getName();
 				String upperCase = upperCase(fieldName);
 				String getMethod = "get" + upperCase;
 				String setMethod = "set" + upperCase;
 				 String type = field.getGenericType().toString();
 				 System.out.println(type);
 				 if (Constant.JAVA_STRING.equals(type)) {
 					Method set = clazz.getMethod(setMethod, String.class);
 					if ("user_name".equals(fieldName)) {
 						set.invoke(user, "administrator");
 					}
 					if ("address".equals(fieldName)) {
 						set.invoke(user, "天津市");
 					}
 					if ("mobile".equals(fieldName)) {
 						set.invoke(user, "18812345678");
 					}
 				 } else if (Constant.JAVA_INTEGER.equals(type)) {
 					Method set = clazz.getMethod(setMethod, Integer.class);
 					if ("age".equals(fieldName)) {
 						set.invoke(user, 30);
 					}
 					if ("is_del".equals(fieldName)) {
 						set.invoke(user, Constant.IS_DEL_NO);
 					}
 				 } else if (Constant.JAVA_DATE.equals(type)) {
  					Method set = clazz.getMethod(setMethod, Date.class);
  					String birthday = "2000-08-08";
  					if ("birthday".equals(fieldName)) {
 						set.invoke(user, CommonUtils.stringToDate(birthday, "yyyy-MM-dd"));
 					}
  					if ("create_time".equals(fieldName)) {
 						set.invoke(user, new Date());
 					}
 				 }
 				//method.invoke(setMethod, type);
 			}
 		}
 		String jsonString = JSONObject.toJSONString(user);
 		System.out.println(jsonString);
		/***
		 * 获取某属性的注解（@JsonFormat）
		 */
		for (Field field : declaredFields) {
		  JsonFormat annotation3 = field.getAnnotation(JsonFormat.class); 
		  String fieldName = field.getName();
		  if (fieldName.equals("birthday")) {
			  String pattern = annotation3.pattern(); 
			  String timezone = annotation3.timezone();
			  System.out.println(pattern); 
			  System.out.println(timezone);
		  }
		}
		 
	}

    public static String upperCase(String str) {  
        char[] ch = str.toCharArray();  
        if (ch[0] >= 'a' && ch[0] <= 'z') {  
            ch[0] = (char) (ch[0] - 32);  
        }  
        return new String(ch);  
    } 

	public static void main(String[] args) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException, ParseException {
		reflect();
	}
}
