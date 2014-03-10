package com.example.hello;

import java.util.HashMap;
import java.util.Map;

import org.apache.http.protocol.HTTP;

import com.example.base.C;
import com.example.base.BaseAuth;
import com.example.base.BaseUi;
import com.example.base.BaseApp;
import com.example.util.AppClient;

import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends BaseUi {
	private EditText editText;//登录账号
	private EditText editPass;//登录密码
	private Button logButton;//登录按钮
	private String logResult;//登录验证返回字符串
	
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
				Toast toast = Toast.makeText(MainActivity.this,"你好",Toast.LENGTH_SHORT);
				//获取当前用户登录账号以及密码
				String username = editText.getText().toString();
				String password = editPass.getText().toString();
				
				HashMap<String, String> map = new HashMap<String, String>();
				map.put("username",username );
				map.put("password",password);
				
				AppClient client = new AppClient("/Public/register",HTTP.UTF_8,1);//客户端初始化
				try {
					logResult = client.post(map);
					
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
        
      //登录按钮点击事件
//       OnClickListener onClickListener = new OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				// TODO Auto-generated method stub
//				switch(v.getId()){
//					case R.id.logbutton:
//						
//						
//					break;
//				}
//			}
//       };
       
       
    }
    
    

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    
}
