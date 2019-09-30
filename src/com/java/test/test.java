package com.java.test;

import java.lang.reflect.InvocationTargetException;
import java.text.ParseException;
import java.util.Date;

import com.alibaba.fastjson.JSONObject;
import com.java.entity.User;
import com.java.utils.ReflectUtil;

public class test {
	
	public static void main(String[] args) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException, ParseException {
		JSONObject json = new JSONObject();
		String userName = "张三";
		String address = "天津市";
		String mobile = "18812345678";
		Integer age = 30;
		Integer isDel = 0;
		String birthday = "2000-08-08";
		Date date = new Date();
		json.put("user_name", userName);
		json.put("address", address);
		json.put("mobile", mobile);
		json.put("age", age);
		json.put("is_del", isDel);
		json.put("birthday", birthday);
		json.put("create_time", date);
		System.out.println(json.toJSONString());
		User user =  new User();
		Class<? extends User> class1 = user.getClass();
		ReflectUtil.reflect(class1, json.toJSONString());
		
		int[] myArray = {1, 2, 3, 4, 5};
        doIt(myArray);
        for (int i = 0; i < myArray.length; i++)
        {
            System.out.println(myArray[i] + " ");
        }
        
        int[] arr = new int[9];
        System.out.println("arr[8] ：" + arr[8]);
	}
	
	public static void doIt( int[] z )
    {
        int[] A = z;
        A[0] = 99;
    }
}
