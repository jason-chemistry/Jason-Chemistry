package com.example.djn004test;

import java.util.ArrayList;

public class WebData {
		String title;
		String time;
		String url;
		String id;
		public WebData(String title, String time, String url, String id) {
			super();
			this.title = title;
			this.time = time;
			this.url = url;
			this.id = id;
		}
		
		public String getTitle() {
			return title;
		}
		public void setTitle(String title) {
			this.title = title;
		}
		public String getTime() {
			return time;
		}
		public void setTime(String time) {
			this.time = time;
		}
		public String getUrl() {
			return url;
		}
		public void setUrl(String url) {
			this.url = url;
		}
		public String getId() {
			return id;
		}
		public void setId(String id) {
			this.id = id;
		}
	
		
		
		
}
