package com.hello008.hello;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import com.hello008.base.BaseUi;

public class RegisterActivity extends BaseUi {
	private Button subButton;//提交注册按钮
	private Button selectButton;//选择国家地区按钮
	private EditText editereagone;//国家或地区值隐藏编辑框
	
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
}
