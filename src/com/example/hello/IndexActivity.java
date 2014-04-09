package com.example.hello;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;
import org.json.JSONTokener;

import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Message;
import android.view.Menu;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.SimpleAdapter.ViewBinder;
import android.widget.Toast;

import com.example.base.BaseHandler;
import com.example.base.BaseTask;
import com.example.base.BaseUi;
import com.example.util.AppClient;
import com.example.util.JsonParser;

public class IndexActivity extends BaseUi{
	private String friendResult;//返回好友信息
	private SimpleAdapter sadapter;
	
	 @Override
	    protected void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        setContentView(R.layout.index);
	        // set handler
			this.setHandler(new IndexHandler(this));
	        
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
		        	List<Map<String, Object>> friends = null;
		          	AppClient client = new AppClient("/Index/queryMyFriend");//客户端初始化
		   			try {
		   				//网络请求
		   				friendResult = client.post(map);
		   				//JSON 解析
		   				friends = JsonParser.parseJsonList(friendResult);
		   				loadImage("");
		   				for (Map<String, Object> friend : friends) {
							loadImage("http://www.hello008.com/Public/Uploads/"+(String)friend.get("faceimgurl"));
						}
		   			} catch (Exception e) {
		   				e.printStackTrace();
		   			}
		              return friends;
	          }

	          @Override
	          /**
	           * 执行ui变更操作
	           */
	          protected void onPostExecute(List<Map<String, Object>> friends) {
	        	    //创建 Simpleadapter
	   				sadapter = new SimpleAdapter( IndexActivity.this,
	   														   friends, 
	   														   R.layout.index, 
	   														   new String[]{"faceimg","uname","uphone"},
	   														   new int[]{R.id.faceimg,R.id.uname,R.id.uphone});
	   				ListView list = (ListView) findViewById(R.id.friendlist);
	   				list.setAdapter(sadapter);
//		   		    Toast.makeText(IndexActivity.this,friendResult,Toast.LENGTH_LONG).show();
	   				
	   				//Simpleadapter 处理加载网络图片问题
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
		              System.out.println("pretExecute------");
		              super.onPreExecute();
		          }
	
		          protected void onProgressUpdate(Integer... values) {
		          
		          }

	       } 
	    
	  private class IndexHandler extends BaseHandler {
			public IndexHandler(BaseUi ui) {
				super(ui);
			}
			@Override
			public void handleMessage(Message msg) {
				super.handleMessage(msg);
				try {
					switch (msg.what) {
						case BaseTask.LOAD_IMAGE:
							sadapter.notifyDataSetChanged();
							break;
					}
				} catch (Exception e) {
					e.printStackTrace();
					Toast.makeText(IndexActivity.this,e.getMessage(),Toast.LENGTH_LONG).show();
				}
			}
		}

}
