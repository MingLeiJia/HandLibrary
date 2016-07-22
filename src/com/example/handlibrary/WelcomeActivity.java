package com.example.handlibrary;
/**
 * created by Minglei Jia
 */
import java.util.Timer;
import java.util.TimerTask;

import com.example.handlibrary.util.Key;
import com.example.handlibrary.util.SPUtils;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;

public class WelcomeActivity extends Activity {
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
				
		setContentView(R.layout.welcomepicture);
		
		
		new Timer().schedule(new TimerTask() {
			@Override
			public void run() {
				
				if(verfytoken()){
					Intent intent=new Intent(WelcomeActivity.this,HomeActivity.class);
					startActivity(intent);
					finish();
					intent=null;
				}else{
				
				startActivity(new Intent(WelcomeActivity.this, LoginActivity.class));
				finish();
				overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
				}
			}
		}, 3000);
	}
	
	public boolean verfytoken(){
		
		String token = (String) SPUtils.getParam(this, Key.TOKEN, "hello");
		
		if (!token.equals("hello")&&token !=null){
			return true;
		}else{
			return false;
		}

		
	}
}
