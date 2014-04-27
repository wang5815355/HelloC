package com.hello008.base;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.ExecutorService;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;

import com.hello008.base.BaseApp;
import com.hello008.base.BaseHandler;
import com.hello008.base.BaseTask;
import com.hello008.base.BaseTaskPool;
import com.hello008.hello.R;
import com.hello008.util.AppCache;
import com.hello008.util.AppClient;
import com.hello008.util.AppUtil;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Message;
import android.util.Log;
import android.widget.ProgressBar;

public class BaseUi extends Activity{
	protected BaseApp app;
	protected BaseTaskPool taskPool;
	private Context context;
	protected BaseHandler handler;
	int newVerCode = 0;
	String newVerName = null;
	
	/** Called when the activity is first created. */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// async task handler
		this.handler = new BaseHandler(this);
		// init task pool
		this.taskPool = new BaseTaskPool(this);
		// init application
		this.app = (BaseApp) this.getApplicationContext();
	}
	
	public void forward(Class<?> classobj){
		Intent intent = new Intent();
		intent.setClass(this, classobj);
		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		this.startActivity(intent);
		this.finish();
	}
	
	public BaseTaskPool getTaskPool () {
		return this.taskPool;
	}
	
	public void loadImage (final String url) {
		taskPool.addTask(0, new BaseTask(){
			@Override
			public void onComplete(){
				AppCache.getCachedImage(getContext(), url);
				sendMessage(BaseTask.LOAD_IMAGE);
			}
		}, 0);
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
  	
  	void down(final ProgressDialog pBar) {
        handler.post(new Runnable() {
            public void run() {
                pBar.cancel();
                update();
            }
        });
  	}
  	
  	void update() {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.fromFile(new File(Environment
                .getExternalStorageDirectory(), "Hello")),
                "application/vnd.android.package-archive");
        startActivity(intent);
    }
  	
  	void downFile(final String url,final ProgressDialog pBar) {
  		new Thread() {
  		    public void run() {
  			HttpClient client = new DefaultHttpClient();
  			HttpGet get = new HttpGet(url);
  			HttpResponse response;
  			try {
  			    response = client.execute(get);
  			    HttpEntity entity = response.getEntity();
  			    long length = entity.getContentLength();
  			    InputStream is = entity.getContent();
  			    FileOutputStream fileOutputStream = null;
  			    if (is != null) {
  				File file = new File(
  					Environment.getExternalStorageDirectory(),
  					"Hello");
  				fileOutputStream = new FileOutputStream(file);
  				byte[] buf = new byte[1024];
  				int ch = -1;
  				int count = 0;
  				while ((ch = is.read(buf)) != -1) {
  				    fileOutputStream.write(buf, 0, ch);
  				    count += ch;
  				    if (length > 0) {
  				    }
  				}
  			    }
  			    fileOutputStream.flush();
  			    if (fileOutputStream != null) {
  				fileOutputStream.close();
  			    }
  			    down(pBar);
  			} catch (ClientProtocolException e) {
  			    e.printStackTrace();
  			} catch (IOException e) {
  			    e.printStackTrace();
  			}
  		    }
  		}.start();
  		}
	
	
	public void downApp (final String url,final ProgressDialog pBar){
		taskPool.addTask(3, new BaseTask(){
			@Override
			public void onComplete(){
				downFile(url,pBar);
			}
		},0);
	}
	
	
	
	public void updateApp (){
		taskPool.addTask(2, new BaseTask(){
			@Override
			public void onComplete(){
				//判断版本更新
				if (getServerVer()) {
					int vercode = AppUtil.getVerCode(getApplicationContext()); 
					if (newVerCode > vercode) {
						sendMessage(BaseTask.UPDATE_APP);
					} else {
						Log.w("polling", "不更新");
					}
				}
			}
		},0);
	}
	
	public void sendMessage (int what) {
		Message m = new Message();
		m.what = what;
		handler.sendMessage(m);
	}
	
	public void sendMessage (int what, String data) {
		Bundle b = new Bundle();
		b.putString("data", data);
		Message m = new Message();
		m.what = what;
		m.setData(b);
		handler.sendMessage(m);
	}
	
	public void sendMessage (int what, int taskId, String data) {
		Bundle b = new Bundle();
		b.putInt("task", taskId);
		b.putString("data", data);
		Message m = new Message();
		m.what = what;
		m.setData(b);
		handler.sendMessage(m);
	}
	
	public void setHandler (BaseHandler handler) {
		this.handler = handler;
	}
	
	public Context getContext () {
		return this;
	}
}
