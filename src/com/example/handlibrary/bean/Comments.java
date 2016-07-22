package com.example.handlibrary.bean;
/**
 * created by Minglei Jia
 */
import cn.bmob.v3.BmobObject;

public class Comments extends BmobObject{

	


	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String bookID;
	private String studentid;
	private String comment;
	private String score;
	
	public String getBookID() {
		return bookID;
	}
	public void setBookID(String bookID) {
		this.bookID = bookID;
	}

	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}
	public String getScore() {
		return score;
	}
	public void setScore(String score) {
		this.score = score;
	}
	public String getStudentid() {
		return studentid;
	}
	public void setStudentid(String studentid) {
		this.studentid = studentid;
	}
	
	
	
}
