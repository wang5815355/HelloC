package com.hello008.list;

import java.util.List;
import java.util.Map;

import com.hello008.base.BaseUi;
import com.hello008.hello.R;
import com.hello008.util.AppCache;

import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class AreaList extends BaseAdapter{
	private BaseUi ui;
	private LayoutInflater inflater;
	private List<Map<String, Object>> areas = null;
	
	public final class AreaListItem {
		public TextView areaname;
		public TextView areanamegone;
	}
	
	public AreaList(BaseUi ui, List<Map<String, Object>> areas) {
		super();
		this.ui = ui;
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
	
	public void setFriendList(List<Map<String, Object>> areas){
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
		AreaItem.areaname = (TextView) v.findViewById(R.id.areaname);
		AreaItem.areanamegone = (TextView) v.findViewById(R.id.areanamegone);
		
		AreaItem.areaname.setText(areas.get(p).get("areaname").toString());
		AreaItem.areanamegone.setText(areas.get(p).get("areanamegone").toString());
		
		return v;
	}
	
	

}
