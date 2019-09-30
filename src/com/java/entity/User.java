package com.java.entity;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.java.annotation.UserAnnotation;
import com.java.annotation.UserAnnotations;

import lombok.Data;

@Data
@UserAnnotations(value = {@UserAnnotation(name = "11", value = "22", type = 1)})
public class User implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private Integer userId;
	
	private String user_name;

	private String address;

	private Integer age;

	private String mobile;
	
	@JsonFormat(pattern="yyyy-MM-dd",timezone = "GMT+8")
	private Date birthday;

	private Integer is_del;
	
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
	private Date create_time;
	
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
	private Date update_time;
	
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
	private Date del_time;
	
	@UserAnnotations(value = {@UserAnnotation(name = "张三", value = "administrator", type = 100)})
	public String getUserInfo(String user_name, String address) {
		return "";
	}

}
