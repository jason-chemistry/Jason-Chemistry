 package com.example.sqlitetest;

import java.io.File;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore.Audio.Media;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class MyCursorAdapter extends CursorAdapter {
	Context context;
	public MyCursorAdapter(Context context, Cursor c) {
		super(context, c);
		this.context=context;
		// TODO Auto-generated constructor stub
	}

	@Override
	public View newView(Context context, Cursor cursor, ViewGroup parent) {
		// TODO Auto-generated method stub
		View view = LayoutInflater.from(context).inflate(R.layout.rowview, null, false);	
		return view;
	}

	@Override
	public void bindView(View view, Context context, Cursor cursor) {
		// TODO Auto-generated method stub
		ImageView im1=(ImageView) view.findViewById(R.id.imageView1);
		TextView tv1=(TextView)view.findViewById(R.id.textView1);
		TextView tv2=(TextView)view.findViewById(R.id.textView2);		
		//获取路径;
		int name_index = cursor.getColumnIndex(MySqliteOpenHelper.KEY_NAME);
		int age_index = cursor.getColumnIndex(MySqliteOpenHelper.KEY_STUDENT_ID);
		int path_index = cursor.getColumnIndex(MySqliteOpenHelper.KEY_IMGPATH);
		//设置名字;
		String name=cursor.getString(name_index);
		String id=cursor.getString(age_index);
		String path=cursor.getString(path_index);		
		im1.setImageURI(Uri.fromFile(new File(path)));
		tv1.setText(name);
		tv2.setText(id);
		
		
		 
	}

}
