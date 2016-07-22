package com.example.handlibrary.bean;
/**
 * created by Minglei Jia
 */
import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobDate;

public class BorrowInfo extends BmobObject{
	
	private String studentid;
	private BmobDate borrowtime;
	private String borrowbookid;
	private String borrowbookisbn;
	
	public String getStudentid() {
		return studentid;
	}
	public void setStudentid(String studentid) {
		this.studentid = studentid;
	}
	public BmobDate getBorrowtime() {
		return borrowtime;
	}
	public void setBorrowtime(BmobDate borrowtime) {
		this.borrowtime = borrowtime;
	}
	public String getBorrowbookid() {
		return borrowbookid;
	}
	public void setBorrowbookid(String borrowbookid) {
		this.borrowbookid = borrowbookid;
	}
	public String getBorrowbookisbn() {
		return borrowbookisbn;
	}
	public void setBorrowbookisbn(String borrowbookisbn) {
		this.borrowbookisbn = borrowbookisbn;
	}

	
	
	

}
