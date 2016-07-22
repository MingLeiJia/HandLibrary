package com.example.handlibrary;
/**
 * created by Minglei Jia
 */
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindListener;

import com.example.handlibrary.bean.Comments;
import com.example.handlibrary.util.Key;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.Toast;

public class CommentsActivity extends Activity {

	private ListView showcomment;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.comments);
		
		ActionBar actionbar =getActionBar();
		actionbar.setDisplayHomeAsUpEnabled(true);
		
		showcomment = (ListView) findViewById(R.id.lv_comments);
		
		Intent intent = getIntent();
		String bookid = intent.getStringExtra(Key.BOOKID);
		final BmobQuery<Comments> bmobQuery = new BmobQuery<Comments>();
		bmobQuery.addWhereEqualTo("bookID", bookid);
		bmobQuery.findObjects(CommentsActivity.this, new FindListener<Comments>() {

			@Override
			public void onSuccess(List<Comments> arg0) {
				// TODO Auto-generated method stub
				//System.out.println("###############"+arg0);
				CommentsAdapter adapter = new CommentsAdapter(CommentsActivity.this, arg0);
				
				showcomment.setAdapter(adapter);
			}

			@Override
			public void onError(int arg0, String arg1) {
				// TODO Auto-generated method stub
				Toast.makeText(CommentsActivity.this, "ΩË‘ƒ ß∞‹£°"+arg1+"("+arg0+")", Toast.LENGTH_SHORT).show();
			}
		});
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
