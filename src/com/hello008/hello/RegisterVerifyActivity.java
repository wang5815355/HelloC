package com.hello008.hello;

import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;

public class RegisterVerifyActivity extends Activity {
	EditText phonenumberv;
	TextView verifyv;
	private TimeCount time;
	
	
	/**
	 * 短信验证初始化
	 * @author wangkai
	 */
	private void initSms(){
		//调用短信验证码 初始化
		SMSSDK.initSDK(this,"8993354f32b8","c8ea55c1f8ca7b67042c1f76131f2514");
		 EventHandler eh=new EventHandler(){
	            @Override
	            public void afterEvent(int event, int result, Object data) {
	               if (result == SMSSDK.RESULT_COMPLETE) {
	                //回调完成
	                if (event == SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE) {
	                //提交验证码成功
	                }else if (event == SMSSDK.EVENT_GET_VERIFICATION_CODE){
	                //获取验证码成功
	                }else if (event ==SMSSDK.EVENT_GET_SUPPORTED_COUNTRIES){
	                //返回支持发送验证码的国家列表
	                } 
	              }else{                                                                 
	                 ((Throwable)data).printStackTrace(); 
	                 Log.w("testsms",((Throwable)data).getMessage());
	          }
	      } 
	   };
	   
	   SMSSDK.registerEventHandler(eh); //注册短信回调
	}
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register_verify);
		
		phonenumberv = (EditText) findViewById(R.id.phonenumber);
		verifyv = (TextView) findViewById(R.id.verifycode);
		
	    TelephonyManager tm = (TelephonyManager) this.getSystemService(Context.TELEPHONY_SERVICE);
	    String phonenumber = tm.getLine1Number();
	    if(phonenumber!= null){
	    	phonenumber = phonenumber.substring(3);
	    	phonenumberv.setText(phonenumber); 
	    	phonenumberv.setSelection(phonenumber.length());
	    }
	    
	    initSms();
        // 重新获取验证码短信
		SMSSDK.getVerificationCode("86", "18680687476", null);
	    
		//启动计时器
	    time = new TimeCount(10000, 1000);
	    time.start();
	}
	
	/* 定义一个倒计时的内部类 */
	class TimeCount extends CountDownTimer {
		public TimeCount(long millisInFuture, long countDownInterval) {
		super(millisInFuture, countDownInterval);//参数依次为总时长,和计时的时间间隔
		}
		@Override
		public void onFinish() {//计时完毕时触发
//			checking.setText("重新验证");
//			checking.setClickable(true);
		}
		@Override
		public void onTick(long millisUntilFinished){//计时过程显示
//			checking.setClickable(false);
			verifyv.setText("自动验证手机号码  "+millisUntilFinished /1000+"秒");
		}
	}

}
