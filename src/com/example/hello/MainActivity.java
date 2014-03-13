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

import android.app.Activity;
import android.app.Dialog;
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
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);//设置模板界面
        
        //判断当前用户是否登录
        if(BaseAuth.isLogin()){//若登录则跳转
        	this.forward(IndexActivity.class);
        }
        
        //控件对象初始化，及记住密码
        editText = (EditText) this.findViewById(R.id.editText1);//登陆账号
        editPass = (EditText) this.findViewById(R.id.editText2);//登录密码
        logButton = (Button) this.findViewById(R.id.logbutton);
        
        logButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				//获取当前用户登录账号以及密码
				String username = editText.getText().toString();
				String password = editPass.getText().toString();
				
				HashMap<String, String> map = new HashMap<String, String>();
				map.put("username",username );
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
				
				//json解析
				jsonParser = new JSONTokener(logResult);  
				jo = (JSONObject) jsonParser.nextValue();
//				BaseMessage str = AppUtil.getMessage(logResult);
//				str.getResult();
				
			} catch (Exception e) {
				e.printStackTrace();
			}
           return dialog;
       }

       @Override
       protected void onPostExecute(Dialog dialog) {
    	String info = null;
    	String status = null;
		try {
			info = jo.getString("info");
			status = jo.getString("status");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		if(status.equals("3")){//当登录成功
			BaseAuth.setLogin(true);
			new MainActivity().forward(IndexActivity.class);
			Toast.makeText(MainActivity.this,status,Toast.LENGTH_SHORT).show();
		}else{//登录不成功
			dialog.cancel();
			Toast.makeText(MainActivity.this,info,Toast.LENGTH_SHORT).show();
		}
		
       }

       @Override
       protected void onPreExecute() {
           // TODO Auto-generated method stub\
           System.out.println("pretExecute------");
           super.onPreExecute();
       }

       protected void onProgressUpdate(Integer... values) {
           // TODO Auto-generated method stub
       }

    } 
    
}
