package com.example.handlibrary.bean;
/**
 * created by Minglei Jia
 */
import cn.bmob.v3.BmobObject;

public class Person extends BmobObject{

	private  String name;
	private  String employid;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getEmployid() {
		return employid;
	}
	public void setEmployid(String employid) {
		this.employid = employid;
	}
	

	
}
