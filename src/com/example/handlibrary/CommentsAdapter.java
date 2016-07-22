package com.example.handlibrary;
/**
 * created by Minglei Jia
 */
import java.util.List;

import com.example.handlibrary.bean.Comments;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class CommentsAdapter extends BaseAdapter {
	
	private List<Comments> commentslist = null;
	private Context context = null;

	public CommentsAdapter(Context context, List<Comments> commentslist) {
		this.context = context;
		this.commentslist = commentslist;
		
	}
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return commentslist.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
			return commentslist.get(position);
	
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
			convertView = LayoutInflater.from(context).inflate(R.layout.aty_comments_cell, null);
			holder.username = (TextView) convertView.findViewById(R.id.tv_comments_cell_user);
			holder.score = (TextView) convertView.findViewById(R.id.tv_comments_cell_score);
			holder.comment = (TextView) convertView.findViewById(R.id.tv_comments_cell_comment);
			convertView.setTag(holder);
		}else{
			holder = (ViewHolder) convertView.getTag();
		}

			String usernameString = commentslist.get(position).getStudentid();
			String scoreString = commentslist.get(position).getScore(); 
			String commentString = commentslist.get(position).getComment();

			holder.username.setText(usernameString);
			holder.score.setText(scoreString);
			holder.comment.setText(commentString);

		//  Í¼Æ¬µÄ´¦Àí
		return convertView;
	}
	public final class ViewHolder{
		
		public TextView username;
		public TextView score;
		public TextView comment;

	}

}
