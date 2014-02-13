package com.example.hello;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import com.example.base.*;

public class IndexActivity extends Activity{
	 @Override
	    protected void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        setContentView(R.layout.index);
	        
	        //判断当前用户是否登录
	        
	        //控件对象初始化，及记住密码
	        
	    }


	    @Override
	    public boolean onCreateOptionsMenu(Menu menu) {
	        // Inflate the menu; this adds items to the action bar if it is present.
	        getMenuInflater().inflate(R.menu.main, menu);
	        return true;
	    }

}
