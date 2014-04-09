package com.example.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.base.BaseTask;
import com.example.base.BaseTaskPool;
import com.example.base.BaseUi;
import com.example.model.Friend;

public class JsonParser extends BaseUi{
	protected BaseTaskPool taskPool = new BaseTaskPool(this);
	 
	/**
	 * 好友数据json解析
	 * @author wangkai
	 */
	public List<Map<String, Object>> parseJsonList(String json){
		String uname = null;//好友名称
		String uphone = null;//好友手机号码
		String faceimgurl = null;//好友头像图片URL
		List<Map<String, Object>> friends = null;
		
		try {
				JSONArray jsa = new JSONObject(json).getJSONArray("info");
				friends = new ArrayList<Map<String,Object>>();
				for(int i = 0; i<jsa.length();i++){
					JSONObject jso = (JSONObject)jsa.opt(i);
					uname = jso.getString("uname1");
					uphone = jso.getString("uphonenumber1");
					faceimgurl = jso.getString("faceimage1");
					Map<String, Object> friend = new HashMap<String, Object>();
					
					//异步加载好友头像图片
					loadImage("http://www.hello008.com/Public/Uploads/"+faceimgurl);
					
					friend.put("uname",uname);
					friend.put("uphone",uphone);
					friends.add(friend);
				}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		return friends;
	}
	
	/**
	 * 线程池加载用户图片
	 * @author wangkai
	 */
	public void loadImage (final String url) {
		taskPool.addTask(0, new BaseTask(){
			@Override
			public void onComplete(){
				AppCache.getCachedImage(getContext(), url);
				sendMessage(BaseTask.LOAD_IMAGE);
			}
		}, 0);
	}
}
