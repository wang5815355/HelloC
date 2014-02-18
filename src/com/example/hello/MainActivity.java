package com.example.hello;

import com.example.base.BaseAuth;
import com.example.base.BaseUi;

import android.os.Bundle;
import android.view.Menu;

public class MainActivity extends BaseUi {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        //判断当前用户是否登录
        if(BaseAuth.isLogin()){//若登录则跳转
        	this.forward(IndexActivity.class);
        }
        //控件对象初始化，及记住密码
        
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    
}
