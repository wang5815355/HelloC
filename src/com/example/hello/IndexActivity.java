package com.example.hello;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;
import org.json.JSONTokener;

import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Message;
import android.view.Menu;
import android.widget.ListView;
import android.widget.Toast;

import com.example.base.BaseHandler;
import com.example.base.BaseTask;
import com.example.base.BaseUi;
import com.example.list.FriendList;
import com.example.model.Friend;
import com.example.service.PollingService;
import com.example.sqlite.FriendSqlite;
import com.example.util.AppClient;
import com.example.util.HttpUtil;
import com.example.util.JsonParser;
import com.example.util.PollingUtils;

public class IndexActivity extends BaseUi{
	private String friendResult;//返回好友信息
//	private SimpleAdapter sadapter;
	private FriendList friendList;
	private FriendSqlite friendSqlite;
	
	 @Override
	 protected void onCreate(Bundle savedInstanceState) {
	      super.onCreate(savedInstanceState);
	      setContentView(R.layout.index);
	        
	      HashMap<String, String> map = new HashMap<String, String>();
	      map.put("pagenum","1");
	      AnsyTry anys=new AnsyTry(map);
	      anys.execute();
			
		  //启动轮询service
	      PollingUtils.startPollingService(this, 5, PollingService.class, PollingService.ACTION);
	  }
	 
	    @Override
	    public boolean onCreateOptionsMenu(Menu menu) {
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
		        	Friend friendO = null;
		          	AppClient client = new AppClient("/Index/queryMyFriend");//客户端初始化
		          	friendSqlite = new FriendSqlite(IndexActivity.this);
		          	
		          	//判断网络连接状态
		          	Integer netType = HttpUtil.getNetType(IndexActivity.this);
					if(netType != HttpUtil.NONET_INT){//网络连接正常
						try {
				   				//网络请求
				   				friendResult = client.post(map);
				   				//JSON 解析
				   				friends = JsonParser.parseJsonList(friendResult);
				   				if(friends == null){
				   					friends = friendSqlite.getAllFriends();
				   				}
				   				for (Map<String, Object> friend : friends) {
				   					friendO = new Friend();
				   					friendO.setFaceimage((String)friend.get("faceimgurl"));
				   					friendO.setId((String)friend.get("id"));
				   					friendO.setUname((String)friend.get("uname"));
				   					friendO.setUphone((String)friend.get("uphone"));
				   					//将数据存数sqllit中
				   					friendSqlite.updateFriend(friendO);
									loadImage("http://www.hello008.com/Public/Uploads/"+(String)friend.get("faceimgurl"));
								}
			   				
			   			} catch (Exception e) {
			   				e.printStackTrace();
			   			}
						
					}else{
						 //网络连接断开时从sqllite中取出信息
						 friends = friendSqlite.getAllFriends();
						 for (Map<String, Object> friend : friends) {
								loadImage("http://www.hello008.com/Public/Uploads/"+(String)friend.get("faceimgurl"));
						 }
					}
		          	
		   			
		              return friends;
	          }

	          @Override
	          /**
	           * 执行ui变更操作
	           */
	          protected void onPostExecute(List<Map<String, Object>> friends) {
	        	    //自定义adapter
	        	  	friendList = new FriendList(IndexActivity.this, friends);
	   				ListView list = (ListView) findViewById(R.id.friendlist);
	   				list.setAdapter(friendList);
//		   		    Toast.makeText(IndexActivity.this,friendResult,Toast.LENGTH_LONG).show();
	   				IndexActivity.this.setHandler(new IndexHandler(IndexActivity.this, friendList));
	   				
		      }
	
		          @Override
		          protected void onPreExecute() {
		              System.out.println("pretExecute------");
		              super.onPreExecute();
		          }
	
		          protected void onProgressUpdate(Integer... values) {
		          
		          }

	       } 
	  
	  /**
	   * 图片异步下砸成功后更新ui 
	   */
	  private class IndexHandler extends BaseHandler {
//		  	SimpleAdapter sadapter;
		  	private FriendList friendList;
		  	
			public IndexHandler(BaseUi ui,FriendList friendList) {
				super(ui);
				this.friendList = friendList;
			}
			@Override
			public void handleMessage(Message msg) {
				super.handleMessage(msg);
				try {
					switch (msg.what) {
						case BaseTask.LOAD_IMAGE:
							friendList.notifyDataSetChanged();
						break;
					}
				} catch (Exception e) {
					Toast.makeText(IndexActivity.this,e.toString(),Toast.LENGTH_LONG).show();
				}
			}
		}

}
