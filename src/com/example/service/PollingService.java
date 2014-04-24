package com.example.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import com.example.base.BaseUi;
import com.example.model.Customer;
import com.example.model.Friend;
import com.example.sqlite.FriendSqlite;
import com.example.util.AppClient;
import com.example.util.HttpUtil;
import com.example.util.JsonParser;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.util.Log;

public class PollingService extends Service {
	 
    public static final String ACTION = "com.exzample.service.PollingService";
     
//    private Notification mNotification;
//    private NotificationManager mManager;
    BaseUi baseui = new BaseUi();
 
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
 
    @Override
    public void onCreate() {
//        initNotifiManager();
    }
     
    @Override
    public void onStart(Intent intent, int startId) {
        new PollingThread().start();
    }
 
    //初始化通知栏配置
//    private void initNotifiManager() {
//        mManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
//        int icon = R.drawable.ic_launcher;
//        mNotification = new Notification();
//        mNotification.icon = icon;
//        mNotification.tickerText = "New Message";
//        mNotification.defaults |= Notification.DEFAULT_SOUND;
//        mNotification.flags = Notification.FLAG_AUTO_CANCEL;
//    }
 
    //弹出Notification
//    private void showNotification() {
//        mNotification.when = System.currentTimeMillis();
//        //Navigator to the new activity when click the notification title
//        Intent i = new Intent(this, MessageActivity.class);
//        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, i,
//                Intent.FLAG_ACTIVITY_NEW_TASK);
//        mNotification.setLatestEventInfo(this,
//                getResources().getString(R.string.app_name), "You have new message!", pendingIntent);
//        mManager.notify(0, mNotification);
//    }
    
    /**
     * 向远程后台查询用户界面更新
     * @author wangKai
     * @return 
     */
    private void indexPost(){
    	List<Map<String, Object>> friends = null;
    	String friendResult = null;
    	Friend friendO = null;
    	AppClient client = new AppClient("/Index/queryMyFriend");//客户端初始化
    	HashMap<String, String> map = new HashMap<String, String>();
	    map.put("pagenum","1");
	    FriendSqlite friendSqlite = new FriendSqlite(getApplicationContext());
	    
    	//判断网络连接状态
      	Integer netType = HttpUtil.getNetType(getApplicationContext());
    	if(netType != HttpUtil.NONET_INT){//网络连接正常app
    		Log.w("polling", "ceshi3");
    		try {
   				//网络请求
   				friendResult = client.post(map);
   				//JSON 解析
   				friends = JsonParser.parseJsonList(friendResult);
   				
   				if(friends == null){//重新登录
   					JSONObject jo;
   					client = new AppClient("/Public/register");//客户端初始化
   					//获取当前用户登录账号以及密码
   					SharedPreferences setting ;
   					setting = getSharedPreferences("MainActivity", Context.MODE_PRIVATE);
   					String username = setting.getString("username","");
   					String password = setting.getString("password","");
   					
   					map.put("username",username);
   					map.put("password",password);
	   				String logResult = client.post(map);
	   				if(!(logResult.equals("网络错误") || logResult == null)){
						//json解析
	   					JSONTokener jsonParser = new JSONTokener(logResult);  
						jo = (JSONObject) jsonParser.nextValue();
					}else{
						jo = new JSONObject().put("status","-1");
						jo.put("info","网络错误");
					}
	   				
	   				if(jo != null){
	   		    		try {
	   		    			String sid = jo.getString("sid");
	   		    			Customer customer = Customer.getInstance();
	   		    			customer.setSid(sid);//设置sessionid
	   		    		} catch (JSONException e) {
	   		    			e.printStackTrace();
	   		    		}
	   		    	}else{
	   		    		try {
	   						jo = new JSONObject().put("status","-1");
	   						jo.put("info","网络错误");
	   					} catch (JSONException e) {
	   						e.printStackTrace();
	   					}
	   		    	}
   				}else{
//   					BaseUi baseUi = new BaseUi();
   					for (Map<String, Object> friend : friends) {
   	   					friendO = new Friend();
   	   					friendO.setFaceimage((String)friend.get("faceimgurl"));
   	   					friendO.setId((String)friend.get("id"));
   	   					friendO.setUname((String)friend.get("uname"));
   	   					friendO.setUphone((String)friend.get("uphone"));
//   	   					baseUi.loadImage("http://www.hello008.com/Public/Uploads/"+(String)friend.get("faceimgurl"));
   	   					//将数据存数sqllit中
   	   					friendSqlite.updateFriend(friendO);
   					}
   				}
   				
   				//发送广播
   				Intent intent=new Intent();
   				intent.setAction("ACTION_MY");
   				sendBroadcast(intent);
   				
    		}catch (Exception e) {
    			Log.w("polling",e.toString());
    			e.printStackTrace();
    		}
    		
    	}
    }
    
 
    /**
     * Polling thread
     * 模拟向Server轮询的异步线程
     * @Author Ryan
     * @Create 2013-7-13 上午10:18:34
     */
    class PollingThread extends Thread {
        @Override
        public void run() {
        	indexPost();
        }
    }
     
    @Override
    public void onDestroy() {
        super.onDestroy();
        System.out.println("Service:onDestroy");
    }
 
}