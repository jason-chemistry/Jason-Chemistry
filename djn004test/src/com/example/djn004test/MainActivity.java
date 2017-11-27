package com.example.djn004test;

import java.util.ArrayList;
import java.util.HashMap;


import android.support.v7.app.ActionBarActivity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;


public class MainActivity extends ActionBarActivity{
	ListView lv;
	Button bt;
	Handler handler;
	WedDataAdapter adp;
	ArrayList<WebData> webdatas;
	private View moreview;
	private ProgressBar pg1;
	private ProgressBar pg2;
	private Button bt1;
	private int Page=0;
	private int LastVisibleItem=0;
	private int maxnum=3;
	public ArrayList<String> urls;
	public ArrayList<String> titles;
	public ArrayList<String> times;
	public ArrayList<String> ids;
	ArrayList<WebData> newslist=new ArrayList<WebData>();
    
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        InintData();
        Events();
        adp=new WedDataAdapter(MainActivity.this, 0, newslist);	
		lv.setAdapter(adp);
        handler=new Handler(new Handler.Callback() {
			
			@Override
			public boolean handleMessage(Message arg0) {
				// TODO Auto-generated method stub
				updataUI(arg0);
				
				return true;
			}
		});
      
   
	
	
	}

	protected void updataUI(Message arg0) {
		// TODO Auto-generated method stub
		pg1.setVisibility(View.GONE);
		bt1.setVisibility(View.VISIBLE);
		
		webdatas=new ArrayList<WebData>();
		webdatas=(ArrayList<WebData>) arg0.getData().getSerializable("WEBKEY");
		
		for(WebData item:webdatas)
		 {
			newslist.add(item);
		 }
		adp.notifyDataSetChanged();
       
	}
	private void InintData() {
		// TODO Auto-generated method stub
		lv=(ListView) findViewById(R.id.listView1);
		bt=(Button) findViewById(R.id.button1);
		moreview = getLayoutInflater().inflate(R.layout.footview, null, false);
		pg1=(ProgressBar) moreview.findViewById(R.id.pg);
		pg2=(ProgressBar)findViewById(R.id.progressBar1);
        bt1=(Button) moreview.findViewById(R.id.more_button);
		lv.addFooterView(moreview);
	}
    
	private void Events() {
		// TODO Auto-generated method stub
    	
    	bt.setOnClickListener(new Button.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				bt.setVisibility(View.GONE);
				//pg2.setVisibility(View.VISIBLE);
				new MyTread(handler).start();
				
			}
		});
    	
    	
    	lv.setOnItemClickListener(new ListView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				
				String url = adp.getItem(arg2).getUrl();
				Uri uri=Uri.parse(url);
				Intent intent=new Intent(Intent.ACTION_VIEW,uri);
				startActivity(intent);
			}
    		
		});
    	
    	lv.setOnScrollListener(new OnScrollListener() {
			
			@Override
			public void onScrollStateChanged(AbsListView arg0, int arg1) {
				// TODO Auto-generated method stub
				
			if(arg1==OnScrollListener.SCROLL_STATE_IDLE&&LastVisibleItem==adp.getCount()){
					Page++;
					if(Page>=maxnum)
					{
						pg1.setVisibility(View.GONE);
						bt1.setVisibility(View.VISIBLE);
						bt1.setText("没有更多数据");
					}
					else
					{
						new MyTread(handler, Page).start();
						pg1.setVisibility(View.VISIBLE);
						bt1.setVisibility(View.GONE);
					}
					
				
				}
			}
			
			@Override
			public void onScroll(AbsListView arg0, int firstVisibleItem, int VisibleItemCount, int totalItemCount) {
				// TODO Auto-generated method stub
				LastVisibleItem=firstVisibleItem+VisibleItemCount-1;
			}
		});
  	
    	
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
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


	
}



