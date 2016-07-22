package com.example.handlibrary;
/**
 * created by Minglei Jia
 */
import java.util.ArrayList;
import java.util.List;
import com.example.handlibrary.bean.Book;
import com.example.handlibrary.bean.BorrowInfo;
import com.example.handlibrary.util.DoubanUtil;
import com.example.handlibrary.util.Key;
import com.example.handlibrary.util.SPUtils;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BmobDate;
import cn.bmob.v3.listener.FindListener;
import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ListView;

public class MyActivity extends Activity {

	private ProgressDialog pDialog;
	private Handler hd;
	private ListView mybook;
	List<Book> booktoshow;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.my);
		
		ActionBar actionbar =getActionBar();
		actionbar.setDisplayHomeAsUpEnabled(true);
		

		mybook = (ListView) findViewById(R.id.lv_mybook);

		final String token = (String) SPUtils.getParam(MyActivity.this, Key.TOKEN, "hello");
		if(token.equals("hello")||token.equals(null)){
			new AlertDialog.Builder(MyActivity.this)
			.setTitle("友情提示")
			.setMessage("当前没有登录，请登录后继续操作！")
			.setCancelable(false)
			.setNegativeButton("取消", null)
			.setPositiveButton("去登陆", new android.content.DialogInterface.OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					Intent intent = new Intent(MyActivity.this, LoginActivity.class);
					startActivity(intent);
					finish();
				}
			}).create().show();
		}else{
			pDialog = new ProgressDialog(this);
			pDialog.setMessage("正在获取我借阅的书，请稍候...");
			pDialog.show();

			String studentid = (String) SPUtils.getParam(this, Key.studentID, "hello");

			final BmobQuery<BorrowInfo> bmobQuery = new BmobQuery<BorrowInfo>();
			bmobQuery.addWhereEqualTo("studentid", studentid);
			bmobQuery.findObjects(MyActivity.this, new FindListener<BorrowInfo>() {

				@Override
				public void onSuccess(List<BorrowInfo> arg0) {
					// TODO Auto-generated method stub
					booktoshow = new ArrayList<Book>();
					new DownloadThread(arg0).start();
					hd = new Handler(){
						public void handleMessage(Message msg) {

							pDialog.dismiss();
							BookListAdapter adapter = new BookListAdapter(MyActivity.this, booktoshow);
							mybook.setAdapter(adapter);
						}
					};
				}

				@Override
				public void onError(int arg0, String arg1) {
					// TODO Auto-generated method stub

				}
			});

		}

	}

	private class DownloadThread extends Thread
	{
		List<BorrowInfo> borrowinfo;
		public DownloadThread(List<BorrowInfo> borrowinfo)
		{
			this.borrowinfo = borrowinfo;
		}
		public void run()
		{
			for(BorrowInfo borrowinfo : borrowinfo){
				String bookisbn = borrowinfo.getBorrowbookisbn();
				String bookid = borrowinfo.getBorrowbookid();
				BmobDate booktime = borrowinfo.getBorrowtime();
				Book borrowbook = new Book();
				borrowbook.setBookid(bookid);
				borrowbook.setBookborrowtime(booktime);

				final String urlstr="https://api.douban.com/v2/book/isbn/"+bookisbn;
				String result=DoubanUtil.Download(urlstr);

				Book book=new DoubanUtil().parseBookInfo(result);

				borrowbook.setTitle(book.getTitle());
				borrowbook.setBitmap(book.getBitmap());
				borrowbook.setAuthor(book.getAuthor());
				booktoshow.add(borrowbook);
			}

			Message msg=Message.obtain();
			hd.sendMessage(msg);
			Log.i("OUTPUT","send over");
		}

	}
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    switch (item.getItemId()) {
	        case android.R.id.home:
	            finish();
	            return true;
	        default:
	            return super.onOptionsItemSelected(item);
	    }
	}
}
