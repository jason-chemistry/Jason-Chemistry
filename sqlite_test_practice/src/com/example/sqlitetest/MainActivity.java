package com.example.sqlitetest;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.Locale;

import com.example.sqlitetest.R.id;

import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnClickListener;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;
import android.content.Intent;
public class MainActivity extends Activity {

	ListView lv;
	SQLiteDatabase db;
	Cursor cursor;
	SimpleCursorAdapter adapter;
	long _id;
	ImageView DigIm;//对话框中的im
	String path;//相片的路径；
	TextView tv;
	String number;

	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		lv=(ListView) findViewById(R.id.listView1);
		db=new MySqliteOpenHelper(this).getWritableDatabase();
		cursor=db.rawQuery("select * from "+MySqliteOpenHelper.TABLE_NAME, null);		
		MyCursorAdapter adapter = new MyCursorAdapter(this, cursor);
		lv.setAdapter(adapter);
		
		lv.setOnItemClickListener(new ListView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				Uri uri;
				Intent i;
				uri=Uri.parse("tel:"+number);
				i=new Intent(Intent.ACTION_CALL, uri);
				startActivity(i);
			}
		});
		registerForContextMenu(lv);
	
	}
	//在主程序的MainActivity结束的时候，关闭游标和数据库来保证数据的安全性
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		cursor.close();
		db.close();
		super.onDestroy();
	}
	
	
	
	
	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		// TODO Auto-generated method stub
		getMenuInflater().inflate(R.menu.main, menu);
		
		AdapterContextMenuInfo adapterContextMenuInfo=(AdapterContextMenuInfo) menuInfo;
		//对应id,用来以后数据的删除处理;
		_id=adapterContextMenuInfo.id;
		
		super.onCreateContextMenu(menu, v, menuInfo);
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		
		processMenuItem(item);
		
		
		return super.onContextItemSelected(item);
	}
	
	
	
	
	//上下文菜单的三个选项的触发事件;
	public void processMenuItem(MenuItem item) {
		// TODO Auto-generated method stub
		switch (item.getItemId()) {
		case R.id.item1://add
			processAdd();
			break;
		case R.id.item2://edit
			processEdit();
			break;
		case R.id.item3://delete
			processDel();
			break;
			
		case R.id.item4:
			processSend();//发送短信；
			break;

		default:
			
			break;
		}
	}

	
	private void processSend() {
		// TODO Auto-generated method stub
		Cursor cursor2=db.rawQuery("select * from "+MySqliteOpenHelper.TABLE_NAME+" where _id="+_id, null);
		cursor2.moveToFirst();
		int index1=cursor2.getColumnIndex(MySqliteOpenHelper.KEY_STUDENT_ID);
		number=cursor2.getString(index1);
		Uri uri;
		Intent i;
		uri=Uri.parse("smsto:"+number);
		i=new Intent(Intent.ACTION_SENDTO,uri);
		i.putExtra("sms_body", "@Copy right 董嘉楠");
		startActivity(i);
	}


	//编辑函数
	void processEdit() {
		// TODO Auto-generated method stub
		View view = LayoutInflater.from(this).inflate(R.layout.add_edit, null, false);
		final EditText EtName = (EditText) view.findViewById(R.id.EditText1);
		final EditText EtPhone = (EditText) view.findViewById(R.id.EditText2);
		final Button BtFromLocal = (Button) view.findViewById(R.id.button1);
		final Button BtFromCamera = (Button) view.findViewById(R.id.button2);
		 DigIm=(ImageView) view.findViewById(R.id.imageView1);
		
		Cursor cursor2 = db.rawQuery("select * from "+MySqliteOpenHelper.TABLE_NAME+" where _id="+_id, null);
		
		//使游标回到第一条数据;
		cursor2.moveToFirst();
		//取得字段的位置
		int name_index = cursor2.getColumnIndex(MySqliteOpenHelper.KEY_NAME);
		int age_index = cursor2.getColumnIndex(MySqliteOpenHelper.KEY_STUDENT_ID);
		int img_index= cursor2.getColumnIndex(MySqliteOpenHelper.KEY_IMGPATH);
		//设置名字;
		String name=cursor2.getString(name_index);
		String id=cursor2.getString(age_index);
		String img=cursor2.getString(img_index);
		
		Builder builder = new AlertDialog.Builder(this);
		
		EtName.setText(name);
		EtPhone.setText(id);
		DigIm.setImageURI(Uri.fromFile(new File(img)));
		
		builder.setPositiveButton("OK", new OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				String name = EtName.getText().toString();
				String st_id = EtPhone.getText().toString();
				db_add(name,st_id,path);
				
			}
		});
		builder.setTitle("编辑联系人").setView(view).setNegativeButton("Cancle", null).show();	
		
		BtFromLocal.setOnClickListener(new Button.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				Intent intent=new Intent(Intent.ACTION_PICK);
				intent.setType("image/*");
				startActivityForResult(intent,1);
			}
		});
		
		BtFromCamera.setOnClickListener(new Button.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Uri uri;
				Intent intent=new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
				startActivityForResult(intent,2);
			}
		});	
		
		
	}
	
	
	void db_edit(String name, String st_id,String path) {
		// TODO Auto-generated method stub
		
		db.update(MySqliteOpenHelper.TABLE_NAME, encodeCv(name,st_id,path), "_id="+_id, null);
		//通知游标去重新去查询
		cursor.requery();
	}

	//点击上下文菜单弹出加入的对话框，输入数据进行插入操作；
	 void processAdd() {
		// TODO Auto-generated method stub	
		 View view = LayoutInflater.from(this).inflate(R.layout.add_edit, null, false);
			final EditText EtName = (EditText) view.findViewById(R.id.EditText1);
			final EditText EtPhone = (EditText) view.findViewById(R.id.EditText2);
			final Button BtFromLocal = (Button) view.findViewById(R.id.button1);
			final Button BtFromCamera = (Button) view.findViewById(R.id.button2);
			 DigIm=(ImageView) view.findViewById(R.id.imageView1);
			
			Builder builder = new AlertDialog.Builder(this);
			
			builder.setPositiveButton("OK", new OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					String name = EtName.getText().toString();
					String st_id = EtPhone.getText().toString();
					db_add(name,st_id,path);
					
				}
			});
			builder.setTitle("新建联系人").setView(view).setNegativeButton("Cancle", null).show();	
		
			
			BtFromLocal.setOnClickListener(new Button.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					
					Intent intent=new Intent(Intent.ACTION_PICK);
					intent.setType("image/*");
					startActivityForResult(intent,1);
				}
			});
			
			BtFromCamera.setOnClickListener(new Button.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					Uri uri;
					Intent intent=new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
					startActivityForResult(intent,2);
				}
			});	
			
		
	
		
	 
	 }
	 
	  //通过requestCode来辨别是那个意图发送给其的任务，来进行分别处理；
	    @Override
	    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
	    	// TODO Auto-generated method stub
	    	   
	    		//重要，如果结果是ok就显示
	    		if(resultCode==RESULT_OK){
	    			if(requestCode==1){
	    				try {
	    					ContentResolver resolver=getContentResolver();//内容解析器负责解析回传数据data;
	    					Uri originalUri=data.getData();//获取data的uri；
	    					
	    					Bitmap bm=MediaStore.Images.Media.getBitmap(resolver, originalUri);//由内容解释将uri解析成图片bmp;
	    					DigIm.setImageBitmap(bm);
	    					String[] choice={MediaStore.Images.Media.DATA};
	    					
	    					//返回一个游标来帮助查询；select * from table;
	    					Cursor cursor=resolver.query(originalUri, choice, null, null, null);
	    					
	    					//选择取什么东西
	    					int column_index=cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
	    					
	    					cursor.moveToFirst();
	    					
	    					//获取第一个字段；String path = cursor.getString(0);
	    					path=cursor.getString(column_index);
	    					Log.e("eeeeeeee", path);
	    				
	    				} catch (IOException e) {
	    					// TODO Auto-generated catch block
	    					Log.e("Tag-->Error",e.toString());
	    				}
		    	}
	    			else if(requestCode==2){
//	    				  Bitmap photo = (Bitmap) data.getExtras().get("data");
//	    		          DigIm.setImageBitmap(photo);
	    				  String sdStatus = Environment.getExternalStorageState();  
	    		            if (!sdStatus.equals(Environment.MEDIA_MOUNTED)) { // 检测sd是否可用  
	    		                Log.i("TestFile",  
	    		                        "SD card is not avaiable/writeable right now.");  
	    		                return;  
	    		            }  
	    		            String name = new DateFormat().format("yyyyMMdd_hhmmss",Calendar.getInstance(Locale.CHINA)) + ".jpg";     
	    		            Toast.makeText(this, name, Toast.LENGTH_LONG).show();  
	    		            Bundle bundle = data.getExtras();  
	    		            Bitmap bitmap = (Bitmap) bundle.get("data");// 获取相机返回的数据，并转换为Bitmap图片格式  
	    		            FileOutputStream b = null;  
	    		            File file = new File("/sdcard/myImage/");  
	    		            file.mkdirs();// 创建文件夹  
	    		            String fileName = "/sdcard/myImage/"+name;  
	    		            path=fileName;
	    		  
	    		            try {  
	    		                b = new FileOutputStream(fileName);  
	    		                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, b);// 把数据写入文件  
	    		            } catch (FileNotFoundException e) {  
	    		                e.printStackTrace();  
	    		            } finally {  
	    		                try {  
	    		                    b.flush();  
	    		                    b.close();  
	    		                } catch (IOException e) {  
	    		                    e.printStackTrace();  
	    		                }  
	    		            }  
	    		            DigIm.setImageBitmap(bitmap);// 将图片显示在ImageView里  

	    			}

	    	}
	    	
	 
	    	   
	    	super.onActivityResult(requestCode, resultCode, data);
	    }
	    
	 
	 //下面函数将字符封装打包成cv对象，方便插入数据库；
	 ContentValues encodeCv(String name, String st_id,String path)
	 {
		 ContentValues cv=new ContentValues();
		 cv.put(MySqliteOpenHelper.KEY_NAME, name);
		 cv.put(MySqliteOpenHelper.KEY_STUDENT_ID,st_id);
		 cv.put(MySqliteOpenHelper.KEY_IMGPATH,path);
		 return cv;
		 
	 }
	
	 //在数据库中进行插入操作
	 void db_add(String name, String st_id,String path) {
		// TODO Auto-generated method stub
		
		db.insert(MySqliteOpenHelper.TABLE_NAME, null, encodeCv(name,st_id,path));
		//通知游标去重新去查询
		cursor.requery();
	}
	 
	
	 //删除函数
	void processDel() {
		// TODO Auto-generated method stub
		db.delete(MySqliteOpenHelper.TABLE_NAME,"_id="+_id,null);
		//通知游标去重新去查询
		cursor.requery();
	}
	
	
	
	
	
	


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		processMenuItem(item);
		return super.onOptionsItemSelected(item);
	}

	
	
	

}
