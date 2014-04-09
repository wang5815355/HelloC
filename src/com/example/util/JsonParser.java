package com.example.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.graphics.Bitmap;

import com.example.util.AppCache;
import com.example.base.BaseUi;

public class JsonParser extends BaseUi{
	
	/**
	 * 好友数据json解析
	 * @author wangkai
	 */
	public static List<Map<String, Object>> parseJsonList(String json){
		String uname = null;//好友名称
		String uphone = null;//好友手机号码
		String faceimgurl = null;//好友头像图片URL
		Bitmap faceimg = null;
		List<Map<String, Object>> friends = null;
		
		try {
				JSONArray jsa = new JSONObject(json).getJSONArray("info");
				friends = new ArrayList<Map<String,Object>>();
				for(int i = 0; i<jsa.length();i++){
					JSONObject jso = (JSONObject)jsa.opt(i);
					uname = jso.getString("uname1");
					uphone = jso.getString("uphonenumber1");
					faceimgurl = jso.getString("faceimage1");
					
					//异步加载好友头像图片
//					jp.loadImage("http://www.hello008.com/Public/Uploads/"+faceimgurl);
					faceimg =  AppCache.getImage("http://www.hello008.com/Public/Uploads/"+faceimgurl);
					
					Map<String, Object> friend = new HashMap<String, Object>();
					friend.put("faceimg",faceimg);
					friend.put("faceimgurl",faceimgurl);
					friend.put("uname",uname);
					friend.put("uphone",uphone);
					friends.add(friend);
				}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		return friends;
	}
	
}
