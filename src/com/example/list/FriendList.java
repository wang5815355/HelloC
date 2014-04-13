package com.example.list;

import java.util.List;
import java.util.Map;

import com.example.base.BaseUi;
import com.example.hello.R;
import com.example.util.AppCache;

import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class FriendList extends BaseAdapter{
	private BaseUi ui;
	private LayoutInflater inflater;
	private List<Map<String, Object>> friends;
	
	public final class FriendListItem {
		public ImageView faceimg;
		public TextView uname;
		public TextView uphone;
	}
	
	public FriendList(BaseUi ui, List<Map<String, Object>> friends) {
		super();
		this.ui = ui;
		this.inflater = LayoutInflater.from(this.ui);
		this.friends = friends;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return this.friends!=null? this.friends.size(): 0 ;
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return this.friends.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}

	@Override
	public View getView(int p, View v, ViewGroup vg) {
		FriendListItem friendItem = null;
		if (v == null) {
			v = inflater.inflate(R.layout.friendlist, null);
		}else {
			friendItem = (FriendListItem) v.getTag();
		}
		
		friendItem = new FriendListItem();
		friendItem.faceimg = (ImageView) v.findViewById(R.id.faceimg);
		friendItem.uname = (TextView) v.findViewById(R.id.uname);
		friendItem.uphone = (TextView) v.findViewById(R.id.uphone);
		
		friendItem.uname.setText(friends.get(p).get("uname").toString());
		friendItem.uphone.setText(friends.get(p).get("uphone").toString());
		String faceimgurl = friends.get(p).get("faceimgurl").toString();
		Bitmap faceimg = AppCache.getImage("http://www.hello008.com/Public/Uploads/"+faceimgurl);
		if(faceimg != null){
			friendItem.faceimg.setImageBitmap(faceimg);
		}
		
		return v;
	}
	
	

}
