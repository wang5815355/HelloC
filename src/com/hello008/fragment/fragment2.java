package com.hello008.fragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.hello008.hello.R;
import com.hello008.hello.testActivity;
import com.hello008.list.CircleList;
import com.hello008.sqlite.FriendSqlite;


public class fragment2 extends Fragment{
	private View mMainView;
	private TextView tv;
	private Button btn;
	private CircleList areaList;
//	private List<Map<String, Object>> areas = null;
	public static List<Map<String, Object>> areas = null;
	public static int tagthis = 0;//当tag

	LocalBroadcastManager broadcastManager = LocalBroadcastManager.getInstance(testActivity.ts);
	IntentFilter intentFilter = new IntentFilter();
	
	BroadcastReceiver mItemViewListClickReceiver = new BroadcastReceiver() {
	            @Override
	            public void onReceive(Context context, Intent intent){
	                Log.w("test1===","123");
	                areas =  (List<Map<String, Object>>)intent.getSerializableExtra("areas");
	                Log.w("test1===",areas.get(0).toString());
	                areaList = new CircleList(testActivity.ts, areas);
	    			ListView list = (ListView) mMainView.findViewById(R.id.circlelist);
	    			list.setAdapter(areaList);
	            }
	 };
	
	public static void setCircles(List<Map<String, Object>> circles,int tag){
		areas = circles;
		tagthis =  tag;
	} 
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		LayoutInflater inflater = getActivity().getLayoutInflater();
		mMainView = inflater.inflate(R.layout.frag2, (ViewGroup)getActivity().findViewById(R.id.viewpager), false);
		
		// 广播接收器的动态注册
		intentFilter.addAction("ACTION_FRAGMENT2");
		broadcastManager.registerReceiver(mItemViewListClickReceiver, intentFilter);
		
//		areas = new ArrayList<Map<String,Object>>();
//		Map<String, Object> areaO = new HashMap<String, Object>();
//		Map<String, Object> area1 = new HashMap<String, Object>();
//		areaO.put("areaname","+86 中国");
//		areaO.put("areanamegone","86");
//		area1.put("areaname","+61 Australia");
//		area1.put("areanamegone","61");
//		areas.add(areaO);
//		areas.add(area1);
		if(tagthis == 1){
			
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		ViewGroup p = (ViewGroup) mMainView.getParent(); 
        if (p != null) { 
            p.removeAllViewsInLayout(); 
        } 
		
		return mMainView;
	}
	
	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}

	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}

	@Override
	public void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
	}

	@Override
	public void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
	}

}