package com.example.handlibrary.bean;
/**
 * created by Minglei Jia
 */
import java.util.List;

import cn.bmob.v3.BmobUser;

public class YUser extends BmobUser{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String sex;
	private String grade;
	

	public String getSex() {
		return sex;
	}
	public void setSex(String sex) {
		this.sex = sex;
	}
	public String getGrade() {
		return grade;
	}
	public void setGrade(String grade) {
		this.grade = grade;
	}

	
}
