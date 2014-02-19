package com.example.base;

import com.example.base.BaseApp;

import android.app.Activity;
import android.content.Intent;

public class BaseUi extends Activity{
	protected BaseApp app;
	
	public void forward(Class<?> classobj){
		Intent intent = new Intent();
		intent.setClass(this, classobj);
		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		this.startActivity(intent);
		this.finish();
	}
	
	
	
}
