package com.example.hello;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.SimpleAdapter.ViewBinder;
import android.widget.Toast;

import com.example.base.*;
import com.example.hello.MainActivity.AnsyTry;
import com.example.model.Customer;
import com.example.util.AppClient;
import com.example.util.JsonParser;

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
	    
	    class AnsyTry extends AsyncTask<String, HashMap<String, String>, List<Map<String, Object>>>{
	      	  JSONObject jo;
	      	  JSONTokener jsonParser;
	      	  SharedPreferences setting;
	      	  HashMap<String, String> map;
	      	  
	         
	          public AnsyTry(HashMap<String, String> map) {
	              super();
	              this.map = map;
	          }
	          
	          @Override
	          /**
	           * 网络耗时操作
	           */
	          protected List<Map<String, Object>> doInBackground(String... params) {
	              // TODO Auto-generated method stub
		        	List<Map<String, Object>> friends = null;
		          	AppClient client = new AppClient("/Index/queryMyFriend");//客户端初始化
		   			try {
		   				//网络请求
		   				friendResult = client.post(map);
		   				//Json解析
		   				friends = JsonParser.parseJsonList(friendResult);
		   			} catch (Exception e) {
		   				e.printStackTrace();
		   			}
		              return friends;
	          }

	          @SuppressLint("CommitPrefEdits")
	          @Override
	          /**
	           * 执行ui变更操作
	           */
	          protected void onPostExecute(List<Map<String, Object>> friends) {
	        	    //创建Simpleadapter
	   				SimpleAdapter sadapter = new SimpleAdapter( IndexActivity.this,
	   														   friends, 
	   														   R.layout.index, 
	   														   new String[]{"uname","uphone"},
	   														   new int[]{R.id.uname,R.id.uphone}) ;
	   				ListView list = (ListView) findViewById(R.id.friendlist);
	   				list.setAdapter(sadapter);
//		   		    Toast.makeText(IndexActivity.this,friendResult,Toast.LENGTH_LONG).show();
	   				
	   				//simpleadapter 处理加载网络图片问题
	   				sadapter.setViewBinder(new ViewBinder() {    
                        public boolean setViewValue(  
                                            View view,   
                                            Object data,    
                                         String textRepresentation) {    
                            //判断是否为我们要处理的对象    
                            if(view instanceof ImageView  && data instanceof Bitmap){    
                                ImageView iv = (ImageView) view ;    
                                iv.setImageBitmap((Bitmap) data) ;    
                                return true;    
                            }else    
                            return false;    
                        }    
                    });    
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
