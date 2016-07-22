package com.example.handlibrary;
/**
 * created by Minglei Jia
 */
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import com.example.handlibrary.util.BluetoothMsg;
import com.example.handlibrary.util.Key;
import com.example.handlibrary.util.SPUtils;

import android.app.Activity;
import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

public class HomeActivity extends Activity implements OnItemClickListener{

	private BluetoothAdapter blueadapter=null;  
	private DeviceReceiver mydevice=new DeviceReceiver();  
	private List<String> deviceList=new ArrayList<String>();  
	private ListView deviceListview;  
	private Button btserch;  
	private ArrayAdapter<String> adapter;  
	private boolean hasregister=false;  
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.home);
		
		TextView tip = (TextView) findViewById(R.id.tv_tips);
		Typeface face = Typeface.createFromAsset(getAssets(),
				"fonts/huawenxingkai.ttf");
		tip.setTypeface(face);
		
		checkUserMode();

		setView();
		setBluetooth();
	}
	@Override  
	protected void onStart() {  
		//ע���������չ㲥  
		if(!hasregister){  
			hasregister=true;  
			IntentFilter filterStart=new IntentFilter(BluetoothDevice.ACTION_FOUND);      
			IntentFilter filterEnd=new IntentFilter(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);      
			registerReceiver(mydevice, filterStart);  
			registerReceiver(mydevice, filterEnd);  
		}         
		super.onStart();  
	} 
	@Override  
	protected void onDestroy() {  
		if(blueadapter!=null&&blueadapter.isDiscovering()){  
			blueadapter.cancelDiscovery();  
		}  
		if(hasregister){  
			hasregister=false;  
			unregisterReceiver(mydevice);  
		}  
		super.onDestroy();  
	}
	private void setView(){  

		deviceListview=(ListView)findViewById(R.id.lv_devices);  
		adapter=new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, deviceList);  
		deviceListview.setAdapter(adapter);  
		deviceListview.setOnItemClickListener(this);  
		btserch=(Button)findViewById(R.id.bn_scan);  
		btserch.setOnClickListener(new ClinckMonitor());  

	}
	/** 
	 * Setting Up Bluetooth 
	 */  
	 private void setBluetooth(){  
		 blueadapter=BluetoothAdapter.getDefaultAdapter();  

		 if(blueadapter!=null){  //Device support Bluetooth  
			 //ȷ�Ͽ�������  
			 if(!blueadapter.isEnabled()){  
				 //�����û�����  
				 Intent intent=new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);  
				 startActivityForResult(intent, RESULT_FIRST_USER);  
				 //ʹ�����豸�ɼ����������  
				 Intent in=new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);  
				 in.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 200);  
				 startActivity(in);  
				 //ֱ�ӿ�������������ʾ  
				 blueadapter.enable();  
			 }  
		 }  
		 else{   //Device does not support Bluetooth  

			 AlertDialog.Builder dialog = new AlertDialog.Builder(this);  
			 dialog.setTitle("������ʾ");  
			 dialog.setMessage("�����豸û���䱸����������������ٴγ��ԡ�");  

			 dialog.setNegativeButton("�õ�",  
					 new DialogInterface.OnClickListener() {  
				 @Override  
				 public void onClick(DialogInterface dialog, int which) {  

				 }  
			 });  
			 dialog.show();  
		 }  
	 }  
	 private void checkUserMode()
	 {
		 String token = (String) SPUtils.getParam(this, Key.TOKEN, "hello");
		 if(token.equals("")||token.equals("hello"))
		 {
			 new AlertDialog.Builder(HomeActivity.this)
			 .setTitle("������ʾ")
			 .setMessage("����ǰ���ο���ݵ�¼���޷����ܸ��๦�ܣ���������¼ʹ�á�")
			 .setCancelable(false)
			 .setPositiveButton("ȥ��¼", new DialogInterface.OnClickListener() {

				 @Override
				 public void onClick(DialogInterface dialog, int which) {
					 // TODO Auto-generated method stub
					 Intent intent = new Intent(HomeActivity.this, LoginActivity.class);
					 startActivity(intent);
					 finish();
				 }
			 })
			 .setNegativeButton("�ͽ���", new DialogInterface.OnClickListener() {

				 @Override
				 public void onClick(DialogInterface dialog, int which) {
					 // TODO Auto-generated method stub

				 }
			 }).show();
		 }
	 }


	 public void onItemClick(AdapterView<?> parent, View view, int position,
			 long id) {
		 // TODO Auto-generated method stub
		 Log.e("msgParent", "Parent= "+parent);  
		 Log.e("msgView", "View= "+view);  
		 Log.e("msgChildView", "ChildView= "+parent.getChildAt(position-parent.getFirstVisiblePosition()));  

		 final String msg = deviceList.get(position);  

		 if(blueadapter!=null&&blueadapter.isDiscovering()){  
			 blueadapter.cancelDiscovery();  
			 btserch.setText("��������");  
		 }  

		 AlertDialog.Builder dialog = new AlertDialog.Builder(this);// ����һ�����������  
		 dialog.setTitle("ȷ�������豸��");  
		 dialog.setMessage(msg);    
		 dialog.setPositiveButton("����",  
				 new DialogInterface.OnClickListener() {  
			 @Override  
			 public void onClick(DialogInterface dialog, int which) {  

				 BluetoothMsg.BlueToothAddress=msg.substring(msg.length()-17);  

				 if(BluetoothMsg.lastblueToothAddress!=BluetoothMsg.BlueToothAddress){  
					 BluetoothMsg.lastblueToothAddress=BluetoothMsg.BlueToothAddress;  
				 }  

				 Intent in=new Intent(HomeActivity.this,BookActivity.class);  
				 startActivity(in);  
				 
			 }  
		 });  
		 dialog.setNegativeButton("ȡ��",  
				 new DialogInterface.OnClickListener() {  
			 @Override  
			 public void onClick(DialogInterface dialog, int which) {  
				 BluetoothMsg.BlueToothAddress = null;  
			 }  
		 });  
		 dialog.show(); 
	 }

	 /** 
	  * ��������״̬�㲥���� 
	  * 
	  */  
	 private class DeviceReceiver extends BroadcastReceiver{  

		 @Override  
		 public void onReceive(Context context, Intent intent) {  
			 String action =intent.getAction();  
			 if(BluetoothDevice.ACTION_FOUND.equals(action)){    //���������豸  
				 BluetoothDevice btd=intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);  
				 //����û������Ե������豸  
				 if (btd.getBondState() != BluetoothDevice.BOND_BONDED) {  
					 deviceList.add(btd.getName()+'\n'+btd.getAddress());  
					 adapter.notifyDataSetChanged();  
				 }  
			 }  
			 else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)){   //��������  

				 if (deviceListview.getCount() == 0) {  
					 new AlertDialog.Builder(HomeActivity.this)
					 .setTitle("������ʾ")
					 .setMessage("û�������������豸��")
					 .setCancelable(false)
					 .setPositiveButton("�õ�", new DialogInterface.OnClickListener() {

						 @Override
						 public void onClick(DialogInterface dialog, int which) {
							 // TODO Auto-generated method stub
						 }
					 }).show();  
				 }  
				 btserch.setText("��������");  
			 }  
		 }     
	 }
	 private class ClinckMonitor implements OnClickListener{  

		 @Override  
		 public void onClick(View v) {  
			 if(blueadapter.isDiscovering()){  
				 blueadapter.cancelDiscovery();  
				 btserch.setText("��������");  
			 }else{  
				 findAvalibleDevice();  
				 blueadapter.startDiscovery();  
				 btserch.setText("��������");  
			 }  
		 }  
	 }
	 /** 
	  * Finding Devices 
	  */  
	 private void findAvalibleDevice(){  
		 //��ȡ����������豸  
		 Set<BluetoothDevice> device=blueadapter.getBondedDevices();  

		 if(blueadapter!=null&&blueadapter.isDiscovering()){  
			 deviceList.clear();  
			 adapter.notifyDataSetChanged();  
		 }  
		 if(device.size()>0){ //�����Ѿ���Թ��������豸  
			 for(Iterator<BluetoothDevice> it=device.iterator();it.hasNext();){  
				 BluetoothDevice btd=it.next();  
				 deviceList.add(btd.getName()+'\n'+btd.getAddress());  
				 adapter.notifyDataSetChanged();  
			 }  
		 }else{  //�������Ѿ���Թ��������豸 

			 //deviceList.add("No can be matched to use bluetooth");  
			 //adapter.notifyDataSetChanged();
			 new AlertDialog.Builder(HomeActivity.this)
			 .setTitle("������ʾ")
			 .setMessage("�������Ѿ���Թ��������豸��")
			 .setCancelable(false)
			 .setPositiveButton("�õ�", new DialogInterface.OnClickListener() {

				 @Override
				 public void onClick(DialogInterface dialog, int which) {
					 // TODO Auto-generated method stub
				 }
			 }).show(); 
		 }  
	 }
	 @Override  
	 protected void onActivityResult(int requestCode, int resultCode, Intent data) {  

		 switch(resultCode){  
		 case RESULT_OK:  
			 findAvalibleDevice();  
			 break;  
		 case RESULT_CANCELED:  
			 break;  
		 }  
		 super.onActivityResult(requestCode, resultCode, data);  
	 }

	 @Override
	 public boolean onCreateOptionsMenu(Menu menu) {
		 // Inflate the menu; this adds items to the action bar if it is present.
		 getMenuInflater().inflate(R.menu.main, menu);
		 return true;
	 }

	 @Override
	 public boolean onOptionsItemSelected(MenuItem item) {
		 // Handle action bar item clicks here. The action bar will
		 // automatically handle clicks on the Home/Up button, so long
		 // as you specify a parent activity in AndroidManifest.xml.
		 int id = item.getItemId();
		 if (id == R.id.my) 
		 {
			 Intent intent = new Intent(HomeActivity.this, MyActivity.class);
			 startActivity(intent);
			 
			 return true;
		 }else if (id == R.id.loginout) 
		 {
			 new AlertDialog.Builder(HomeActivity.this)
			 .setTitle("������ʾ")
			 .setMessage("ȷ���˳���")
			 .setCancelable(false)
			 .setPositiveButton("ȷ��", new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					 finish();
				}
			})
			.setNegativeButton("ȡ��", null)
			.create().show();
			
			 return true;
		 }else if(id == R.id.cancelaccount)
		 {
			 new AlertDialog.Builder(HomeActivity.this)
			 .setTitle("������ʾ")
			 .setMessage("ȷ��ע����")
			 .setCancelable(false)
			 .setPositiveButton("ȷ��", new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					 SPUtils.setParam(HomeActivity.this, Key.TOKEN, "hello");
					 finish();
				}
			})
			.setNegativeButton("ȡ��", null)
			.create().show();

			 return true;
		 }else if(id == R.id.about)
		 {
			 return true;
		 }
		 return super.onOptionsItemSelected(item);
	 }

}
