package com.hello008.service;


import org.json.JSONArray;
import org.json.JSONObject;

import com.hello008.base.BaseUi;
import com.hello008.hello.R;
import com.hello008.util.AppClient;
import com.hello008.util.AppUtil;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.net.Uri;
import android.os.IBinder;
import android.util.Log;

public class UpdatePollingService extends Service {
	 
    public static final String ACTION_UPDATE = "com.hello008.service.UpdatePollingService";
    NotificationManager mManager;
    Notification mNotification;
    int newVerCode;
	String newVerName;
     
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
    private void initNotifiManager() {
        mManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        int icon = R.drawable.ic_launcher;
        mNotification = new Notification();
        mNotification.icon = icon;
        mNotification.tickerText = "Hello有新的版本";
        mNotification.defaults = Notification.DEFAULT_LIGHTS;
        mNotification.flags = Notification.FLAG_SHOW_LIGHTS;
    }
 
    //弹出Notification
    @SuppressWarnings("deprecation")
	private void showNotification() {
        mNotification.when = System.currentTimeMillis();
        //Navigator to the new activity when click the notification title
        Uri uri = Uri.parse("http://www.hello008.com/Public/App/Hello.apk");  
		Intent i = new Intent(Intent.ACTION_VIEW, uri);  
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, i,
                Intent.FLAG_ACTIVITY_NEW_TASK);
        mNotification.setLatestEventInfo(this,
                getResources().getString(R.string.app_name), "检测到有最新版本 ,点击立刻升级", pendingIntent);
        mManager.notify(0, mNotification);
    }
    
    /**
  	 * 版本更新判断
  	 * @author wangkai
  	 */
  	private boolean getServerVer () {
  		try {
  			Log.w("polling", "getServer");
        	  AppClient client = new AppClient("/Public/App/ver.json");//客户端初始化
        	  //网络请求
              String verjson = client.get();
              JSONArray array = new JSONArray(verjson);
              if (array.length() > 0) {
                  JSONObject obj = array.getJSONObject(0);
				try {
                      newVerCode = Integer.parseInt(obj.getString("verCode"));
                      newVerName = obj.getString("verName");
                      Log.w("polling", "getServer5");
                  } catch (Exception e) {
                      newVerCode = -1;
                      newVerName = "";
                      Log.w("polling", "getServer4");
                      return false;
                  }
              }
          } catch (Exception e) {
        	  Log.w("polling","==="+e);
              return false;
          }
  		Log.w("polling", "getServer2");
          return true;
      }
    
    
    /**
     * 检测应用是否有最新版本
     * @author wangKai
     * @return 
     */
    private void indexPost(){
    	//判断版本更新
		if (getServerVer()) {
			int vercode = AppUtil.getVerCode(getApplicationContext()); 
			if (newVerCode > vercode) {
				initNotifiManager();
		    	showNotification();
			} else {
				Log.w("polling", "不更新");
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
        	Log.w("polling", "应用");
        	indexPost();
        }
    }
     
    @Override
    public void onDestroy() {
        super.onDestroy();
        System.out.println("Service:onDestroy");
    }
 
}