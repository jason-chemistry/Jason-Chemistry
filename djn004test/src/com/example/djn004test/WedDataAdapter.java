package com.example.djn004test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class WedDataAdapter extends ArrayAdapter<WebData> {
	
	Context context;
	ArrayList<WebData> WebDatas=new ArrayList<WebData>();
	
	public WedDataAdapter(Context context, int resource, List<WebData> objects) {
		super(context, android.R.layout.simple_list_item_1, objects);
		this.context=context;
		this.WebDatas=(ArrayList<WebData>) objects;
		
		// TODO Auto-generated constructor stub
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		View v = LayoutInflater.from(context).inflate(R.layout.item, null, false);
		TextView tv1,tv2,tv3;
		tv1=(TextView) v.findViewById(R.id.textView1);
		tv2=(TextView) v.findViewById(R.id.textView2);
		tv3=(TextView) v.findViewById(R.id.textView3);
		tv1.setText(WebDatas.get(position).getId());
		tv2.setText(WebDatas.get(position).getTime());
		tv3.setText(WebDatas.get(position).getTitle());
		return v;
		
	}
	
	

	
}
