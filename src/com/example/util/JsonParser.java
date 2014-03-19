package com.example.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.model.Friend;

public class JsonParser {
	
	//查询当前登录用户好友数据 json解析
	public static List<Map<String, Object>> parseJsonList(String json){
		String uname = null;//好友名称
		String uphone = null;//好友手机号码
		List<Map<String, Object>> friends = null;
		
		try {
				JSONArray jsa = new JSONObject(json).getJSONArray("info");
				friends = new ArrayList<Map<String,Object>>();
				for(int i = 0; i<jsa.length();i++){
					JSONObject jso = (JSONObject)jsa.opt(i);
					uname = jso.getString("uname1");
					uphone = jso.getString("uphonenumber1");
					Map<String, Object> friend = new HashMap<String, Object>();
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
