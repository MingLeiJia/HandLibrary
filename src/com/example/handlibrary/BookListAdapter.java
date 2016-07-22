package com.example.handlibrary;
/**
 * created by Minglei Jia
 */
import java.io.BufferedInputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.net.URLConnection;
import java.util.List;








import cn.bmob.v3.datatype.BmobDate;

import com.example.handlibrary.bean.Book;
import com.example.handlibrary.util.Key;
import com.example.handlibrary.util.SPUtils;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class BookListAdapter extends BaseAdapter {
	
	private List<Book> booklist = null;
	private Context context = null;

	public BookListAdapter(Context context, List<Book> booklist) {
		this.context = context;
		this.booklist = booklist;
		
	}
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return booklist.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
			return booklist.get(position);
	
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		
		ViewHolder holder = null;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = LayoutInflater.from(context).inflate(R.layout.aty_booklist_cell, null);
			holder.img = (ImageView) convertView.findViewById(R.id.iv_booklist);
			holder.author = (TextView) convertView.findViewById(R.id.tv_booklist_cell_author);
			holder.bookname = (TextView) convertView.findViewById(R.id.tv_booklist_cell_bookname);
			holder.comment = (Button) convertView.findViewById(R.id.bn_comment);
			holder.borrowtime = (TextView) convertView.findViewById(R.id.tv_booklist_cell_time);
			convertView.setTag(holder);
		}else{
			holder = (ViewHolder) convertView.getTag();
		}

			String bookName = booklist.get(position).getTitle();
			String bookAuthor = booklist.get(position).getAuthor();
			BmobDate booktime = booklist.get(position).getBookborrowtime();
			Bitmap bookcover = booklist.get(position).getBitmap(); 
			final String bookid = booklist.get(position).getBookid();

			//System.out.println("$$$$$$$$$$"+bookAuthor.toString());
			holder.bookname.setText(bookName);
			holder.author.setText(bookAuthor);
			holder.img.setImageBitmap(bookcover);
			holder.borrowtime.setText(booktime.getDate().toString());
			holder.comment.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					Intent intent = new Intent(context, CommentActivity.class);
					intent.putExtra(Key.BOOKID, bookid);
					context.startActivity(intent);
				}
			});

		//  Í¼Æ¬µÄ´¦Àí
		return convertView;
	}
	public final class ViewHolder{
		public ImageView img;
		public TextView bookname;
		public TextView author;
		public TextView borrowtime;
		public Button comment;
	}

}
