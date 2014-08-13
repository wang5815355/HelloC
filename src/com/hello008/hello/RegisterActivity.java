package com.hello008.hello;

import java.util.HashMap;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.hello008.base.BaseAuth;
import com.hello008.base.BaseUi;
import com.hello008.model.Customer;
import com.hello008.util.AppClient;

public class RegisterActivity extends BaseUi {
	private Button subButton;//提交注册按钮
	private Button selectButton;//选择国家地区按钮
	private EditText editereagone;//国家或地区值隐藏编辑框
	private String logResult;//登录验证返回字符串
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.register);
		
		subButton = (Button) this.findViewById(R.id.submitbutton);//提交注册资料按钮
		selectButton = (Button) this.findViewById(R.id.selectarea);//选择国家
		editereagone = (EditText) this.findViewById(R.id.editereagone);//国家或地区值隐藏编辑框
		
		//点击国家地区选择按钮
		selectButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				//跳转到选择国家activity
				RegisterActivity.this.forwardunfinish(SelectAreaActivity.class);
			}
		});
		
		//点击注册按钮提交注册信息
		subButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				
			}
		});
		
		//接受跳转回的国家选择结果
		Bundle params = new Bundle();
		params = this.getIntent().getExtras();
		if(params != null){
			String areaname = params.getString("areaname");
			String areanamegone = params.getString("areanamegone");
			selectButton.setText(areaname);
			editereagone.setText(areanamegone);
		}
		
	}
	
	
	
	/**
	 * 异步网络提交
	 */
	 class AnsyTry extends AsyncTask<String, HashMap<String, String>, Dialog>{
	   	 HashMap<String, String> hmap = null;
	   	 JSONObject jo;
	   	 JSONTokener jsonParser;
	   	 Dialog dialog;
	   	 SharedPreferences setting;
//	   	 private ProgressDialog progress;
	      
	       public AnsyTry(HashMap<String, String> hmap,Dialog dialog) {
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
			
			
			if(status.equals("3")){//当登录成功
				dialog.dismiss();
				BaseAuth.setLogin(true);
				setting = getPreferences(Context.MODE_PRIVATE);
				SharedPreferences.Editor editor = setting.edit();
				editor.putBoolean("islogin", true);
				editor.putString("username",hmap.get("username"));
				editor.putString("password",hmap.get("password"));
				editor.commit(); 
				
				Customer customer = Customer.getInstance();
				customer.setSid(sid);//设置sessionid
				RegisterActivity.this.forward(IndexActivity.class);
//				Toast.makeText(MainActivity.this,status,Toast.LENGTH_SHORT).show();
			}else{//登录不成功
				BaseAuth.setLogin(true);
//				progress.dismiss();
				dialog.cancel();
				Toast.makeText(RegisterActivity.this,info,Toast.LENGTH_SHORT).show();
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
