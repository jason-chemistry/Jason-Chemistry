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
	ImageView DigIm;//�Ի����е�im
	String path;//��Ƭ��·����
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
	//���������MainActivity������ʱ�򣬹ر��α�����ݿ�����֤���ݵİ�ȫ��
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
		//��Ӧid,�����Ժ����ݵ�ɾ������;
		_id=adapterContextMenuInfo.id;
		
		super.onCreateContextMenu(menu, v, menuInfo);
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		
		processMenuItem(item);
		
		
		return super.onContextItemSelected(item);
	}
	
	
	
	
	//�����Ĳ˵�������ѡ��Ĵ����¼�;
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
			processSend();//���Ͷ��ţ�
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
		i.putExtra("sms_body", "@Copy right �����");
		startActivity(i);
	}


	//�༭����
	void processEdit() {
		// TODO Auto-generated method stub
		View view = LayoutInflater.from(this).inflate(R.layout.add_edit, null, false);
		final EditText EtName = (EditText) view.findViewById(R.id.EditText1);
		final EditText EtPhone = (EditText) view.findViewById(R.id.EditText2);
		final Button BtFromLocal = (Button) view.findViewById(R.id.button1);
		final Button BtFromCamera = (Button) view.findViewById(R.id.button2);
		 DigIm=(ImageView) view.findViewById(R.id.imageView1);
		
		Cursor cursor2 = db.rawQuery("select * from "+MySqliteOpenHelper.TABLE_NAME+" where _id="+_id, null);
		
		//ʹ�α�ص���һ������;
		cursor2.moveToFirst();
		//ȡ���ֶε�λ��
		int name_index = cursor2.getColumnIndex(MySqliteOpenHelper.KEY_NAME);
		int age_index = cursor2.getColumnIndex(MySqliteOpenHelper.KEY_STUDENT_ID);
		int img_index= cursor2.getColumnIndex(MySqliteOpenHelper.KEY_IMGPATH);
		//��������;
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
		builder.setTitle("�༭��ϵ��").setView(view).setNegativeButton("Cancle", null).show();	
		
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
		//֪ͨ�α�ȥ����ȥ��ѯ
		cursor.requery();
	}

	//��������Ĳ˵���������ĶԻ����������ݽ��в��������
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
			builder.setTitle("�½���ϵ��").setView(view).setNegativeButton("Cancle", null).show();	
		
			
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
	 
	  //ͨ��requestCode��������Ǹ���ͼ���͸�������������зֱ���
	    @Override
	    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
	    	// TODO Auto-generated method stub
	    	   
	    		//��Ҫ����������ok����ʾ
	    		if(resultCode==RESULT_OK){
	    			if(requestCode==1){
	    				try {
	    					ContentResolver resolver=getContentResolver();//���ݽ�������������ش�����data;
	    					Uri originalUri=data.getData();//��ȡdata��uri��
	    					
	    					Bitmap bm=MediaStore.Images.Media.getBitmap(resolver, originalUri);//�����ݽ��ͽ�uri������ͼƬbmp;
	    					DigIm.setImageBitmap(bm);
	    					String[] choice={MediaStore.Images.Media.DATA};
	    					
	    					//����һ���α���������ѯ��select * from table;
	    					Cursor cursor=resolver.query(originalUri, choice, null, null, null);
	    					
	    					//ѡ��ȡʲô����
	    					int column_index=cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
	    					
	    					cursor.moveToFirst();
	    					
	    					//��ȡ��һ���ֶΣ�String path = cursor.getString(0);
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
	    		            if (!sdStatus.equals(Environment.MEDIA_MOUNTED)) { // ���sd�Ƿ����  
	    		                Log.i("TestFile",  
	    		                        "SD card is not avaiable/writeable right now.");  
	    		                return;  
	    		            }  
	    		            String name = new DateFormat().format("yyyyMMdd_hhmmss",Calendar.getInstance(Locale.CHINA)) + ".jpg";     
	    		            Toast.makeText(this, name, Toast.LENGTH_LONG).show();  
	    		            Bundle bundle = data.getExtras();  
	    		            Bitmap bitmap = (Bitmap) bundle.get("data");// ��ȡ������ص����ݣ���ת��ΪBitmapͼƬ��ʽ  
	    		            FileOutputStream b = null;  
	    		            File file = new File("/sdcard/myImage/");  
	    		            file.mkdirs();// �����ļ���  
	    		            String fileName = "/sdcard/myImage/"+name;  
	    		            path=fileName;
	    		  
	    		            try {  
	    		                b = new FileOutputStream(fileName);  
	    		                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, b);// ������д���ļ�  
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
	    		            DigIm.setImageBitmap(bitmap);// ��ͼƬ��ʾ��ImageView��  

	    			}

	    	}
	    	
	 
	    	   
	    	super.onActivityResult(requestCode, resultCode, data);
	    }
	    
	 
	 //���溯�����ַ���װ�����cv���󣬷���������ݿ⣻
	 ContentValues encodeCv(String name, String st_id,String path)
	 {
		 ContentValues cv=new ContentValues();
		 cv.put(MySqliteOpenHelper.KEY_NAME, name);
		 cv.put(MySqliteOpenHelper.KEY_STUDENT_ID,st_id);
		 cv.put(MySqliteOpenHelper.KEY_IMGPATH,path);
		 return cv;
		 
	 }
	
	 //�����ݿ��н��в������
	 void db_add(String name, String st_id,String path) {
		// TODO Auto-generated method stub
		
		db.insert(MySqliteOpenHelper.TABLE_NAME, null, encodeCv(name,st_id,path));
		//֪ͨ�α�ȥ����ȥ��ѯ
		cursor.requery();
	}
	 
	
	 //ɾ������
	void processDel() {
		// TODO Auto-generated method stub
		db.delete(MySqliteOpenHelper.TABLE_NAME,"_id="+_id,null);
		//֪ͨ�α�ȥ����ȥ��ѯ
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
