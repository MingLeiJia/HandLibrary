package com.example.handlibrary.bean;
/**
 * created by Minglei Jia
 */
import java.util.List;

import cn.bmob.v3.datatype.BmobDate;
import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;

public class Book implements Parcelable{
	

	private BmobDate bookborrowtime;
	private String bookid;
	private String mTitle="";
	private Bitmap mBitmap;
	private String mAuthor="";
	private String mPublisher="";
	private String mPublishDate="";
	private String mISBN="";
	private String mSummary="";
	private String mPrice="";
	private List<String> comments;
	private String catalog;
	private String bing;
	private String pages;


	public BmobDate getBookborrowtime() {
		return bookborrowtime;
	}
	public void setBookborrowtime(BmobDate bookborrowtime) {
		this.bookborrowtime = bookborrowtime;
	}
	public String getBookid() {
		return bookid;
	}
	public void setBookid(String bookid) {
		this.bookid = bookid;
	}
	
	public String getTitle() {
		return mTitle;
	}
	public void setTitle(String mTitle) {
		this.mTitle = mTitle;
	}
	public Bitmap getBitmap() {
		return mBitmap;
	}
	public void setBitmap(Bitmap mBitmap) {
		this.mBitmap = mBitmap;
	}
	public String getAuthor() {
		return mAuthor;
	}
	public void setAuthor(String mAuthor) {
		this.mAuthor = mAuthor;
	}
	public String getPublisher() {
		return mPublisher;
	}
	public void setPublisher(String mPublisher) {
		this.mPublisher = mPublisher;
	}
	public String getPublishDate() {
		return mPublishDate;
	}
	public void setPublishDate(String mPublishDate) {
		this.mPublishDate = mPublishDate;
	}
	public String getISBN() {
		return mISBN;
	}
	public void setISBN(String mISBN) {
		this.mISBN = mISBN;
	}
	public String getSummary() {
		return mSummary;
	}
	public void setSummary(String mSummary) {
		this.mSummary = mSummary;
	}
	public String getPrice() {
		return mPrice;
	}
	public void setPrice(String mPrice) {
		this.mPrice = mPrice;
	}
	public List<String> getComments() {
		return comments;
	}
	public void setComments(List<String> comments) {
		this.comments = comments;
	}
	public String getCatalog() {
		return catalog;
	}
	public void setCatalog(String catalog) {
		this.catalog = catalog;
	}
	public String getBing() {
		return bing;
	}
	public void setBing(String bing) {
		this.bing = bing;
	}
	public String getPages() {
		return pages;
	}
	public void setPages(String pages) {
		this.pages = pages;
	}

	

	
	public static final Parcelable.Creator<Book> CREATOR = new Creator<Book>() {
		public Book createFromParcel(Parcel source) {
			Book bookInfo = new Book();
			bookInfo.mTitle = source.readString();
			bookInfo.mBitmap = source.readParcelable(Bitmap.class.getClassLoader());
			bookInfo.mAuthor = source.readString();
			bookInfo.mPublisher = source.readString();
			bookInfo.mPublishDate = source.readString();
			bookInfo.mISBN = source.readString();
			bookInfo.mSummary = source.readString();
			bookInfo.mPrice = source.readString();
			bookInfo.catalog = source.readString();
			bookInfo.bing = source.readString();
			bookInfo.pages = source.readString();
			return bookInfo;
		}
		public Book[] newArray(int size) {
			return new Book[size];
		}
	};
	public int describeContents() {
		return 0;
	}
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(mTitle);
		dest.writeParcelable(mBitmap, flags);
		dest.writeString(mAuthor);
		dest.writeString(mPublisher);
		dest.writeString(mPublishDate);
		dest.writeString(mISBN);
		dest.writeString(mSummary);
		dest.writeString(mPrice);
		dest.writeString(catalog);
		dest.writeString(bing);
		dest.writeString(pages);
	}
}
