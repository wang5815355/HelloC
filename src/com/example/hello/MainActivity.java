package com.example.hello;

import java.util.HashMap;

import com.example.base.C;
import com.example.base.BaseAuth;
import com.example.base.BaseUi;
import com.example.base.BaseApp;

import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends BaseUi {
	private EditText editText;
	private EditText editPass;
	private Button logButton;
	
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
        
      //登录按钮点击事件
       OnClickListener onClickListener = new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				switch(v.getId()){
					case R.id.logbutton:
					break;
				}
			}
       };
       
       
    }
    
    /**
     * 登录处理
     * @author wangkai
     */
    private void doTaskLogin() {
		app.setLong(System.currentTimeMillis());
		if (editText.length() > 0 && editPass.length() > 0) {
			HashMap<String, String> urlParams = new HashMap<String, String>();
			urlParams.put("name", editText.getText().toString());//账号
			urlParams.put("pass", editPass.getText().toString());//密码
			
			try {
				this.doTaskAsync(C.task.login, C.api.login, urlParams);
			} catch (Exception e) {
				e.printStackTrace();
			}
			
		}
	}
    

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    
}
