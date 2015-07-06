package com.hello008.hello;

import java.util.HashMap;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import com.hello008.base.BaseAuth;
import com.hello008.base.BaseFragmentUi;
import com.hello008.base.BaseUi;
import com.hello008.hello.MainActivity.AnsyTry;
import com.hello008.model.Customer;
import com.hello008.util.AppClient;

import android.os.AsyncTask;
import android.os.Bundle;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Rect;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.Toast;

public class LoginLoadActivity extends BaseUi {
	private String logResult;
	
	/**
     * 设置遮罩dialog样式
     * @author wangkai
     * */
    private void setParams(LayoutParams lay) {  
    	 DisplayMetrics dm = new DisplayMetrics();  
    	 getWindowManager().getDefaultDisplay().getMetrics(dm);  
    	 Rect rect = new Rect();  
    	 View view = getWindow().getDecorView();  
    	 view.getWindowVisibleDisplayFrame(rect);  
    	 lay.height = dm.heightPixels - rect.top;  
    	 lay.width = dm.widthPixels;  
     } 
	 
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login_load);
		
		SharedPreferences setting;
        setting = getSharedPreferences("login",Context.MODE_APPEND);
        HashMap<String, String> map = new HashMap<String, String>();
		map.put("username",setting.getString("username",""));
		map.put("password",setting.getString("password",""));
		
		Log.w("testuser", setting.getString("username","")+"123");
		
		//创建遮罩dialog
		Dialog dialog  = new Dialog(LoginLoadActivity.this, R.style.mydialog);
		dialog.setContentView(R.layout.index_load);  
		LayoutParams lay = dialog.getWindow().getAttributes();  
		setParams(lay);//设置遮罩参数  
//		dialog.show();
		
		AnsyTry anys=new AnsyTry(map,dialog,1);//tag 0 普通登录 1，自动登录
		anys.execute();
	}
	
	class AnsyTry extends AsyncTask<String, HashMap<String, String>, Dialog>{
	   	 HashMap<String, String> hmap = null;
	   	 JSONObject jo;
	   	 JSONTokener jsonParser;
	   	 Dialog dialog;
	   	 SharedPreferences setting;
	   	 int tag;
//	   	 private ProgressDialog progress;
	      
	       public AnsyTry(HashMap<String, String> hmap,Dialog dialog,int tag) {
	           super();
	           this.hmap = hmap;
	           this.dialog = dialog;
	       }
	       
	       @Override
	       protected Dialog doInBackground(String... params) {
	       	AppClient client = new AppClient("/Public/register");//客户端初始化
	      
				try {
					logResult = client.post(hmap);
					if(!(logResult.equals("网络错误") || logResult == null)){
						//json解析
						jsonParser = new JSONTokener(logResult);  
						jo = (JSONObject) jsonParser.nextValue();
					}else{
						jo = new JSONObject().put("status","-1");
						jo.put("info","网络错误");
					}
					
				} catch (Exception e) {
					e.printStackTrace();
				}
	           return dialog;
	       }

	       @SuppressLint("CommitPrefEdits")
	       @Override
	       protected void onPostExecute(Dialog dialog) {
	    	String info = null;
	    	String status = null;
	    	String sid = null;
	    	String complete = null;
	    	if(jo != null){
	    		try {
	    			info = jo.getString("info");
	    			status = jo.getString("status");
	    			sid = jo.getString("sid");
	    			complete = jo.getString("complete");
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
			
			if(status.equals("3")){//当登录成功
				dialog.dismiss();
				BaseAuth.setLogin(true);
				setting = getPreferences(Context.MODE_PRIVATE);
				SharedPreferences.Editor editor = setting.edit();
				editor.putBoolean("islogin", true);
				editor.putString("username",hmap.get("username"));
				editor.putString("password",hmap.get("password"));
				
				Customer customer = Customer.getInstance();
				customer.setSid(sid);//设置sessionid
//				customer.setLoginname(hmap.get("username"));//保存当前登录用户的登录名称
				
//				Log.w("test1===",hmap.get("username"));
				if(complete.equalsIgnoreCase("0")){
					editor.putString("regstep","1");
					editor.commit(); 
					LoginLoadActivity.this.forward(RegisterTwoActivity.class);
				}else{
					editor.putString("regstep","2");
					editor.commit(); 
					LoginLoadActivity.this.forward(testActivity.class);
				}
				
//				Toast.makeText(LoginLoadActivity,status,Toast.LENGTH_SHORT).show();
			}else{//登录不成功
				BaseAuth.setLogin(true);
//				progress.dismiss();
				dialog.cancel();
				Toast.makeText(LoginLoadActivity.this,info,Toast.LENGTH_SHORT).show();
			}
			
	       }

	       @Override
	       protected void onPreExecute() {
	           // TODO Auto-generated method stub\
//	    	   progress = ProgressDialog.show(LoginLoadActivity, null, "正在登录…");
	           super.onPreExecute();
	       }

	       protected void onProgressUpdate(Integer... values) {
	           // TODO Auto-generated method stub
	       }

	    } 


}
