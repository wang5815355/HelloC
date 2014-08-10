package com.hello008.hello;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

import com.hello008.base.BaseUi;
import com.hello008.list.AreaList;
import com.hello008.util.AppCache;


/**
 * 注册选择国际或地区activity
 * @author wangkai
 */
public class SelectAreaActivity extends BaseUi{
	private AreaList areaList;
	private List<Map<String, Object>> areas = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.selectarea);
		
		areas = new ArrayList<Map<String,Object>>();
		Map<String, Object> areaO = new HashMap<String, Object>();
		Map<String, Object> area1 = new HashMap<String, Object>();
		areaO.put("areaname","+86 中国");
		areaO.put("areanamegone","86");
		area1.put("areaname","+61 Australia");
		area1.put("areanamegone","61");
		areas.add(areaO);
		areas.add(area1);
		
		areaList = new AreaList(SelectAreaActivity.this, areas);
		ListView list = (ListView) findViewById(R.id.arealist);
		list.setAdapter(areaList);
		
		
		//设置listviewitem监听器
		list.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
					int arg2, final long arg3) {
				//获取用户选择国家地区
				String areaname = (String)areas.get((int)arg3).get("areaname");
				String areanamegone = (String)areas.get((int)arg3).get("areanamegone");
				Bundle b = new Bundle();
				b.putString("areaname", areaname);
				b.putString("areanamegone", areanamegone);
				
				//跳回注册页面
				SelectAreaActivity.this.forward(RegisterActivity.class, b);
				
			}
		});
	}
}
