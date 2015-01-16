package com.hello008.fragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.hello008.hello.R;
import com.hello008.hello.testActivity;
import com.hello008.list.CircleList;


public class fragment2 extends Fragment{
	private View mMainView;
	private TextView tv;
	private Button btn;
	private CircleList areaList;
	private List<Map<String, Object>> areas = null;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		LayoutInflater inflater = getActivity().getLayoutInflater();
		mMainView = inflater.inflate(R.layout.frag2, (ViewGroup)getActivity().findViewById(R.id.viewpager), false);
		
		areas = new ArrayList<Map<String,Object>>();
		Map<String, Object> areaO = new HashMap<String, Object>();
		Map<String, Object> area1 = new HashMap<String, Object>();
		areaO.put("areaname","+86 中国");
		areaO.put("areanamegone","86");
		area1.put("areaname","+61 Australia");
		area1.put("areanamegone","61");
		areas.add(areaO);
		areas.add(area1);
		
		areaList = new CircleList(testActivity.ts, areas);
		ListView list = (ListView) mMainView.findViewById(R.id.arealist);
		list.setAdapter(areaList);
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