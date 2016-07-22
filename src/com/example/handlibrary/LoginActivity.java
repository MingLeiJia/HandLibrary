package com.example.handlibrary;
/**
 * created by Minglei Jia
 */
import com.example.handlibrary.util.Key;
import com.example.handlibrary.util.SPUtils;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.listener.SaveListener;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


public class LoginActivity extends Activity {

	private EditText studentid, password;
	private Button login;
	private TextView noaccount, passforgot, tourist;
	private String account, pw;
	private ProgressDialog pDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        Bmob.initialize(this, "649025eeb32089da5bf65304ce8cf12a");
        
        initView();
        noaccount.setOnClickListener(new MyOnClickListener());
        login.setOnClickListener(new MyOnClickListener());
        tourist.setOnClickListener(new MyOnClickListener());
    }
    private void initView() {
    	studentid = (EditText) findViewById(R.id.et_loginid);
		password = (EditText) findViewById(R.id.et_loginpass);
		login = (Button) findViewById(R.id.bn_login);
		noaccount = (TextView) findViewById(R.id.tv_noaccount);
		passforgot = (TextView) findViewById(R.id.tv_passforgot);
		tourist = (TextView) findViewById(R.id.tv_tourist);
	}
    private boolean inputcheck() {
    	account = studentid.getText().toString();
		pw = password.getText().toString();
		if(account.length()!=11)
		{
			Toast.makeText(LoginActivity.this, "学号为11位！", Toast.LENGTH_SHORT).show();
			return false;
		}else if(account.equals(""))
		{
			Toast.makeText(LoginActivity.this, "学号不能为空！", Toast.LENGTH_SHORT).show();
			return false;
		}else if(pw.length() < 6)
		{
			Toast.makeText(LoginActivity.this, "密码长度不能少于六位！", Toast.LENGTH_SHORT).show();
			return false;
		}else {
			return true;
		}
	}
    private class MyOnClickListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			switch (v.getId()) {
			case R.id.tv_tourist:
				Intent intent1 = new Intent(LoginActivity.this, HomeActivity.class);
				startActivity(intent1);
				finish();
				break;
			case R.id.tv_noaccount:
				Intent intent2 = new Intent(LoginActivity.this, RegisterActivity.class);
				startActivity(intent2);
				break;
			case R.id.bn_login:
				boolean checkresult = inputcheck();
				if(checkresult == false)
				{
					return;
				}else {
					pDialog = new ProgressDialog(LoginActivity.this);
					pDialog.setMessage("正在登录，请稍候......");
					pDialog.show();
					final BmobUser bu2 = new BmobUser();
					bu2.setUsername(account);
					bu2.setPassword(pw);
					bu2.login(LoginActivity.this, new SaveListener() {
						
						@Override
						public void onSuccess() {
							// TODO Auto-generated method stub
							pDialog.dismiss();
							String token = bu2.getObjectId();
							SPUtils.setParam(LoginActivity.this, Key.TOKEN, token);
							SPUtils.setParam(LoginActivity.this, Key.studentID, account);
							Toast.makeText(LoginActivity.this, "恭喜，登录成功！"
									, Toast.LENGTH_SHORT).show();
							Intent intent1 = new Intent(LoginActivity.this, HomeActivity.class);
							startActivity(intent1);
							finish();
						}
						
						@Override
						public void onFailure(int arg0, String arg1) {
							// TODO Auto-generated method stub
							pDialog.dismiss();
							Toast.makeText(LoginActivity.this, "对不起，"+arg1
									, Toast.LENGTH_SHORT).show();
						}
					});
				}
				break;
			default:
				break;
			}
		}
		
	}
}
