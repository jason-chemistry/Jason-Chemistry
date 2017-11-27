package com.example.djn004test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Handler;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import android.os.Bundle;
import android.os.Message;

public class MyTread extends Thread {

	android.os.Handler handler;
	ArrayList<WebData> webdatas=new ArrayList<WebData>();
	Bundle bundle;
	Message message;
	ArrayList<String> titles;
	ArrayList<String> ids;
	ArrayList<String> times;
	ArrayList<String> urls;
	int page;
	
	
	public static final String WEBKEY="WEBKEY";
	public static final String IDKEY="IDKEY";
	public static final String TIMEKEY="TIMEKEY";
	public static final String URLKEY="URLKEY";
	public static final String TITLEKEY="TITLEKEY";
	public static final String MAXNUM="MAXNUM";

	
	public MyTread(android.os.Handler handler) {
		this.handler=handler;
	}
	
	public MyTread(android.os.Handler handler,int page) {
		this.handler=handler;
		this.page=page;
	}



	public void run() {
		// TODO Auto-generated method stub
		String url="http://jwc.wzu.edu.cn/Col/Col70/Index.aspx";
		if(page>=2){
			url=String.format("http://jwc.wzu.edu.cn/Col/Col70/Index_%d.aspx", page);
		}
		else{
			url="http://jwc.wzu.edu.cn/Col/Col70/Index.aspx";
		}
		try {
			
			Document doc = Jsoup.connect(url).timeout(1000).get();
			Elements divs=doc.getElementsByAttributeValue("class", "newslist");
			Elements lis = divs.select("li");
			Elements as = lis.select("a");
			Elements spans = lis.select("span");
			String url0="http://jwc.wzu.edu.cn/";
			for(int i=0;i<as.size();i=i+1){
				String url1=url0+as.get(i).attr("href");
				String title=as.get(i).attr("title");
				String time=spans.get(i).text();
				String id=String.format("%02d %02d",page,i);
				titles=new ArrayList<String>();
				ids=new ArrayList<String>();
				times=new ArrayList<String>();
				urls=new ArrayList<String>();
				titles.add(title);
				ids.add(id);
				times.add(time);
				urls.add(url1);
				WebData webdata = new WebData(title, time, url1, id);
				webdatas.add(webdata);			
			}			
			bundle=new Bundle();			
			bundle.putSerializable(WEBKEY,webdatas);
			bundle.putSerializable(IDKEY,ids);
			bundle.putSerializable(TIMEKEY,times);
			bundle.putSerializable(TITLEKEY,titles);
			bundle.putSerializable(URLKEY,urls);			
			message=new Message();			
			message.setData(bundle);
			
		
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		handler.sendMessage(message);
		
		
	}

	
}
