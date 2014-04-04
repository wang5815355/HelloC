package com.example.base;

import java.util.concurrent.ExecutorService;

import com.example.base.BaseHandler;
import com.example.base.BaseTaskPool;
import com.example.base.BaseTask;
import com.example.util.AppCache;
import com.example.base.BaseApp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;

public class BaseUi extends Activity{
	protected BaseApp app;
	protected BaseTaskPool taskPool;
	private Context context;
	protected BaseHandler handler;
	
	
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
	
	
	public Context getContext () {
		return this;
	}
}
