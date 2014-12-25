package com.hello008.list;

import java.util.List;
import java.util.Map;

import com.hello008.base.BaseFragmentUi;
import com.hello008.base.BaseUi;
import com.hello008.base.C;
import com.hello008.hello.R;
import com.hello008.hello.testActivity;
import com.hello008.util.AppCache;
import com.hello008.util.MemoryCache;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.v4.widget.SimpleCursorAdapter.ViewBinder;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class FriendList extends BaseAdapter{
	private BaseUi ui;
	private LayoutInflater inflater;
	private List<Map<String, Object>> friends = null;
	MemoryCache memoryCache = new MemoryCache();
	
	BaseFragmentUi fui;
	
	 private boolean mBusy = false;

     public void setFlagBusy(boolean busy) {
             this.mBusy = busy;
     }
	
	public static class FriendListItem {
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

	public FriendList(BaseFragmentUi fui,
			List<Map<String, Object>> friends) {
		this.fui = fui;
		this.inflater = LayoutInflater.from(this.fui);
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
	
	public void setFriendList(List<Map<String, Object>> friends){
		this.friends = null;
		this.friends = friends;
	}

	@Override
	public View getView(int p, View v, ViewGroup vg) {
		FriendListItem friendItem = null;
		
		if (v == null) {
			v = inflater.inflate(R.layout.friendlist, null);
			friendItem = new FriendListItem();
			friendItem.faceimg = (ImageView) v.findViewById(R.id.faceimg);
			friendItem.uname = (TextView) v.findViewById(R.id.uname);
			friendItem.uphone = (TextView) v.findViewById(R.id.uphone);
			v.setTag(friendItem);
		}else {
			friendItem = (FriendListItem) v.getTag();
		}
		
//		friendItem = new FriendListItem();
		friendItem.faceimg = (ImageView) v.findViewById(R.id.faceimg);
		friendItem.uname = (TextView) v.findViewById(R.id.uname);
		friendItem.uphone = (TextView) v.findViewById(R.id.uphone);
		
		friendItem.uname.setText(friends.get(p).get("uname").toString());
		friendItem.uphone.setText(friends.get(p).get("uphone").toString());
		String faceimgurl = friends.get(p).get("faceimgurl").toString();
		
		//当内存存在图片时优先加载内存
		Bitmap faceimg = memoryCache.get(faceimgurl);
		if(faceimg==null){
			faceimg = AppCache.getImage("http://www.hello008.com/Public/Uploads/"+faceimgurl);
			memoryCache.put(faceimgurl, faceimg);
		}
		
		if(faceimg != null){
//			Drawable bd = new BitmapDrawable(faceimg);
			friendItem.faceimg.setImageBitmap(faceimg);
//			friendItem.faceimg.setImageDrawable(bd);
		}
		
//		if(!faceimg.isRecycled() ){ 
//			faceimg.recycle();   //回收图片所占的内存 
//	         System.gc();
//		} 
		
		return v;
	}
	
	

}
