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

import lombok.extern.slf4j.Slf4j;
@Slf4j
public class ReflectUtil {
	
	@SuppressWarnings("unused")
	public static void reflect(Class<?> clazz, String json) throws IllegalAccessException, IllegalArgumentException, 
		InvocationTargetException, NoSuchMethodException, SecurityException, ParseException {
		User user = new User();
		//Class<? extends User> clazz = user.getClass();
		Annotation[] declaredAnnotations = clazz.getDeclaredAnnotations();
		for (Annotation annotation : declaredAnnotations) {
			//System.out.println(annotation.toString());
			Class<? extends Annotation> annotationType = annotation.annotationType();
			//System.out.println(annotationType.toString());
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
				log.info("getUserInfo annotation ", "name = " + name, "value = " + value, "type = " + type);
			}
		}
		/***
		 * <li>获取类注解（@UserAnnotations）</li>
		 */
		UserAnnotations annotation2 = (UserAnnotations) clazz.getAnnotation(UserAnnotations.class);
		UserAnnotation[] list2 = annotation2.value();
		UserAnnotation userAnnotation = list2[0];
 		String name2 = userAnnotation.name();
  		String value2 = userAnnotation.value();
 		int type2 = userAnnotation.type();
		log.info("UserAnnotations注解值,name：[{}], value：[{}], type：[{}]", name2 , value2, type2);
 		/***
 		 * 获取所有属性，并赋值
 		 */	

 		Field[] declaredFields = clazz.getDeclaredFields();
		for (Method method : declaredMethods) {
			for (Field field : declaredFields) {
				String fieldName = field.getName();
				String upperCase = upperCase(fieldName);
				String getMethod = "get" + upperCase;
				log.info("实体类User的GET方法,get*:[{}]", getMethod);
				String setMethod = "set" + upperCase;
				String type = field.getGenericType().toString();
				// System.out.println(type);
				JSONObject obj = JSONObject.parseObject(json);
				if (Constant.JAVA_STRING.equals(type)) {
					Method set = clazz.getMethod(setMethod, String.class);
					if ("user_name".equals(fieldName)) {
						boolean containsKey = obj.containsKey("user_name");
						if (containsKey) {
							String userName = (String) obj.get("user_name");
							set.invoke(user, userName);
						}
					}
					if ("address".equals(fieldName)) {
						boolean containsKey = obj.containsKey("address");
						if (containsKey) {
							String address = (String) obj.get("address");
							set.invoke(user, address);
						}
					}
					if ("mobile".equals(fieldName)) {
						boolean containsKey = obj.containsKey("mobile");
						if (containsKey) {
							String mobile = (String) obj.get("mobile");
							set.invoke(user, mobile);
						}
					}
				} else if (Constant.JAVA_INTEGER.equals(type)) {
					Method set = clazz.getMethod(setMethod, Integer.class);
					if ("age".equals(fieldName)) {
						boolean containsKey = obj.containsKey("age");
						if (containsKey) {
							Integer age = (Integer) obj.get("age");
							set.invoke(user, age);
						}
					}
					if ("is_del".equals(fieldName)) {
						boolean containsKey = obj.containsKey("is_del");
						if (containsKey) {
							Integer is_del = (Integer) obj.get("is_del");
							set.invoke(user, is_del);
						}
					}
				} else if (Constant.JAVA_DATE.equals(type)) {
					Method set = clazz.getMethod(setMethod, Date.class);
					if ("birthday".equals(fieldName)) {
						boolean containsKey = obj.containsKey("birthday");
						if (containsKey) {
							Date date = obj.getDate("birthday");
							set.invoke(user, date/* CommonUtils.stringToDate(birthday, "yyyy-MM-dd") */);
						}
					}
					if ("create_time".equals(fieldName)) {
						boolean containsKey = obj.containsKey("create_time");
						if (containsKey) {
							Date date = obj.getDate("create_time");
							set.invoke(user, date);
						}
					}
				}
				// method.invoke(setMethod, type);
			}
		}
 		//String jsonString = JSONObject.toJSONString(user);
 		//System.out.println("" + jsonString);
		/***
		 * 获取某属性的注解（@JsonFormat）
		 */
		for (Field field : declaredFields) {
		  JsonFormat annotation3 = field.getAnnotation(JsonFormat.class); 
		  String fieldName = field.getName();
		  if (fieldName.equals("birthday")) {
			  String pattern = annotation3.pattern(); 
			  String timezone = annotation3.timezone();
			  System.out.println("日期格式化 ：" + pattern); 
			  System.out.println("时区 ：" + timezone);
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
		//reflect();
	}
}
