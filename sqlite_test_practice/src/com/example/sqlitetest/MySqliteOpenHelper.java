package com.example.sqlitetest;

import android.content.ContentValues;
import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class MySqliteOpenHelper extends SQLiteOpenHelper  {
	

	
	
	private static String dbName="test.db";
	public static final String KEY_NAME="name";
	public static final String KEY_STUDENT_ID="student_id";
	public static final String KEY_IMGPATH="img_path";
	public static final String TABLE_NAME="student";
	
	public MySqliteOpenHelper(Context context) {
		
		super(context, dbName, null, 1);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		String sql=String.format("create table %s(_id INTEGER PRIMARY KEY AUTOINCREMENT,%s TEXT,%s text,%s TEXT)",TABLE_NAME,KEY_NAME,KEY_STUDENT_ID,KEY_IMGPATH);
		db.execSQL(sql);
		
		ContentValues cv=new ContentValues();
		cv.put(KEY_NAME, "ÕÅÈý");
		cv.put(KEY_STUDENT_ID, "11111");
		cv.put(KEY_IMGPATH,"/storage/emulated/0/Download/30adcbef76094b36a6a14727a1cc7cd98c109dcc.jpg");
		db.insert(TABLE_NAME, null, cv);
		
		
		
	}
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		
	}
	
			
			


}
