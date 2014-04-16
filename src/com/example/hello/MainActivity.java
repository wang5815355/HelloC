package com.example.hello;

import java.util.HashMap;
import java.util.Map;

import org.apache.http.protocol.HTTP;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import com.example.base.BaseMessage;
import com.example.base.C;
import com.example.base.BaseAuth;
import com.example.base.BaseUi;
import com.example.base.BaseApp;
import com.example.model.Customer;
import com.example.util.AppClient;
import com.example.util.AppUtil;
import com.example.util.HttpUtil;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Rect;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends BaseUi {
	private EditText editText;//登录账号
	private EditText editPass;//登录密码
	private Button logButton;//登录按钮
	private String logResult;//登录验证返回字符串
	BaseMessage str;
	
	static private MainActivity main = null;
	
	static public MainActivity getInstance () {
		if (MainActivity.main == null) {
			MainActivity.main = new MainActivity();
		}
		return MainActivity.main;
	}
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);//设置模板界面
        SharedPreferences setting;
        //控件对象初始化，及记住密码
        editText = (EditText) this.findViewById(R.id.editText1);//登陆账号
        editPass = (EditText) this.findViewById(R.id.editText2);//登录密码
        logButton = (Button) this.findViewById(R.id.logbutton);
        final BaseApp baseApp = (BaseApp) this.getApplicationContext();
        
        //判断当前用户是否登录
        setting = getPreferences(Context.MODE_APPEND);
		SharedPreferences.Editor editor = setting.edit();
        editor.putBoolean("islogin",false);
        BaseAuth.setLogin(false);
        Boolean islogin = setting.getBoolean("islogin",false);
        editor.commit();
        if(BaseAuth.isLogin() != false || islogin != false){//若登录则跳转
        	this.forward(IndexActivity.class);
        }
        
        editText.setText(setting.getString("username", ""));
        editPass.setText(setting.getString("password", ""));
        
        logButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				//判断网络连接状态
				Integer netType = HttpUtil.getNetType(MainActivity.this);
				if(netType == HttpUtil.NONET_INT){//网络未连接
					Toast.makeText(MainActivity.this,"网络未连接,请查看",Toast.LENGTH_SHORT).show();
					return;
				}
				
				//获取当前用户登录账号以及密码
				String username = editText.getText().toString();
				String password = editPass.getText().toString();
				
				HashMap<String, String> map = new HashMap<String, String>();
				map.put("username",username);
				map.put("password",password);
				
				//创建遮罩dialog
				Dialog dialog  = new Dialog(MainActivity.this, R.style.mydialog);
				dialog.setContentView(R.layout.main_dialog);  
				LayoutParams lay = dialog.getWindow().getAttributes();  
				setParams(lay);//设置遮罩参数  
				dialog.show();
				
				AnsyTry anys=new AnsyTry(map,dialog);
				anys.execute();
			}
			
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
	
		});
        
    }
    
    class AnsyTry extends AsyncTask<String, HashMap<String, String>, Dialog>{
   	 HashMap<String, String> hmap = null;
   	 JSONObject jo;
   	 JSONTokener jsonParser;
   	 Dialog dialog;
   	 SharedPreferences setting;
   	 private ProgressDialog progress;
      
       public AnsyTry(HashMap<String, String> hmap,Dialog dialog) {
           super();
           this.hmap = hmap;
           this.dialog = dialog;
       }
       
       @Override
       protected Dialog doInBackground(String... params) {
           // TODO Auto-generated method stub
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
		try {
			info = jo.getString("info");
			status = jo.getString("status");
			sid = jo.getString("sid");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
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
			MainActivity.this.forward(IndexActivity.class);
//			Toast.makeText(MainActivity.this,status,Toast.LENGTH_SHORT).show();
		}else{//登录不成功
			BaseAuth.setLogin(true);
			progress.dismiss();
			dialog.cancel();
			Toast.makeText(MainActivity.this,info,Toast.LENGTH_SHORT).show();
		}
		
       }

       @Override
       protected void onPreExecute() {
           // TODO Auto-generated method stub\
    	   progress = ProgressDialog.show(MainActivity.this, null, "正在登录…");
           super.onPreExecute();
       }

       protected void onProgressUpdate(Integer... values) {
           // TODO Auto-generated method stub
       }

    } 
    
}
