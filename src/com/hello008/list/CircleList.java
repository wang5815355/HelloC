package com.hello008.list;

import java.util.List;
import java.util.Map;

import com.hello008.base.BaseUi;
import com.hello008.hello.R;
import com.hello008.hello.testActivity;
import com.hello008.util.AppCache;

import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class CircleList extends BaseAdapter{
	private testActivity ui;
	private LayoutInflater inflater;
	private List<Map<String, Object>> areas = null;
	
	public final class AreaListItem {
		public TextView circlename;
		public TextView circleid;
	}
	
	public CircleList(testActivity ts, List<Map<String, Object>> areas) {
		super();
		this.ui = ts;
		this.inflater = LayoutInflater.from(this.ui);
		this.areas = areas;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return this.areas!=null? this.areas.size(): 0 ;
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return this.areas.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}
	
	public void setCircleList(List<Map<String, Object>> areas){
		this.areas = null;
		this.areas = areas;
	}

	@Override
	public View getView(int p, View v, ViewGroup vg) {
		AreaListItem AreaItem = null;
		if (v == null) {
			v = inflater.inflate(R.layout.arealist, null);
		}else {
			AreaItem = (AreaListItem) v.getTag();
		}
		
		AreaItem = new AreaListItem();
		AreaItem.circlename = (TextView) v.findViewById(R.id.circlename);
		AreaItem.circleid = (TextView) v.findViewById(R.id.areanamegone);
		
		AreaItem.circleid.setText(areas.get(p).get("circleid").toString());
		AreaItem.circlename.setText(areas.get(p).get("circlename").toString());
		
		return v;
	}
	
	

}
