package com.example.handlibrary;
/**
 * created by Minglei Jia
 */
import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

public class SummaryActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.summary);
		ActionBar actionbar =getActionBar();
		actionbar.setDisplayHomeAsUpEnabled(true);
		
		TextView cate = (TextView) findViewById(R.id.tv_sum);
		Typeface face = Typeface.createFromAsset(getAssets(),
				"fonts/fangzhenghuali.ttf");
		cate.setTypeface(face);
		
		Intent intent = getIntent();
		String summary = intent.getStringExtra("SUMMARY");
		
		TextView show = (TextView) findViewById(R.id.tv_summaryshow);
		if(summary.equals("")){
			show.setText("对不起，暂不提供概述。");
		}else{
		show.setText(summary);
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
