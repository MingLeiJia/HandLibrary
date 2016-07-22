package com.example.handlibrary;
/**
 * created by Minglei Jia
 */
import java.util.List;

import com.example.handlibrary.bean.Person;
import com.example.handlibrary.bean.YUser;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.wifi.WifiConfiguration.Protocol;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class RegisterActivity extends Activity {

	private EditText studentid, pass, passrepeat;
	private CheckBox agree;
	private Button register;
	private String userid , pw;
	private ProgressDialog pDialog;
	private TextView protocol;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.register);
		initView();
		register.setOnClickListener(new MyOnClickListener());
		protocol.setOnClickListener(new MyOnClickListener());
	}
	private void initView() {
		studentid = (EditText) findViewById(R.id.et_register_id);
		pass = (EditText) findViewById(R.id.et_register_pass);
		passrepeat = (EditText) findViewById(R.id.et_register_passrepeat);
		agree = (CheckBox) findViewById(R.id.cb_protocol);
		register = (Button) findViewById(R.id.bn_register);
		agree = (CheckBox) findViewById(R.id.cb_protocol);
		protocol = (TextView) findViewById(R.id.tv_protocol);
		
	}
	private boolean inputcheck() {
		userid = studentid.getText().toString();
		pw = pass.getText().toString();
		String pwrepeat = passrepeat.getText().toString();
		if(!agree.isChecked())
		{
			Toast.makeText(RegisterActivity.this, "���Ķ�ע��Э�鲢ͬ�⣡", Toast.LENGTH_SHORT).show();
			return false;
		}else if(userid.length()!=11)
		{
			Toast.makeText(RegisterActivity.this, "ѧ��Ϊ11λ��", Toast.LENGTH_SHORT).show();
			return false;
		}else if(userid.equals(""))
		{
			Toast.makeText(RegisterActivity.this, "ѧ�Ų���Ϊ�գ�", Toast.LENGTH_SHORT).show();
			return false;
		}else if(pw.equals("")||pwrepeat.equals(""))
		{
			Toast.makeText(RegisterActivity.this, "���벻��Ϊ�գ�", Toast.LENGTH_SHORT).show();
			return false;
		}else if(pass.length() < 6)
		{
			Toast.makeText(RegisterActivity.this, "���볤�Ȳ���������λ��", Toast.LENGTH_SHORT).show();
			return false;
		}else if(!pw.equals(pwrepeat))
		{
			Toast.makeText(RegisterActivity.this, "�����������벻һ�£�", Toast.LENGTH_SHORT).show();
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
			case R.id.bn_register:
				boolean checkresult = inputcheck();
				if(checkresult == false)
				{
					return;
				}else {
					pDialog = new ProgressDialog(RegisterActivity.this);
					pDialog.setMessage("����ע�ᣬ���Ժ�......");
					pDialog.show();
					BmobQuery<Person> query = new BmobQuery<Person>();
					query.addWhereEqualTo("employid", userid);
					query.findObjects(RegisterActivity.this, new FindListener<Person>() {

						@Override
						public void onError(int arg0, String arg1) {
							// TODO Auto-generated method stub
							pDialog.dismiss();
							Toast.makeText(RegisterActivity.this, "�Բ���"+arg1
									, Toast.LENGTH_SHORT).show();
						}

						public void onSuccess(final List<Person> arg0) {
							// TODO Auto-generated method stub
							pDialog.dismiss();
							if(arg0.size()==0)
							{
								Toast.makeText(RegisterActivity.this, "�Բ����㲻�Ǳ�Уѧ�����޷�ע�ᣡ"
										, Toast.LENGTH_SHORT).show();
							}else {
								
								final YUser yUser = new YUser();
								yUser.setUsername(userid);
								yUser.setPassword(pw);
								yUser.signUp(RegisterActivity.this, new SaveListener() {
									
									@Override
									public void onSuccess() {
										// TODO Auto-generated method stub
										Toast.makeText(RegisterActivity.this, "��ϲ�㣬"+arg0.get(0).getName()+"��ע��ɹ���"
												, Toast.LENGTH_SHORT).show();
										finish();
									}
									
									@Override
									public void onFailure(int arg0, String arg1) {
										// TODO Auto-generated method stub
										Toast.makeText(RegisterActivity.this, "�Բ���"+arg1
												, Toast.LENGTH_SHORT).show();
									}
								});
							}

							
						}
					});
				}

				break;
			case R.id.tv_protocol:
				Intent intent = new Intent(RegisterActivity.this, ProtocolActivity.class);
				startActivity(intent);
				break;
			default:
				break;
			}
		}
	}
}
