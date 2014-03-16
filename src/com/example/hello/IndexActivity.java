package com.example.hello;

import java.util.HashMap;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.widget.Toast;

import com.example.base.*;
import com.example.hello.MainActivity.AnsyTry;
import com.example.model.Customer;
import com.example.util.AppClient;

public class IndexActivity extends Activity{
	private String friendResult;//返回好友信息
	
	 @Override
	    protected void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        setContentView(R.layout.index);
	        
	        HashMap<String, String> map = new HashMap<String, String>();
	        map.put("pagenum","1");
	        AnsyTry anys=new AnsyTry(map);
			anys.execute();
	    }


	    @Override
	    public boolean onCreateOptionsMenu(Menu menu) {
	        // Inflate the menu; this adds items to the action bar if it is present.
	        getMenuInflater().inflate(R.menu.main, menu);
	        return true;
	    }
	    
	    class AnsyTry extends AsyncTask<String, HashMap<String, String>, String>{
	      	 JSONObject jo;
	      	 JSONTokener jsonParser;
	      	 SharedPreferences setting;
	      	 HashMap<String, String> map;
	         
	          public AnsyTry(HashMap<String, String> map) {
	              super();
	              this.map = map;
	          }
	          
	          @Override
	          protected String doInBackground(String... params) {
	              // TODO Auto-generated method stub
	          	AppClient client = new AppClient("/Index/queryMyFriend");//客户端初始化
	   			try {
	   				friendResult = client.post(map);
	   				
	   				//json解析
	   				jsonParser = new JSONTokener(friendResult);  
	   				jo = (JSONObject) jsonParser.nextValue();
//	   				BaseMessage str = AppUtil.getMessage(logResult);
//	   				str.getResult();
	   				
	   			} catch (Exception e) {
	   				e.printStackTrace();
	   			}
	              return "1";
	          }

	          @SuppressLint("CommitPrefEdits")
	          @Override
	          protected void onPostExecute(String str) {
		       	String info = null;
		       	String status = null;
		       	String sid = null;
		       	
		   		
		   		Toast.makeText(IndexActivity.this,friendResult,Toast.LENGTH_LONG).show();
		      }
	
		          @Override
		          protected void onPreExecute() {
		              // TODO Auto-generated method stub\
		              System.out.println("pretExecute------");
		              super.onPreExecute();
		          }
	
		          protected void onProgressUpdate(Integer... values) {
		              // TODO Auto-generated method stub
		          }

	       } 
	       

}
