package com.example.handlibrary;
/**
 * created by Minglei Jia
 */
import android.app.Activity;
import android.app.AlertDialog;
import android.os.Bundle;

import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.UUID;

import cn.bmob.v3.datatype.BmobDate;
import cn.bmob.v3.listener.SaveListener;

import com.example.handlibrary.bean.Book;
import com.example.handlibrary.bean.BorrowInfo;
import com.example.handlibrary.util.BluetoothMsg;
import com.example.handlibrary.util.DoubanUtil;
import com.example.handlibrary.util.Key;
import com.example.handlibrary.util.SPUtils;

import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
public class BookActivity extends Activity {

	private Context mContext; 
	private clientThread clientConnectThread = null;  
	private BluetoothSocket socket = null;  
	private BluetoothDevice device = null;  
	private readThread mreadThread = null;;   
	private BluetoothAdapter mBluetoothAdapter = null;
	private String bookISBN, bookid;

	private TextView publisher,pages, binding, author, title, pubdate;
	private TextView catalog, summary, comments;
	private Button borrow;
	private ImageView bookcover;

	private Handler hd;
	private ProgressDialog mpd,pdstart;;

	private Book bookresult;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.bookdetail);

		mContext = this;
		initView();

		hd=	new Handler(){
			@Override
			public void handleMessage(Message msg) {
				// TODO Auto-generated method stub
				super.handleMessage(msg);
				bookresult= (Book)msg.obj;
				//��������ʧ
				if(mpd != null){
					
					mpd.dismiss();
					mpd = null;
				}

				if(bookresult.getPublisher().contains("������"))
				{
					publisher.setText(bookresult.getPublisher());
				}else{
					publisher.setText(bookresult.getPublisher()+"������");
				}

				pages.setText(bookresult.getPages()+"ҳ");
				if(bookresult.getBing().equals(""))
				{
					binding.setText("ƽװ��");
				}else{
					binding.setText(bookresult.getBing());}

				author.setText(bookresult.getAuthor());
				title.setText(bookresult.getTitle());
				pubdate.setText(bookresult.getPublishDate());

				bookcover.setImageBitmap(bookresult.getBitmap());

			}
		};
	}

	private void initView()
	{
		bookcover = (ImageView) findViewById(R.id.iv_bookcover);
		publisher = (TextView) findViewById(R.id.bookdetail_publisher);
		pages = (TextView) findViewById(R.id.bookdetail_pages);
		binding = (TextView) findViewById(R.id.bookdetail_binding);
		author = (TextView) findViewById(R.id.bookdetail_author);
		title = (TextView) findViewById(R.id.bookdetail_title);
		pubdate = (TextView) findViewById(R.id.bookdetail_pubdate);

		catalog = (TextView) findViewById(R.id.bookdetail_catalog);
		summary = (TextView) findViewById(R.id.bookdetail_summary);
		comments = (TextView) findViewById(R.id.bookdetail_comments);

		catalog.setOnClickListener(new MyOnClickListener());
		summary.setOnClickListener(new MyOnClickListener());
		comments.setOnClickListener(new MyOnClickListener());

		mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter(); 

		borrow = (Button) findViewById(R.id.bn_borrow);
		borrow.setOnClickListener(new MyOnClickListener());


		pdstart=new ProgressDialog(BookActivity.this);
		pdstart.setMessage("���Ժ����ڻ�ȡ�û�������Ϣ...");
		pdstart.show();
		

	}

	private class MyOnClickListener implements OnClickListener{

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			switch (v.getId()) {
			case R.id.bookdetail_catalog:
				Intent intent1 = new Intent(BookActivity.this, CatelogActivity.class);
				if(bookresult == null){
					return;
				}else{
					if(!bookresult.equals(null)){
						intent1.putExtra("CATEGORY", bookresult.getCatalog());}
					startActivity(intent1);
				}

				break;
			case R.id.bookdetail_summary:
				Intent intent2 = new Intent(BookActivity.this, SummaryActivity.class);
				if(bookresult == null){
					return;
				}else{

					if(!bookresult.equals(null)){
						intent2.putExtra("SUMMARY", bookresult.getSummary());}
					startActivity(intent2);				
				}
				break;
			case R.id.bookdetail_comments:
				Intent intent3 = new Intent(BookActivity.this, CommentsActivity.class);
				intent3.putExtra(Key.BOOKID, bookid);
				startActivity(intent3);			
				break;
			case R.id.bn_borrow:

				final String token = (String) SPUtils.getParam(BookActivity.this, Key.TOKEN, "hello");
				if(token.equals("hello")||token.equals(null)){
					new AlertDialog.Builder(BookActivity.this)
					.setTitle("������ʾ")
					.setMessage("��ǰû�е�¼�����¼�����������")
					.setCancelable(false)
					.setNegativeButton("ȡ��", null)
					.setPositiveButton("ȥ��½", new android.content.DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							// TODO Auto-generated method stub
							Intent intent = new Intent(BookActivity.this, LoginActivity.class);
							startActivity(intent);
							finish();
						}
					}).create().show();
				}else{
					final String studentid = (String) SPUtils.getParam(BookActivity.this, Key.studentID, "hello");
					new AlertDialog.Builder(BookActivity.this)
					.setTitle("������ʾ")
					.setMessage("ȷ��������?")
					.setCancelable(false)
					.setNegativeButton("ȡ��", null)
					.setPositiveButton("ȷ��", new android.content.DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							// TODO Auto-generated method stub

							final BorrowInfo borrowinfo = new BorrowInfo();
							String bookid = (String) SPUtils.getParam(BookActivity.this, Key.BOOKID, "hello");
							String bookisbn = (String) SPUtils.getParam(BookActivity.this, Key.BOOKISBN, "hello");
							borrowinfo.setBorrowbookid(bookid);
							borrowinfo.setBorrowbookisbn(bookisbn);
							borrowinfo.setBorrowtime(new BmobDate(new Date()));
							borrowinfo.setStudentid(studentid);

							borrowinfo.save(BookActivity.this, new SaveListener(){

								@Override
								public void onFailure(int arg0, String arg1) {
									// TODO Auto-generated method stub
									Toast.makeText(BookActivity.this, "����ʧ�ܣ�"+arg1+"("+arg0+")", Toast.LENGTH_SHORT).show();
								}

								@Override
								public void onSuccess() {
									// TODO Auto-generated method stub
									Toast.makeText(BookActivity.this, "���ĳɹ���", Toast.LENGTH_SHORT).show();
								}

							});

						}
					}).create().show();
				}
				break;
			default:
				break;
			}
		}

	}

	@Override  
	protected void onResume() {  


		super.onResume();  
		BluetoothMsg.serviceOrCilent=BluetoothMsg.ServerOrCilent.CILENT;  

		if(BluetoothMsg.isOpen)  
		{  
			//Toast.makeText(mContext, "�����Ѿ��򿪣�����ͨ�š����Ҫ�ٽ������ӣ����ȶϿ���", Toast.LENGTH_SHORT).show();  
			return;  
		}  

		String address = BluetoothMsg.BlueToothAddress;  
		if(!address.equals("null"))  
		{  
			device = mBluetoothAdapter.getRemoteDevice(address);      
			clientConnectThread = new clientThread();  
			clientConnectThread.start();  
			BluetoothMsg.isOpen = true;  
		}  
		else  
		{  
			Toast.makeText(mContext, "address is null !", Toast.LENGTH_SHORT).show();  
		}  


	}  
	private Handler LinkDetectedHandler = new Handler() {  
		@Override  
		public void handleMessage(Message msg) {  
			//Toast.makeText(mContext, (String)msg.obj, Toast.LENGTH_SHORT).show();  
			if(msg.what==1)  
			{  
				pdstart.dismiss();

				if(mpd!=null){
					mpd=new ProgressDialog(BookActivity.this);
					mpd.setMessage("���Ժ����ڶ�ȡ�鼮��Ϣ...");
					mpd.show();
				}

				//bookISBN = (String)msg.obj;  
				String result = (String)msg.obj;  
				bookISBN = result.split(",")[0];
				bookid = result.split(",")[1];
				//Toast.makeText(mContext, "��ǰ��ISBN��"+bookISBN, Toast.LENGTH_LONG).show();  
				String urlstr="https://api.douban.com/v2/book/isbn/"+bookISBN;
				//Toast.makeText(mContext, "��ǰ��ID��"+bookid, Toast.LENGTH_LONG).show(); 
				SPUtils.setParam(BookActivity.this, Key.BOOKID, bookid);
				SPUtils.setParam(BookActivity.this, Key.BOOKISBN, bookISBN);
				new DownloadThread(urlstr).start();
			}else{
				if(pdstart!=null)
				{
					pdstart.dismiss();
				}
				Toast.makeText(mContext, "���紫������", Toast.LENGTH_SHORT).show();
				if(mpd!=null){					
					mpd.dismiss();
				}
				return;
			}
		}  
	}; 
	private class DownloadThread extends Thread
	{
		String url=null;
		public DownloadThread(String urlstr)
		{
			url=urlstr;
		}
		public void run()
		{
			String result=DoubanUtil.Download(url);
			if(result.contains("book_not_found")){
				new AlertDialog.Builder(BookActivity.this)
				.setTitle("������ʾ")
				.setMessage("�Բ���û�ҵ��Ȿ�顣")
				.setCancelable(false)
				.setPositiveButton("֪����", null).show();
				return;
			}else{
				Log.i("OUTPUT", "download over");
				Book book=new DoubanUtil().parseBookInfo(result);
				Log.i("OUTPUT", "parse over");
				Log.i("OUTPUT",book.getSummary()+book.getAuthor());
				//�����߳�UI���淢��Ϣ������������Ϣ��������Ϣ���

				Message msg=Message.obtain();
				msg.obj=book;
				hd.sendMessage(msg);
				Log.i("OUTPUT","send over");
			}


		}
	}
	//�����ͻ���  
	private class clientThread extends Thread {           
		@Override  
		public void run() {  
			try {  
				//����һ��Socket���ӣ�ֻ��Ҫ��������ע��ʱ��UUID��  
				// socket = device.createRfcommSocketToServiceRecord(BluetoothProtocols.OBEX_OBJECT_PUSH_PROTOCOL_UUID);  
				//Method m = device.getClass().getMethod("createRfcommSocket", new Class[] { int.class });
				//socket = (BluetoothSocket) m.invoke(device, 1);
				socket = device.createRfcommSocketToServiceRecord(UUID.fromString("00001101-0000-1000-8000-00805f9b34fb"));
				//socket = device.createInsecureRfcommSocketToServiceRecord(UUID.fromString("2076b461-6001-4659-2241-225576cA6bf9"));
				/*
				//����  
				Message msg2 = new Message();  
				msg2.obj = "���Ժ��������ӷ�����:"+BluetoothMsg.BlueToothAddress;  
				msg2.what = 0;  
				LinkDetectedHandler.sendMessage(msg2);  
				 */

				// TODO Auto-generated method stub
				try {
					socket.connect();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					try {
						socket.close();
					} catch (IOException closeException) {
						closeException.printStackTrace();
					}	   
				} 

				/*
				Message msg = new Message();  
				msg.obj = "�Ѿ������Ϸ���ˣ����Է�����Ϣ��";  
				msg.what = 0;  
				LinkDetectedHandler.sendMessage(msg);  
				 */
				//������������  
				mreadThread = new readThread();  
				mreadThread.start();  
			}   
			catch (IOException e)   
			{  
				Log.e("connect", "", e);  
				Message msg = new Message();  
				msg.obj = "���ӷ�����쳣���Ͽ�����������һ�ԡ�";  
				msg.what = 0;  
				LinkDetectedHandler.sendMessage(msg);  
			}    
		}  
	};
	//��ȡ����  
	private class readThread extends Thread {   
		@Override  
		public void run() {  

			byte[] buffer = new byte[1024];  
			int bytes;  
			InputStream mmInStream = null;  

			try {  
				mmInStream = socket.getInputStream();  
			} catch (IOException e1) {  
				// TODO Auto-generated catch block  
				e1.printStackTrace();  
			}     
			while (true) {  
				try {  
					// Read from the InputStream  
					if( (bytes = mmInStream.read(buffer)) > 0 )  
					{  
						byte[] buf_data = new byte[bytes];  
						for(int i=0; i<bytes; i++)  
						{  
							buf_data[i] = buffer[i];  
						}  
						String s = new String(buf_data);  
						Message msg = new Message();  
						msg.obj = s;  
						msg.what = 1;  
						LinkDetectedHandler.sendMessage(msg);  
					}  
				} catch (IOException e) {  
					try {  
						mmInStream.close();  
					} catch (IOException e1) {  
						// TODO Auto-generated catch block  
						e1.printStackTrace();  
					}  
					break;  
				}  
			}  
		}  
	}  
	/* ֹͣ�ͻ������� */  
	private void shutdownClient() {  
		new Thread() {  
			@Override  
			public void run() {  
				if(clientConnectThread!=null)  
				{  
					clientConnectThread.interrupt();  
					clientConnectThread= null;  
				}  
				if(mreadThread != null)  
				{  
					mreadThread.interrupt();  
					mreadThread = null;  
				}  
				if (socket != null) {  
					try {  
						socket.close();  
					} catch (IOException e) {  
						// TODO Auto-generated catch block  
						e.printStackTrace();  
					}  
					socket = null;  
				}  
			};  
		}.start();  
	} 
	@Override  
	protected void onDestroy() {  
		super.onDestroy();  

		shutdownClient();  

		BluetoothMsg.isOpen = false;  
		BluetoothMsg.serviceOrCilent = BluetoothMsg.ServerOrCilent.NONE;  
	}  
}
