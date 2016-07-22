package com.example.handlibrary;
/**
 * created by Minglei Jia
 */
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;

import com.example.handlibrary.bean.Comments;
import com.example.handlibrary.util.Key;
import com.example.handlibrary.util.SPUtils;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Toast;

public class CommentActivity extends Activity {

	private RatingBar rateingbar;
	private EditText comment;
	private Button sure;
	private String studentid, bookid;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.comment);
		
		ActionBar actionbar =getActionBar();
		actionbar.setDisplayHomeAsUpEnabled(true);
		
		initView();
		Intent intent = getIntent();
		bookid = intent.getStringExtra(Key.BOOKID);
		studentid = (String) SPUtils.getParam(this, Key.studentID, "hello");

		
		sure.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				final float rating = rateingbar.getRating();
				final String commentstring = comment.getText().toString();
				if(rating == 0.0){
					Toast.makeText(CommentActivity.this, "请打分哟！谢谢！", Toast.LENGTH_SHORT).show();
					return;
				}else if(commentstring.equals("")||TextUtils.isEmpty(commentstring)){
					Toast.makeText(CommentActivity.this, "发表一些评论吧！", Toast.LENGTH_SHORT).show();
					return;
				}else{
					final BmobQuery<Comments> bmobQuery = new BmobQuery<Comments>();
					bmobQuery.addWhereEqualTo("bookID", bookid);
					bmobQuery.addWhereEqualTo("studentid", studentid);
					bmobQuery.findObjects(CommentActivity.this, new FindListener<Comments>() {

						@Override
						public void onSuccess(final List<Comments> arg0) {
							// TODO Auto-generated method stub
							
							if(arg0.size() == 0){
								final Comments usercomments = new Comments();
								usercomments.setBookID(bookid);
								usercomments.setStudentid(studentid);
								usercomments.setScore(String.valueOf(rating*2));
								usercomments.setComment(commentstring);
								usercomments.save(CommentActivity.this, new SaveListener() {
									
									@Override
									public void onSuccess() {
										// TODO Auto-generated method stub
										Toast.makeText(CommentActivity.this, "评论成功，谢谢！", Toast.LENGTH_SHORT).show();
										finish();
									}
									
									@Override
									public void onFailure(int arg0, String arg1) {
										// TODO Auto-generated method stub
										Toast.makeText(CommentActivity.this, "评论失败"+arg1+"("+arg0+")", Toast.LENGTH_SHORT).show();
									}
								});
							}else{
								
								new AlertDialog.Builder(CommentActivity.this)
								.setTitle("友情提示")
								.setMessage("你好，你已经发表过评论，如果继续操作，将覆盖上次评论。")
								.setCancelable(false)
								.setPositiveButton("继续", new DialogInterface.OnClickListener() {
									
									@Override
									public void onClick(DialogInterface dialog, int which) {
										// TODO Auto-generated method stub
										final String objectId = arg0.get(0).getObjectId();
										Comments usercomment = new Comments();
										usercomment.setObjectId(objectId);
										usercomment.setScore(String.valueOf(rating*2));
										usercomment.setComment(commentstring);
										usercomment.update(CommentActivity.this, new UpdateListener() {
											
											@Override
											public void onSuccess() {
												// TODO Auto-generated method stub
												Toast.makeText(CommentActivity.this, "评论成功，谢谢！", Toast.LENGTH_SHORT).show();
												finish();
											}
											
											@Override
											public void onFailure(int arg0, String arg1) {
												// TODO Auto-generated method stub
												Toast.makeText(CommentActivity.this, "评论失败"+arg1+"("+arg0+")", Toast.LENGTH_SHORT).show();
											}
										});
										
									}
								})
								.setNegativeButton("取消", null)
								.create().show();
							}
						}

						@Override
						public void onError(int arg0, String arg1) {
							// TODO Auto-generated method stub
							Toast.makeText(CommentActivity.this, "评论失败！"+arg1+"("+arg0+")", Toast.LENGTH_SHORT).show();
						}
					});

				}
				
			}
		});
	}
	
	private void initView(){
		rateingbar = (RatingBar) findViewById(R.id.ratingBar1);
		comment = (EditText) findViewById(R.id.et_comment);
		sure = (Button) findViewById(R.id.bn_comment_sure);
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
