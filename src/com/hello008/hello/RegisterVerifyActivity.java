package com.hello008.hello;

import java.util.HashMap;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import com.hello008.base.BaseAuth;
import com.hello008.base.BaseUi;
import com.hello008.hello.MainActivity.AnsyTry;
import com.hello008.model.Customer;
import com.hello008.util.AppClient;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;
import cn.smssdk.gui.SMSReceiver;

public class RegisterVerifyActivity extends BaseUi {
	EditText phonenumberv;
	TextView verifyv;
	private TimeCount time;
	String phonenumber;
	
	
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
	    phonenumber = tm.getLine1Number();
	    if(phonenumber!= null){
	    	phonenumber = phonenumber.substring(3);
	    	phonenumberv.setText(phonenumber); 
	    	phonenumberv.setSelection(phonenumber.length());
	    }
	    time = new TimeCount(20000, 1000);//计时器初始化
	    initSms();
        // 获取验证码短信
	    Log.w("testsms", phonenumber);
		SMSSDK.getVerificationCode("86",phonenumber, null);
	    
		SMSReceiver smsReceiver = new SMSReceiver(new SMSSDK.VerifyCodeReadListener() {
			@Override
			public void onReadVerifyCode(final String verifyCode) {
				//启动轮询service
				 new Handler().postDelayed(new Runnable(){  
				     public void run() {  
//				    	 time.cancel();
				    	 verifyv.setText("验证码:"+verifyCode);
				    	 HashMap<String, String> map = new HashMap<String, String>();
				    	 // 获取验证码短信
				 	     Log.w("testsms","+1"+ phonenumber);
				 	     map.put("phonenumber",phonenumber);
				 	     map.put("verifycode",verifyCode);
							
				 	     AnsyTry anys=new AnsyTry(map);//tag 0 普通登录 1，自动登录
				 	     anys.execute();
				     }  
				}, 0);
			}
		});
		
		this.registerReceiver(smsReceiver, new IntentFilter(
				"android.provider.Telephony.SMS_RECEIVED"));
		
		//启动计时器
	    time.start();
	}
	
	/* 定义一个倒计时的内部类 */
	class TimeCount extends CountDownTimer {
		public TimeCount(long millisInFuture, long countDownInterval) {
		super(millisInFuture, countDownInterval);//参数依次为总时长,和计时的时间间隔
		}
		@Override
		public void onFinish() {//计时完毕时触发
			
		}
		@Override
		public void onTick(long millisUntilFinished){//计时过程显示
			verifyv.setText("自动验证手机号码  "+millisUntilFinished /1000+"秒");
		}
	}
	
	/**
	 * 异步网络提交
	 */
	 class AnsyTry extends AsyncTask<String, HashMap<String, String>, Dialog>{
	   	 HashMap<String, String> hmap = null;
	   	 JSONObject jo;
	   	 JSONTokener jsonParser;
	   	 SharedPreferences setting;
//	   	 private ProgressDialog progress;
	   	 String regResult;
	      
	       public AnsyTry(HashMap<String, String> hmap) {
	           super();
	           this.hmap = hmap;
	       }
	       
	       @Override
	       protected Dialog doInBackground(String... params) {
	       	AppClient client = new AppClient("/Public/registerIn_phone_verify");//客户端初始化
				try {
					regResult = client.post(hmap);
					Log.w("testsms",regResult.toString() );
					if(!(regResult.equals("网络错误") || regResult == null)){
						//json解析
						jsonParser = new JSONTokener(regResult);  
						jo = (JSONObject) jsonParser.nextValue();
					}else{
						jo = new JSONObject().put("status","-1");
						jo.put("info","网络错误");
					}
					
				} catch (Exception e) {
					e.printStackTrace();
				}
	           return null;
	       }

	       @SuppressLint("CommitPrefEdits")
	       @Override
	       protected void onPostExecute(Dialog dialog) {
	    	String info = null;
	    	String status = null;
	    	String sid = null;
	    	
	    	if(jo != null){
	    		try {
	    			info = jo.getString("info");
	    			status = jo.getString("status");
	    			sid = jo.getString("sid");
	    		} catch (JSONException e) {
	    			// TODO Auto-generated catch block
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
			
			
			if(status.equals("5")){//当注册成功
				dialog.dismiss();
				BaseAuth.setLogin(true);
				setting = getSharedPreferences("register", Context.MODE_PRIVATE);
				SharedPreferences.Editor editor = setting.edit();
				editor.putBoolean("islogin", true);
				editor.putString("username",hmap.get("username"));
				editor.putString("password",hmap.get("password"));
				editor.putString("regstep","1");//注册步骤 1，提交资料，2 提交头像姓名
				editor.commit(); 
				
				Customer customer = Customer.getInstance();
				customer.setSid(sid);//设置sessionid
				RegisterVerifyActivity.this.forward(RegisterTwoActivity.class);
				Toast.makeText(RegisterVerifyActivity.this,"注册成功",Toast.LENGTH_SHORT).show();
			}else{//注册不成功
				BaseAuth.setLogin(true);
//				progress.dismiss();
				dialog.cancel();
				Toast.makeText(RegisterVerifyActivity.this,info,Toast.LENGTH_SHORT).show();
			}
			
	       }

	       @Override
	       protected void onPreExecute() {
	           // TODO Auto-generated method stub\
//	    	   progress = ProgressDialog.show(MainActivity.this, null, "正在登录…");
	           super.onPreExecute();
	       }

	       protected void onProgressUpdate(Integer... values) {
	           // TODO Auto-generated method stub
	       }

	    }

}
